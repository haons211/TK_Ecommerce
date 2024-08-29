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
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Integer id;

    @Column(name ="name", nullable = false)
    String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    Type type;

    @Column(name = "address_parentID", nullable = false)
    Integer parentId;

    @ManyToOne
    @JoinColumn(name = "created_user_id")
    User user;

    @OneToOne(mappedBy = "address")
    AddressShipping addressShipping;

    public enum Type {
        City, District, Ward
    }
}
