package com.example.ecommerce.shopbase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_asset")
public class ProductAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;


    @Column(name = "name") // Rename the column for clarity
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;



}
