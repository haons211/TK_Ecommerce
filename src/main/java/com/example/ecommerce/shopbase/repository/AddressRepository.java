package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findByName(String name);

    Optional<Address> findByIdAndType(Integer id, Address.Type type);

    @Query("from Address where parentId = :parentId")
    List<Address> findAllByParentId(Integer parentId);
}
