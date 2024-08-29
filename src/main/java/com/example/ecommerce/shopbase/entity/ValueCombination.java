package com.example.ecommerce.shopbase.entity;

import com.example.ecommerce.shopbase.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "value_combination")
public class ValueCombination implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "value_id1", referencedColumnName = "id")
    private Value value1;

    @ManyToOne
    @JoinColumn(name = "value_id2", referencedColumnName = "id")
    private Value value2;

    @Column(name = "price")
    private Integer price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sold", nullable = false, columnDefinition = "int default 0")
    private Integer sold = 0;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    Product product;


}
