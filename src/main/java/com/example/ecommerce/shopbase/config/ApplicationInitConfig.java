package com.example.ecommerce.shopbase.config;

import com.example.ecommerce.shopbase.constant.PredefinedRole;
import com.example.ecommerce.shopbase.entity.Role;
import com.example.ecommerce.shopbase.entity.Seller;
import com.example.ecommerce.shopbase.entity.User;
import com.example.ecommerce.shopbase.repository.AddressRepository;
import com.example.ecommerce.shopbase.repository.RoleRepository;
import com.example.ecommerce.shopbase.repository.SellerRepository;
import com.example.ecommerce.shopbase.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";

    @NonFinal
    static final String ADMIN_ADDRESS = "Phường Phú Diễn";

    static final String ADMIN_ADDRESS_DETAIL = "Dia chi mac dinh";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        AddressRepository addressRepository,
                                        SellerRepository sellerRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                Role userRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                Role sellerRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.SELLER_ROLE)
                        .description("Seller role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);
                roles.add(userRole);
                roles.add(sellerRole);

                User admin = User.builder()
                        .username(ADMIN_USER_NAME)
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .status(User.Status.ACTIVE)
                        .lastChangePasswordDateTime(new Date(System.currentTimeMillis()))
                        .roles(roles)
                        .build();

                userRepository.save(admin);

                Seller seller = Seller.builder()
                        .name(ADMIN_USER_NAME)
                        .user(admin)
                        .address(addressRepository.findByName(ADMIN_ADDRESS))
                        .addressDetail(ADMIN_ADDRESS_DETAIL)
                        .build();

                sellerRepository.save(seller);

                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }

}
