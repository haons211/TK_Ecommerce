package com.example.ecommerce.shopbase.repository;


import com.example.ecommerce.shopbase.entity.Token;
import com.example.ecommerce.shopbase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {


    Optional<Token> findByTypeAndToken(Token.Type type, String key);

    @Modifying
    void deleteByTypeAndUser(Token.Type type, User user);

}
