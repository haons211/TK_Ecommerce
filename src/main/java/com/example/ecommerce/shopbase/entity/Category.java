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
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false)
    private String name;

    private String description;
    private Integer parentID;
    private String image;
    private Integer created_by_user;

    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp created_at;

    @Column(nullable = false, insertable = false)
    private Timestamp updated_at;

    private Timestamp deleted_at;
}
