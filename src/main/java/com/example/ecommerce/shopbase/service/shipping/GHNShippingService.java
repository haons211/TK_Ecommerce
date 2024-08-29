package com.example.ecommerce.shopbase.service.shipping;

import com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN.GHNShippingRequest;
import com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN.GHNShippingResponse;
import com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN.ServicePackageRequest;
import com.example.ecommerce.shopbase.api.thirdparty.shipping.GHN.ServicePackageResponse;
import com.example.ecommerce.shopbase.api.thirdparty.shipping.ShippingRequest;
import com.example.ecommerce.shopbase.dto.CartItemDetails;
import com.example.ecommerce.shopbase.dto.response.ShippingFeeResponse;
import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.service.order.OrderService;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("GHN")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GHNShippingService implements ShippingService {


    RestTemplate restTemplate;
    Dotenv dotenv;
    String BASE_URL;
    String ACCESS_TOKEN;
    OrderService orderService;
    BaseRedisService redisService;
    SecurityUtils securityUtils;
    ProductRepository productRepository;


    public GHNShippingService(RestTemplate restTemplate, Dotenv dotenv, OrderService orderService, BaseRedisService redisService, SecurityUtils securityUtils, ProductRepository productRepository) {
        this.restTemplate = restTemplate;
        this.orderService = orderService;
        this.redisService = redisService;
        this.dotenv = dotenv;
        this.BASE_URL = dotenv.get("GHN_WEB");
        this.ACCESS_TOKEN = dotenv.get("GHN_TOKEN");
        this.securityUtils = securityUtils;
        this.productRepository = productRepository;
    }


    // Validate token and URL
    private void validateTokenAndUrl() {
        if (ACCESS_TOKEN == null) {
            throw new AppException(ErrorCode.TOKEN_NOT_EXISTED);
        }
        if (BASE_URL == null) {
            throw new AppException(ErrorCode.URL_NOT_FOUND);
        }
    }
    @Override
    public int getTotalFee(ShippingRequest request,String orderId) throws JsonProcessingException {

        GHNShippingRequest ghnRequest_Con= (GHNShippingRequest) request;
        validateTokenAndUrl();
        String uri = "/shiip/public-api/v2/shipping-order/fee";
        String URL = BASE_URL + uri;
        var currentUser = securityUtils.getCurrentUserLogin();
        List<CartItemDetails> listItemsFromOrder = orderService.getPreOrderDetail(orderId);

        Map<Integer, List<CartItemDetails>> groupedItems = groupByShop(listItemsFromOrder);

        return groupedItems.entrySet().stream()
                .mapToInt(entry -> {
                    List<CartItemDetails> listItems = entry.getValue();
                    int weight = getWeightOfItems(listItems);
                    int cost = getCostOfItems(listItems);
                    int service_type_id = weight < 20000 ? 2 : 5;
                    int shopId = 193113; // GetShopIdbySellerId
                    Integer toDistrictId = ghnRequest_Con.getTo_district_id();
                    GHNShippingRequest ghnRequest = createShippingRequest(service_type_id, weight, cost, listItems,toDistrictId);
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Token", ACCESS_TOKEN);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("ShopId", String.valueOf(shopId));
                    HttpEntity<GHNShippingRequest> entity = new HttpEntity<>(ghnRequest, headers);
                    ResponseEntity<GHNShippingResponse> responseEntity = restTemplate.exchange(
                            URL,
                            HttpMethod.POST,
                            entity,
                            GHNShippingResponse.class
                    );
                    GHNShippingResponse response = responseEntity.getBody();
                    if (response != null && response.getData() != null) {
                        return response.getData().getTotal();
                    } else {
                        return 0;
                    }
                })
                .sum();
    }
    @Override
    public ShippingFeeResponse returnShippingFeeResponse(ShippingRequest request,String orderId) throws JsonProcessingException {
        List<CartItemDetails> listItemsFromOrder = orderService.getPreOrderDetail(orderId);
        Integer totalFee=getTotalFee(request,orderId);
        Integer realCost= orderService.getRealPrice(listItemsFromOrder);
        Integer totalShippingFee= totalFee-realCost;

        ShippingFeeResponse result = ShippingFeeResponse.builder().
                total_cost(totalFee)
                        .shipping_fee(totalShippingFee).build();
        return result;
    }
    // Get service package
    public ServicePackageResponse getServicePackage() {
        validateTokenAndUrl();
        ServicePackageRequest request = ServicePackageRequest.builder()
                .shop_id(885)
                .from_district(1447)
                .to_district(1442)
                .build();
        String uri = "/shiip/public-api/v2/shipping-order/available-services";
        String URL = BASE_URL + uri;

        HttpEntity<ServicePackageRequest> entity = createHttpEntity(request);

        return restTemplate.exchange(
                URL,
                HttpMethod.POST,
                entity,
                ServicePackageResponse.class
        ).getBody();
    }

    // Create shipping request
    private GHNShippingRequest createShippingRequest(int service_type_id, int weight, int cost, List<CartItemDetails> listItems, int toDistrictId) {
        return GHNShippingRequest.builder()
                .service_type_id(service_type_id)
                .to_district_id(toDistrictId)
                .to_ward_code(null)
                .height(0)
                .length(0)
                .weight(weight)
                .width(0)
                .insurance_value(cost)
                .coupon(null)
                .items(convertToItems(listItems))
                .build();
    }

    // Create HTTP entity with headers
    private <T> HttpEntity<T> createHttpEntity(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, headers);
    }

    public Map<Integer, List<CartItemDetails>> groupByShop(List<CartItemDetails> listItemsFromOrder) {
        if (listItemsFromOrder == null) {
            return Collections.emptyMap();
        }
        return listItemsFromOrder.stream()
                .collect(Collectors.groupingBy(CartItemDetails::getSellerId));
    }




    // Get all cart items from order
    public List<CartItemDetails> getAllCartItemFromOrder(String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object, Object> entries = redisService.getField(key);
        List<CartItemDetails> result = new ArrayList<>();

        for (Object value : entries.values()) {
            CartItemDetails cartItemDetails = objectMapper.convertValue(value, CartItemDetails.class);
            result.add(cartItemDetails);
        }

        return result;
    }

    // Get weight of items
    public int getWeightOfItems(List<CartItemDetails> items) {
        return items.stream().mapToInt(CartItemDetails::getWeight).sum();
    }

    // Get cost of items
    public int getCostOfItems(List<CartItemDetails> items) {
        return items.stream().mapToInt(CartItemDetails::getPrice).sum();
    }

    // Convert cart items to shipping request items
    public List<GHNShippingRequest.Item> convertToItems(List<CartItemDetails> items) {
        return items.stream()
                .map(item -> GHNShippingRequest.Item.builder()
                        .name(item.getName())
                        .code(null)
                        .quantity(item.getQuantity())
                        .height(0)
                        .weight(item.getWeight())
                        .width(0)
                        .length(0).build())
                .collect(Collectors.toList());
    }

}
