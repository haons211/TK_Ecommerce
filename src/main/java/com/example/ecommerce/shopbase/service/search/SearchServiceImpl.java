
package com.example.ecommerce.shopbase.service.search;

import com.example.ecommerce.shopbase.dto.response.SearchProductResponse;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.ProductES;
import com.example.ecommerce.shopbase.mapper.ProductMapper;
import com.example.ecommerce.shopbase.repository.ProductESRepository;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.service.product.ProductService;
import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import com.example.ecommerce.shopbase.service.redis.RedisListService;
import com.example.ecommerce.shopbase.service.storage.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    ProductESRepository productESRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RedisListService redisListService;

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    SearchCustomQuery searchCustomQuery;

    @Autowired
    StorageService storageService;


    @Override
    public Page<ProductES> searchProducts(String queryString, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<ProductES> page = productESRepository.findProductESBySearch(queryString,"AUTO",pageable);
        return page;
    }

    @Override
    public Page<ProductES> searchProducts(String queryString, String fuzzyNumber, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return productESRepository.findProductESBySearch(queryString,fuzzyNumber,pageable);
    }

    @Override
    public Page<SearchProductResponse> getSearchedProducts(String queryString, int pageNumber, int pageSize, String type, String order, Integer minPrice, Integer maxPrice) {
        List<Integer> listId = new ArrayList<>();
        Page<ProductES> productESPage;

        if(minPrice!=null && maxPrice!=null){
            productESPage = productESRepository.findProductESByPriceRange(queryString,"AUTO",minPrice,maxPrice,PageRequest.of(pageNumber,pageSize));
        }
        else if(type!=null && order!=null){
            productESPage = sortPriceOrSold(queryString,type,order,PageRequest.of(pageNumber,pageSize));
        }
        else{
            productESPage = searchProducts(queryString,pageNumber,pageSize);
        }

        productESPage.getContent().forEach(
                productES -> {
                    listId.add(productES.getProduct_id());
                }
        );

        List<SearchProductResponse> searchProductResponses = getProductDetailResponse(listId);

        return new PageImpl<>(searchProductResponses, PageRequest.of(pageNumber,pageSize),productESPage.getTotalElements());
    }

    @Override
    public List<SearchProductResponse> getProductDetailResponse(List<Integer> listId) {
        List<SearchProductResponse> searchProductResponses = new ArrayList<>();

        List<Product> products = searchCustomQuery.searchProduct(listId);
        products.forEach(product -> {
            product.setImageDefault(storageService.getObjectURL(product.getImageDefault()));
            searchProductResponses.add(productMapper.toSearchProductResponse(product));
        });
        return searchProductResponses;
    }

    @Override
    public Page<SearchProductResponse> getHomePage(Pageable pageable) {
        Page<SearchProductResponse> searchProductResponsePage;
        Page<Product> productPage = productRepository.findAllByOrderBySold(pageable);
        searchProductResponsePage = productPage.map(product -> productMapper.toSearchProductResponse(product));
        searchProductResponsePage.getContent().forEach(item->{
            item.setImageUrl(storageService.getObjectURL(item.getImageUrl()));
        });
        return searchProductResponsePage;
    }

    public Page<ProductES> sortPriceOrSold(String searchQuery, String type, String order, Pageable pageable){
        String elasticsearchQuery = "{ \"multi_match\": {" +
                "      \"query\": \""+searchQuery+"\"," +
                "      \"fields\":[\"name^2\",\"nameWithoutAccent^1\"]," +
                "      \"fuzziness\": \"AUTO\"," +
                "      \"type\": \"best_fields\"" +
                "    }" +
                "  }";

        Query query = new StringQuery(elasticsearchQuery);
// Thêm sắp xếp
        Sort sort = Sort.by(order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, type);
        query.addSort(sort);
        query.addSort(Sort.by(Sort.Direction.DESC, "_score"));

        // Thêm phân trang
        query.setPageable(pageable);
        SearchHits<ProductES> searchHits = elasticsearchOperations.search(query, ProductES.class);

        List<ProductES> products =searchHits.get().map(searchHit -> searchHit.getContent()).collect(Collectors.toList());

        return new PageImpl<>(products, pageable, searchHits.getTotalHits());
    }

}
