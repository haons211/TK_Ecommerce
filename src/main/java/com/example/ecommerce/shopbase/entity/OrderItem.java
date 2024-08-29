package com.example.ecommerce.shopbase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;



    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private Status ord_item_status;



    private String ord_item_code;
    private Integer ord_item_quantity;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    public enum Status {
        CREATING,SUCCESSFUL,FAILED
    }


}
