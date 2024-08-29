package com.example.ecommerce.shopbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "address_shipping")
public class AddressShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @OneToOne
    @JoinColumn(name = "address_id")
    Address address;

    @Column(name = "address_shipping_code")
    String addressShippingCode;

    @Column(name = "shipping_company")
    String shippingCompany;

    @ManyToOne
    @JoinColumn(name = "created_user_id")
    User user;
}
