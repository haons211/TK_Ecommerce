package com.example.ecommerce.shopbase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String slug;

    @Column(name = "image_default")
    private String imageDefault;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    public enum Status {
        SHOW, HIDE
    }

    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp created_at;

    @Column(nullable = false, insertable = false)
    private Timestamp updated_at;

    @Column(nullable = false)
    private Integer weight;

    private Timestamp deleted_at;
    private Integer price;
    private Integer quantity;
    private Integer sold = 0;
    private Integer created_by_user;

}