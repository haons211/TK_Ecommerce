package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);
}
