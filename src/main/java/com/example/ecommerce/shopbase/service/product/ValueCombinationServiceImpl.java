package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.ValueCombinationRequest;
import com.example.ecommerce.shopbase.entity.CartItem;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Product;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;

import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.repository.CartItemAttributeRepository;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValueCombinationServiceImpl implements ValueCombinationService {

    @Autowired
    private ValueCombinationRepository valueCombinationRepository;
    @Autowired
    private ProductRepository productRepository;

    CartItemAttributeRepository cartItemAttributeRepository;


   /* @Override
    public Integer getProductIdByValueId(int valueId1) {
        return 0;
    }

    @Override
    public Optional<ValueCombination> findByValue1_IdAndValue2_IdIsNull(int valueId1) {
        return Optional.empty();
    }


    @Override
    public Optional<ValueCombination> findByValue1_IdAndValue2_Id(int valueId1, int valueId2) {
        return Optional.empty();
    }

    @Override
    public Product findProductByValueId(int valueId1) {
        return valueCombinationRepository.findProductByValueId(valueId1);
    }*/


    @Override
    public Product findProductByValue(Value value) {
        ValueCombination valueCombination = valueCombinationRepository.findFirstByValue1(value)
                .orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        return valueCombination.getProduct();
    }

    @Override
    public Product findProductByValues(Value value1, Value value2) {
        ValueCombination valueCombination = valueCombinationRepository.findFirstByValue1AndValue2(value1,value2)
                .orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        return valueCombination.getProduct();
    }

    @Override
    public int getPricesByValueIdsWithBothValues(ValueCombinationRequest valueCombinationRequest) {
        int valueId1 = valueCombinationRequest.getValueIds().getFirst();
        int valueId2 = valueCombinationRequest.getValueIds().getLast();
        ValueCombination valueCombination = valueCombinationRepository.findById(valueId1).orElseThrow(() -> new AppException(ErrorCode.VALUECOMBINATION_NOT_EXISTED));
        if (valueCombinationRequest.getValueIds().size() == 1) {
            return valueCombinationRepository.getPricesByValue1(valueCombinationRequest.getValueIds().getFirst());
        } else {
            return valueCombinationRepository.getPricesByValueIdsWithBothValues(valueCombinationRequest.getValueIds());
        }
    }

    @Override
    public void deleteValueCombination(Integer valueId) {
        ValueCombination valueCombination=valueCombinationRepository.findById(valueId).get();
        valueCombinationRepository.delete(valueCombination);
    }

    @Override
    public List<ValueCombination> getAllValueCombinationsbyProductId(Integer productId) {
        Product product=productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        List<ValueCombination> valueCombinations=valueCombinationRepository.findAllByProductId(productId);
        return valueCombinations;
    }


    @Override
    public int getPricePerProduct(CartItem cartItem) {
        return cartItemAttributeRepository.getValueCombinationByCartItem(cartItem).getPrice();
    }

    @Override
    public int getQuantityPerProduct(CartItem cartItem) {
        return cartItemAttributeRepository.getValueCombinationByCartItem(cartItem).getQuantity();
    }
}