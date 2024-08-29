package com.example.ecommerce.shopbase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int value;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int coupon_per_user;

    @Column(nullable = false)
    private int coupon_uses;

    @Column(nullable = false)
    private Timestamp start_time;

    @Column(nullable = false)
    private Timestamp end_time;

    @Column(nullable = false)
    private int min_spend;

    @Column(nullable = false)
    private int max_spend;
}
