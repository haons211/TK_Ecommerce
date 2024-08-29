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
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name="shipping_fee")
    private Integer shippingfee;

    @Column(nullable = false)
    private int total;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp created_at;

    private Timestamp canceled_at;
    private Timestamp completed_at;
    private Timestamp delivery_at;
    private String order_code;
    public enum Status{
        CREATING, SUCCESSFUL , FAILED
    }
}
