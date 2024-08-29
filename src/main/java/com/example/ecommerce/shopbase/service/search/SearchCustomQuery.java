package com.example.ecommerce.shopbase.service.search;

import com.example.ecommerce.shopbase.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchCustomQuery {
    @PersistenceContext
    private EntityManager entityManager;


    public List<Product> searchProduct(List<Integer> ids) {
        String idList = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String queryStr = "SELECT * FROM product WHERE ID IN (" + idList + ") ORDER BY FIELD(ID, " + idList + ")";

        Query query = entityManager.createNativeQuery(queryStr, Product.class);
        return query.getResultList();
    }

    public List<Product> searchProductByPriceRange(List<Integer> ids, Integer minPrice, Integer maxPrice) {
        String idList = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String queryStr = "SELECT * FROM product WHERE ID IN (" + idList + ") AND (price > " + minPrice + " AND price < " + maxPrice + ") ORDER BY FIELD(ID, " + idList + ")";

        Query query = entityManager.createNativeQuery(queryStr, Product.class);
        return query.getResultList();
    }



}
