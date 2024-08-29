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
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private Timestamp payment_time;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    public enum Status {
        CREATING, SUCCESSFULL, FAILED
    }

    @Column(nullable = false)
    private String provider;
    private String transaction_id;
    private String bank_code;

}
