package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.entity.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ValueRepository extends JpaRepository<Value, Integer> {

    @Modifying
    @Query(value = "update value set quantity = quantity - :quantity where value.id = :id",nativeQuery = true)
    public void reductQuantity(int id, int quantity);

    @Modifying
    @Query(value = "update value set quantity = quantity + :quantity where value.id = :id",nativeQuery = true)
    public void increaseQuantity(int id, int quantity);

    List<Value> findAllByAssortment_ID(int assortmentId);

    List<Value> findAllByAssortment(Assortment assortment);
}
