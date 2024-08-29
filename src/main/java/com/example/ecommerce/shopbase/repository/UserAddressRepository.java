package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {

    @Query("from UserAddress where user=:user and isDefault=true")
    Optional<UserAddress> findDefaultAddress(User user);

    List<UserAddress> findAllByUser(User user);
}
