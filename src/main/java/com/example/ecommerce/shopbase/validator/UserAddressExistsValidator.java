package com.example.ecommerce.shopbase.validator;

import com.example.ecommerce.shopbase.repository.UserAddressRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAddressExistsValidator implements ConstraintValidator<UserAddressExists, Integer> {

    UserAddressRepository userAddressRepository;

    @Override
    public boolean isValid(Integer userAddressId, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(userAddressId))
            return true;

        return userAddressRepository.existsById(userAddressId);
    }
}
