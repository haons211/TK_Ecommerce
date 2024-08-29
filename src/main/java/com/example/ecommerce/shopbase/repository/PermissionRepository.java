package com.example.ecommerce.shopbase.repository;

import com.example.ecommerce.shopbase.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
