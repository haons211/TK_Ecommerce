package com.example.ecommerce.shopbase.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "name", nullable = false, unique = true)
    String username;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "created_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdDateTime;

    @Column(name = "updated_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedDateTime;

    @Column(name = "last_change_password_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastChangePasswordDateTime;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "user")
    List<UserAddress> userAddresses;

    @PrePersist
    public void setDefault() {
        if(status == null) {
            status = Status.BLOCKED;
        }
    }

    public enum Status {
        ACTIVE, INACTIVE,BLOCKED
    }
}
