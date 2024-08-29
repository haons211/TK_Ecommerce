package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.ProductES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;

public interface ProductESRepository extends ElasticsearchRepository<ProductES,String> {

    @Query("{ \"multi_match\": {" +
            "      \"query\": \"?0\"," +
            "      \"fields\":[\"name^2\",\"nameWithoutAccent^1\"]," +
            "      \"fuzziness\": \"?1\"," +
            "      \"type\": \"best_fields\"" +
            "    }" +
            "  }")
    Page<ProductES> findProductESBySearch(String searchQuery, String fuzzyNumber, Pageable pageable);

    @Query("{ \"term\" : {" +
            "   \"product_id\": ?0\n" +
            "    }" +
            "  }")
    ProductES findByProductId(String productIds);

    @Query("{ \"terms\" : {" +
            "   \"product_id\": #{#productIds}\n" +
            "    }" +
            "  }")
    List<ProductES> findAllByProductIds(List<String> productIds);

    @Query("{" +
            "  \"bool\":{"  +
            "    \"must\":{"  +
            "      \"multi_match\":{"  +
            "        \"query\":\"?0\","  +
            "        \"fields\":[\"name^3\",\"nameWithoutAccent^2\"]," +
            "        \"fuzziness\":\"?1\","  +
            "        \"type\":\"best_fields\""  +
            "      }"  +
            "    },"  +
            "    \"filter\":{"  +
            "      \"range\":{"  +
            "        \"price\":{"  +
            "          \"gte\":?2,"  +
            "          \"lte\":?3"  +
            "        }"  +
            "      }"  +
            "    }"  +
            "  }"  +
            "}")
    Page<ProductES> findProductESByPriceRange(String searchQuery, String fuzzyNumber,Integer minPrice,Integer maxPrice ,Pageable pageable);

    @Query("{"
            + "\"bool\": {"
            + "  \"must\": {"
            + "    \"multi_match\": {"
            + "      \"query\": \"?0\","
            + "      \"fields\": [\"name^3\", \"nameWithoutAccent^2\"],"
            + "      \"fuzziness\": \"?1\","
            + "      \"type\": \"best_fields\""
            + "    }"
            + "  }"
            + "},"
            + "\"sort\": ["
            + "  \"_score\","
            + "  {\"price\": \"?2\"}"
            + "]"
            + "}")
    Page<ProductES> findProductESByPriceSorted(String searchQuery, String fuzzyNumber, String order, Pageable pageable);

    @Query("{"
            + "\"bool\": {"
            + "  \"must\": {"
            + "    \"multi_match\": {"
            + "      \"query\": \"?0\","
            + "      \"fields\": [\"name^3\", \"nameWithoutAccent^2\"],"
            + "      \"fuzziness\": \"?1\","
            + "      \"type\": \"best_fields\""
            + "    }"
            + "  }"
            + "},"
            + "\"sort\": ["
            + "  \"_score\","
            + "  {\"sold\": \"?2\"}"
            + "]"
            + "}")
    Page<ProductES> findProductESBySoldSorted(String searchQuery, String fuzzyNumber, String order, Pageable pageable);

}
