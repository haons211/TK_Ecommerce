package com.example.ecommerce.shopbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "token", length = 100, nullable = false, unique = true)
    String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "`type`")
    @Enumerated(EnumType.STRING)
    Type type;

    @Column(name = "expired_at")
    Date expiredAt;

    public enum Type {
        REFRESH, FORGOT_PASSWORD
    }
}
