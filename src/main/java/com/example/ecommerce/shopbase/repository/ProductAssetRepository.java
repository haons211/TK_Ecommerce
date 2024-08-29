package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.ProductAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductAssetRepository extends JpaRepository<ProductAsset, Integer> {
    List<ProductAsset> findAllByProductId(int productID);
    Optional<ProductAsset> findFirstByName(String filename);

    void deleteAllByProductId(int id);

    @Query(value = "SELECT pa.name FROM product_asset pa WHERE pa.product_id = :product_id AND pa.default_asset=1 LIMIT 1",nativeQuery = true)
    String getUrlDefaultByProduct(int product_id);

}
