package com.example.ecommerce.shopbase.validator;

import com.example.ecommerce.shopbase.repository.AddressRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressIdExistsValidator implements ConstraintValidator<AddressIdExists, Integer> {

    AddressRepository addressRepository;

    @Override
    public boolean isValid(Integer addressId, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(addressId))
            return true;

        return addressRepository.existsById(addressId);
    }
}
