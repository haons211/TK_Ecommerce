package com.example.ecommerce.shopbase.entity;

import com.example.ecommerce.shopbase.dto.request.ValueRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "value")
public class Value  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "assortment_id", nullable = false)
    @JsonIgnore
    private Assortment assortment;


}
