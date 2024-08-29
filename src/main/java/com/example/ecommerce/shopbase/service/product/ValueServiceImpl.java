package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.ValueRequest;
import com.example.ecommerce.shopbase.entity.Assortment;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.AssortmentRepository;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.example.ecommerce.shopbase.repository.ValueRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValueServiceImpl implements ValueService {

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private AssortmentRepository assortmentRepository;
    @Autowired
    private ValueCombinationRepository valueCombinationRepository;

    @Override
    public Value addValue(Value value) {
        return null;
    }

    @Override
    public Value addValue( ValueRequest valuerequest) {
        Value value1 = Value.builder()
                .value(valuerequest.getValue())
                .assortment(assortmentRepository.findById(valuerequest.getAssortmentId()).get())
                .build();
        return valueRepository.save(value1);
    }

    @Override
    public List<Value> getAllValue() {
        return List.of();
    }


    @Override
    @Transactional
    public void delete(int assortment_id) {

        Assortment assortment = assortmentRepository.findById(assortment_id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSORTMENT_NOT_EXISTED));

        List<Value> values = valueRepository.findAllByAssortment_ID(assortment_id);

        // Xóa các ValueCombination liên quan
        List<Integer> valueIds = values.stream()
                .map(Value::getId)
                .collect(Collectors.toList());

        List<Value> l = valueRepository.findAllById(valueIds);
                l.forEach(value -> {
            List<ValueCombination>valueCombinationList= valueCombinationRepository.findValueCombinationByValue1OrValue2(value, value);
            valueCombinationRepository.deleteAll(valueCombinationList);
        });
        valueRepository.deleteAllById(valueIds);
    }


    @Override
    public Integer getProductIdByValueId(Integer id) {
        return 0;
    }

    public Value updateValue(int id, ValueRequest updatedValueRequest) {
        Value existingValue = valueRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        existingValue.setValue(updatedValueRequest.getValue());


        Assortment assortment = assortmentRepository.findById(updatedValueRequest.getAssortmentId())
                .orElseThrow(() -> new AppException(ErrorCode.ASSORTMENT_NOT_EXISTED));
        existingValue.setAssortment(assortment);

        return valueRepository.save(existingValue);
    }


}