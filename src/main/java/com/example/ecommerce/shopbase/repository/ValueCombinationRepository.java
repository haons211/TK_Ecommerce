package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.dto.product.ValueCombinationDTO;
import com.example.ecommerce.shopbase.dto.request.ValueCombinationRequest;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.List;

public interface ValueCombinationRepository extends JpaRepository<ValueCombination, Integer> {

    Optional<ValueCombination> findFirstByValue1AndValue2(Value value1, Value value2);

    Optional<ValueCombination> findFirstByValue1(Value value1);
    Optional<ValueCombination> findFirstByProduct(Product product);
    @Query("SELECT vc.price "
            + "FROM ValueCombination vc "
            + "WHERE (:valueIds IS NULL OR vc.value1.id IN (:valueIds)) "
            + "  AND (:valueIds IS NULL OR vc.value2.id IN (:valueIds))")
    int getPricesByValueIdsWithBothValues(List<Integer> valueIds);

    @Query("SELECT vc.price "
            + "FROM ValueCombination vc "
            + "WHERE vc.value1.id = :valueId "
            + "   AND vc.value2 IS NULL")
    int getPricesByValue1(Integer valueId);

    @Query("SELECT v.product FROM ValueCombination v WHERE v.value1 = :valueId")
    Product findProductByValueId(@Param("valueId") int valueId);


    List<ValueCombination> findValueCombinationByValue1OrValue2(Value first, Value second);

    List<ValueCombination> findAllByProduct(Product product);

    @Modifying
    @Transactional
    @Query("UPDATE ValueCombination vc SET vc.quantity = :quantity WHERE vc.id = :id")
    void updateQuantityById(Integer quantity,Integer id);

   List<ValueCombination> findAllByProductId(Integer productId);


}
