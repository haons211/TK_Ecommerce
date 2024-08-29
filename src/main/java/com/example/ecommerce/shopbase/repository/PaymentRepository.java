package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
