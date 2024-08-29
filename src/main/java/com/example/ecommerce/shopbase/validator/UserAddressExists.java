package com.example.ecommerce.shopbase.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserAddressExistsValidator.class)
public @interface UserAddressExists {
    String message() default "Address not exist!";

    Class<?> [] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
