package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.product.ProductDTO;
import com.example.ecommerce.shopbase.dto.product.ProductValueCombination;
import com.example.ecommerce.shopbase.dto.request.ProductRequest;
import com.example.ecommerce.shopbase.dto.response.*;
import com.example.ecommerce.shopbase.dto.response.ProductResponse;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.mapper.ProductMapper;
import com.example.ecommerce.shopbase.repository.AssortmentRepository;
import com.example.ecommerce.shopbase.repository.CategoryRepository;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.example.ecommerce.shopbase.service.cartItem.CartItemServiceImpl;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.example.ecommerce.shopbase.repository.*;
import com.example.ecommerce.shopbase.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    AssortmentRepository assortmentRepository;
    AssortmentService assortmentService;
    ProductAttributeService productAttributeService;
    SecurityUtils securityUtils;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    ProductAssetService productAssetServiceImpl;
    ProductAssetRepository productAssetRepository;
    ValueCombinationService valueCombinationServiceImpl;
    ProductAttributeRepository productAttributeRepository;
    AssortmentService assortmentServiceImpl;
    SellerRepository sellerRepository;
    ProductAttributeService productAttributeServiceImpl;
    BaseRedisService baseRedisService;
    ObjectMapper objectMapper;
    ValueCombinationRepository valueCombinationRepository;
    ValueRepository valueRepository;
    private final CartItemServiceImpl cartItemServiceImpl;
    private final CartItemRepository cartItemRepository;
    private final CartItemAttributeRepository cartItemAttributeRepository;
    private final OrderItemRepository orderItemRepository;
    private final ValueServiceImpl valueServiceImpl;

    @Override
    public List<Product> getallProduct() {

        //return productRepository.findProductsByStatus("SHOW");
        return productRepository.findAll();
    }

    @Override
    public ProductDTO getProductAndAttributeAssortment(int id) {
        ProductDTO productDTO = ProductDTO.builder()
                .product(productRepository.findById(id).get())
                .productAssets(productAssetServiceImpl.getProductAssetList(id))
                .attributes(productAttributeService.getProductAttributesByProductId(id))
                .assortments(assortmentService.getAssortmentsByProductId(id))
                .build();
        return productDTO;
    }

    @Override
    public ProductDTO getProductbyId(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        ProductDTO productDTO = ProductDTO.builder()
                .product(productRepository.findById(id).get())
                .productAssets(productAssetServiceImpl.getProductAssetList(id))
                .attributes(productAttributeService.getProductAttributesByProductId(id))
                .assortments(assortmentService.getAssortmentsByProductId(id))
                //.valueCombinations(assortmentService.g(id))
                .build();
        return productDTO;
    }

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest productRequestDTO) {
        User currentUser = securityUtils.getCurrentUserLogin();

        Seller seller = sellerRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        Product product = productMapper.toProduct(productRequestDTO);

        if(!categoryRepository.existsById(productRequestDTO.getCategoryId()))
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);

        Category category = categoryRepository.getReferenceById(productRequestDTO.getCategoryId());

        product.setCategory(category);
        product.setSeller(seller);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse updateProduct(int id, ProductRequest productRequestDTO) {
        User currentUser = securityUtils.getCurrentUserLogin();

        Seller seller = sellerRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));;

        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setDescription(productRequestDTO.getDescription());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setQuantity(productRequestDTO.getQuantity());
        existingProduct.setWeight(productRequestDTO.getWeight());
        int categoryId = productRequestDTO.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        existingProduct.setCategory(category);
        existingProduct.setSeller(seller);


        Product savedProduct = productRepository.save(existingProduct);

        return productMapper.toProductResponse(savedProduct);
    }
    @Override
    @Transactional
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<CartItem> cartItems = cartItemRepository.findAllByProductId(id);
        for (CartItem cartItem : cartItems) {

            cartItemAttributeRepository.deleteByCartItem(cartItem);
            List<OrderItem> orderItems = orderItemRepository.findAllByCartItemId(cartItem.getId());
            for (OrderItem orderItem : orderItems) {
               // orderItemRepository.updateCartItemIdToZero(cartItem.getId());
                orderItemRepository.deleteAllByCartItemId(cartItem.getId());

            }
            cartItemRepository.deleteById(cartItem.getId());

            cartItemRepository.delete(cartItem);
        }

        // Delete all related ProductAssets
        productAssetRepository.deleteAllByProductId(id);

        // Delete all related ProductAttributes
        productAttributeRepository.deleteAllByProductId(id);

        // Delete the Assortment
        assortmentServiceImpl.delete(id);

        // Delete the Value
        valueServiceImpl.delete(id);

        // Delete the Product
        productRepository.delete(product);
    }
    @Override
    public Integer getSellerId(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));
        return product.getSeller().getId();
    }

    @Override
    public Page<Product> getProductByCategory(int categoryId,Pageable pageable) {
        Page<Product> products = productRepository.findProductByCategoryID(categoryId,pageable);
        return products;
    }

    @Override
    public List<List<String>> getFileByProductId(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//
        List<ProductAsset> productAssets = productAssetRepository.findAllById(Collections.singleton(productId));

        List<String> filePaths = productAssets.stream()
                .map(ProductAsset::getName)
                .collect(Collectors.toList());

        return Collections.singletonList(filePaths);
    }

    @Override
    public List<List<String>> getFilePathsByProductId(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
//
        List<ProductAsset> productAssets = productAssetRepository.findAllById(Collections.singleton(productId));

        List<String> filePaths = productAssets.stream()
                .map(ProductAsset::getName)
                .collect(Collectors.toList());

        return Collections.singletonList(filePaths);
    }

    @Override
    public Integer getSoldbySeller() {
        User currentUser = securityUtils.getCurrentUserLogin();
        int userId = currentUser.getId();
        int seller_id = sellerRepository.findSellersByUserId(userId).getId();
        Integer sold = productRepository.getTotalSoldBySellerId(seller_id);
        return sold;
    }


    @Override
    public List<ProductFullResponse> getFullProductDetails(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ProductResponse productResponse = productMapper.toProductResponse(product);
        List<String> filepaths = productAssetServiceImpl.getImg(product.getId());

        if (!filepaths.isEmpty()) {
            productResponse.setCategoryId(product.getCategory().getID());
            productResponse.setImageUrl(filepaths.getFirst());
            productResponse.setSold(product.getSold());
        } else {
            productResponse.setCategoryId(product.getCategory().getID());
            productResponse.setImageUrl("default_image_url");
            productResponse.setSold(product.getSold());
        }

        List<ProductAttribute> productAttributes = productAttributeServiceImpl.getProductAttributesByProductId(productId);
        List<ProductAttributeResponse> productAttributeResponses = new ArrayList<>();
        for (ProductAttribute productAttribute : productAttributes) {
            ProductAttributeResponse response = ProductAttributeResponse.builder()
                    .value(productAttribute.getValue())
                    .name(productAttribute.getName())
                    .productId(product.getId())
                    .build();
            productAttributeResponses.add(response);
        }


        List<ProductAsset> productAssets = productAssetRepository.findAllByProductId(productId);

        List<ProductAssetResponse> productAssetResponses = new ArrayList<>();
        for (ProductAsset productAsset : productAssets) {
            ProductAssetResponse productAssetResponse = ProductAssetResponse.builder()
                    .name(filepaths.getFirst())
                    .productId(productAsset.getProduct().getId())
                    .build();
            productAssetResponses.add(productAssetResponse);
        }

        List<ValueCombination> valueCombinations = valueCombinationServiceImpl.getAllValueCombinationsbyProductId(productId);
        List<ValueCombinationResponse> valueCombinationResponses = new ArrayList<>();
        for (ValueCombination valueCombination : valueCombinations) {
            ValueCombinationResponse valueCombinationResponse = ValueCombinationResponse.builder()
                    .id(valueCombination.getId())
                    .price(valueCombination.getPrice())
                    .value1(valueCombination.getValue1().getValue())
                    .value2(valueCombination.getValue2().getValue())
                    .quantity(valueCombination.getQuantity())
                    .price(valueCombination.getPrice())
                    .productId(productId)
                    .build();
            valueCombinationResponses.add(valueCombinationResponse);
        }

        // Build ProductFullResponse
        ProductFullResponse fullResponse = ProductFullResponse.builder()
                .productResponse(productResponse)
                .productAttributeResponse(productAttributeResponses)
                .productAssetResponse(productAssetResponses)
                .valueCombinationResponse(valueCombinationResponses)
                .build();

        return List.of(fullResponse);
    }

    @Override
    public Page<ProductFullResponse> getProductbySeller(int sellerId, Pageable pageable) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));

        Page<Product> products = productRepository.findAllBySellerId(seller.getId(),pageable);

        List<ProductFullResponse> productFullResponses = new ArrayList<>();

        Page<ProductFullResponse> productFullResponsesPage = products.map(product -> {
            List<String> filepaths = productAssetServiceImpl.getImg(product.getId());

            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponse.setCategoryId(product.getCategory().getID());
            productResponse.setImageUrl(filepaths.isEmpty() ? "default_image_url" : filepaths.getFirst());
            productResponse.setSold(product.getSold());

            // Get product attributes, assets, and value combinations regardless of image availability
            List<ProductAttribute> productAttributes = productAttributeServiceImpl.getProductAttributesByProductId(product.getId());
            List<ProductAttributeResponse> productAttributeResponses = new ArrayList<>();
            for (ProductAttribute productAttribute : productAttributes) {
                ProductAttributeResponse response = ProductAttributeResponse.builder()
                        .value(productAttribute.getValue())
                        .name(productAttribute.getName())
                        .productId(product.getId())
                        .build();
                productAttributeResponses.add(response);
            }

            List<ProductAssetResponse> productAssetResponses = new ArrayList<>();
            for (String filePath : filepaths) {
                ProductAssetResponse productAssetResponse = ProductAssetResponse.builder()
                        .name(filePath)
                        .productId(product.getId())
                        .build();
                productAssetResponses.add(productAssetResponse);
            }

            // Value Combinations
            List<ValueCombination> valueCombinations = valueCombinationServiceImpl.getAllValueCombinationsbyProductId(product.getId());
            List<ValueCombinationResponse> valueCombinationResponses = new ArrayList<>();
            for (ValueCombination valueCombination : valueCombinations) {
                ValueCombinationResponse valueCombinationResponse = ValueCombinationResponse.builder()
                        .id(valueCombination.getId())
                        .price(valueCombination.getPrice())
                        .value1(valueCombination.getValue1().getValue())
                        .value2(valueCombination.getValue2() != null ? valueCombination.getValue2().getValue() : null)
                        .quantity(valueCombination.getQuantity())
                        .price(valueCombination.getPrice())
                        .productId(product.getId())
                        .build();
                valueCombinationResponses.add(valueCombinationResponse);
            }

            // Build ProductFullResponse
            ProductFullResponse fullResponse = ProductFullResponse.builder()
                    .productResponse(productResponse)
                    .productAttributeResponse(productAttributeResponses)
                    .productAssetResponse(productAssetResponses)
                    .valueCombinationResponse(valueCombinationResponses)
                    .build();
            return fullResponse;
        });


        return productFullResponsesPage; // Return the list of ProductFullResponse
    }

    public Page<ProductDetailDTO> getProductBySellerV2(int sellerId, Pageable pageable) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(ErrorCode.SELLER_NOT_EXISTED));

        Page<Product> products = productRepository.findAllBySellerId(seller.getId(),pageable);

        return products.map(this::mapToProductDTO);
    }

    @Override
    public Page<ProductDetailDTO> getProductbySellerForSeller(Pageable pageable) {
        User currentUser = securityUtils.getCurrentUserLogin();
        int userId = currentUser.getId();
        int seller_id = sellerRepository.findSellersByUserId(userId).getId();
        return getProductBySellerV2(seller_id,pageable);
    }

    @Override
    //@Cacheable(value = "product",cacheManager = "cacheManager", key = "#id")
    public ProductDTO getFullInfoProduct(int id) {
        if(baseRedisService.get("product::"+id) != null){
            return objectMapper.convertValue(baseRedisService.get("product::"+id),ProductDTO.class);
        }
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            ProductDTO productDTO = ProductDTO.builder()
                    .product(product)
                    .attributes(productAttributeService.getProductAttributesByProductId(id))
                    .assortments(assortmentService.getAssortmentsByProductId(id))
                    .build();
            baseRedisService.set("product::"+id,productDTO);
            baseRedisService.setTimeToLiveInMinutes("product::"+id,3);
            addProductValueCombination(getProductValueCombination(product));
            return productDTO;
        }
        return null;
    }

    @Override
    public ProductValueCombination getProductValueCombination(Product product) {
        if (product != null) {
            ProductValueCombination productValueCombination = ProductValueCombination.builder()
                    .product(product)
                    .valueCombinations(valueCombinationRepository.findAllByProduct(product))
                    .build();
            return productValueCombination;
        }
        return null;
    }

    private void addProductValueCombination(ProductValueCombination productValueCombination){
        for(ValueCombination valueCombination : productValueCombination.getValueCombinations()){
            baseRedisService.hashSet("p_hash::"+productValueCombination.getProduct().getId(),valueCombination.getId().toString(),valueCombination.getQuantity());
            baseRedisService.setTimeToLiveInMinutes("p_hash::"+productValueCombination.getProduct().getId(),3);
        }
    }

    public ProductDetailDTO getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return mapToProductDTO(product);
    }

    public ProductDetailDTO mapToProductDTO(Product product) {
        ProductDetailDTO productDetailDTO = productMapper.toProductDetailDTO(product);

        List<String> assets = productAssetServiceImpl.getImg(product.getId());
        productDetailDTO.setAssets(assets);
        productDetailDTO.setImageUrl(assets.isEmpty() ? "default image" : assets.getFirst());

        List<Assortment> assortments = assortmentRepository.findAllByProduct(product);
        List<ProductAssortmentResponse> productAssortments = assortments.stream().map(assortment -> {
            List<Value> values = valueRepository.findAllByAssortment(assortment);
            List<ValueDTO> listValueDTO = values.stream().map(value -> new ValueDTO(value.getId(), value.getValue())).toList();
            return new ProductAssortmentResponse(assortment.getName(), listValueDTO);
        }).toList();

        List<ProductAttribute> attributes = productAttributeRepository.findAllByProductId(product.getId());
        List<ProductAttributeResponse> attributeResponses = attributes.stream().map(attribute ->
                ProductAttributeResponse
                        .builder()
                        .value(attribute.getValue())
                        .name(attribute.getName())
                        .build()).toList();

        List<ValueCombination> valueCombinations = valueCombinationServiceImpl.getAllValueCombinationsbyProductId(product.getId());
        List<ValueCombinationResponse> valueCombinationResponses = valueCombinations.stream().map(valueCombination ->
                ValueCombinationResponse.builder()
                        .id(valueCombination.getId())
                        .price(valueCombination.getPrice())
                        .value1(valueCombination.getValue1().getValue())
                        .value2(valueCombination.getValue2() != null ? valueCombination.getValue2().getValue() : null)
                        .quantity(valueCombination.getQuantity())
                        .price(valueCombination.getPrice())
                        .productId(product.getId())
                        .build()).toList();

        productDetailDTO.setAssortments(productAssortments);
        productDetailDTO.setAttributes(attributeResponses);
        productDetailDTO.setOptions(valueCombinationResponses);

        return productDetailDTO;
    }


    @Override
    public Integer getQuantitybySeller() {
        User currentUser = securityUtils.getCurrentUserLogin();
        int userId = currentUser.getId();
        int seller_id = sellerRepository.findSellersByUserId(userId).getId();
        Integer quantity = productRepository.getTotalQuantityBySellerId(seller_id);
        return quantity;
    }
    @Override
    public Page<Product> getSoldOutProductBySeller(Pageable pageable) {
        User currentUser = securityUtils.getCurrentUserLogin();
        int userId = currentUser.getId();
        int sellerId = sellerRepository.findSellersByUserId(userId).getId();

        List<Product> soldOutProducts = productRepository.findAllBySellerId(sellerId, pageable)
            .stream()
            .filter(product -> product.getQuantity() == 0)
            .collect(Collectors.toList());

        return new PageImpl<>(soldOutProducts, pageable, soldOutProducts.size());
    }

    @Override
    public Map<String, Object> getRevenueBySeller() {
        User currentUser = securityUtils.getCurrentUserLogin();
        int userId = currentUser.getId();
        int sellerId = sellerRepository.findSellersByUserId(userId).getId();
        List<Map<String, Object>> revenueList = productRepository.getTotalRevenueByMonthForSeller(sellerId);
        Map<String, Object> revenueMap = new HashMap<>();

        for (Map<String, Object> revenueItem : revenueList) {
            int month = 0;
            int year = 0;
            double totalRevenue = 0.0;

            if (revenueItem.get("month") != null && revenueItem.get("month") instanceof String) {
                month = Integer.parseInt((String) revenueItem.get("month"));
            }
            if (revenueItem.get("year") != null && revenueItem.get("year") instanceof String) {
                year = Integer.parseInt((String) revenueItem.get("year"));
            }
            if (revenueItem.get("total_revenue") != null && revenueItem.get("total_revenue") instanceof Number) {
                totalRevenue = ((Number) revenueItem.get("total_revenue")).doubleValue();
            }

            String key = String.format("%d-%02d", year, month);
            revenueMap.put(key, totalRevenue);
        }

        return revenueMap;
    }

    @Override
    public Double getSumRevenueBySeller() {
        User currentUser = securityUtils.getCurrentUserLogin();
        int userId = currentUser.getId();
        int sellerId = sellerRepository.findSellersByUserId(userId).getId();
        List<Map<String, Object>> revenueList = productRepository.getTotalRevenueByMonthForSeller(sellerId);

        double totalRevenue = 0.0;

        for (Map<String, Object> revenueItem : revenueList) {
            double monthlyRevenue = 0.0;
            if (revenueItem.get("total_revenue") != null && revenueItem.get("total_revenue") instanceof Number) {
                monthlyRevenue = ((Number) revenueItem.get("total_revenue")).doubleValue();
            }
            totalRevenue += monthlyRevenue;
        }

        return totalRevenue;
    }


}





