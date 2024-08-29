package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Address;
import com.example.ecommerce.shopbase.entity.AddressShipping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressShippingRepository extends JpaRepository<AddressShipping, Integer> {

    AddressShipping findByAddress(Address address);

}
