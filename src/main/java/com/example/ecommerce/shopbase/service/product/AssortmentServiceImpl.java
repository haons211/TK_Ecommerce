package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.product.*;
import com.example.ecommerce.shopbase.dto.request.AssortmentRequest;
import com.example.ecommerce.shopbase.dto.request.AssortmentValueCombinationRequest;
import com.example.ecommerce.shopbase.dto.request.ValueCombinationRequest;
import com.example.ecommerce.shopbase.dto.request.ValueRequest;
import com.example.ecommerce.shopbase.entity.*;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AssortmentServiceImpl implements AssortmentService {

    @Autowired
    AssortmentRepository assortmentRepository;

    @Autowired
    ValueRepository valueRepository;

    @Autowired
    AssortmentValueRepository assortmentValueRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ValueCombinationRepository valueCombinationRepository;
    @Autowired
    private ProductAttributeRepository productAttributeRepository;
    @Autowired
    private ValueServiceImpl valueServiceImpl;




    @Override
    public List<AssortmentValueListDTO> getAssortmentsByProductId(int productId) {
        List<AssortmentValueListDTO> assortmentDTOS = new ArrayList<>();
        List<Assortment> assortments = assortmentRepository.findAllByProductId(productId);
        assortments.forEach(assortment -> {
            AssortmentValueListDTO a = new AssortmentValueListDTO(assortment,assortmentValueRepository.findAllByAssortmentID(assortment.getID()));
            assortmentDTOS.add(a);
        });
        return assortmentDTOS;
    }




    @Override
    @Transactional
    public void delete(int product_id) {

        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<Assortment> assortments = assortmentRepository.findAllByProductId(product_id);

        for (Assortment assortment : assortments) {
            List<Value> values = valueRepository.findAllByAssortment_ID(assortment.getID());
            valueServiceImpl.delete(assortment.getID());
        }

        assortmentRepository.deleteAllByProductId(product_id);


    }

    @Override
    public AssortmentValueCombinationRequest addAssortmentValueCombination(AssortmentValueCombinationRequest assortmentValueCombinationRequest) {
        Integer productId = assortmentValueCombinationRequest.getProductId();
        Product product = productRepository.findById(productId).orElse(null);

        List<List<Value>> valueList = new ArrayList<>();

        List<AssortmentRequest> assortmentList = assortmentValueCombinationRequest.getAssortmentList();

        //Them list assortment va list value cua tung assortment
        assortmentList.forEach(assortmentRequestItem -> {
            Assortment assortment1 = Assortment.builder()
                    .product(product)
                    .name(assortmentRequestItem.getName())
                    .build();
            Assortment assortment = assortmentRepository.save(assortment1);
            List<Value> list = new ArrayList<>();
            assortmentRequestItem.getValueRequestList().forEach(valueRequestItem -> {
                Value value1 = Value.builder()
                        .assortment(assortment)
                        .value(valueRequestItem.getValue())
                        .build();
                Value value = valueRepository.save(value1);
                list.add(value);
            });
            valueList.add(list);
        });

        AtomicInteger count= new AtomicInteger(0);
        List<ValueCombinationDTO> valueCombinationDTO = assortmentValueCombinationRequest.getValueCombination();

        if(valueList.size()==1){
            AtomicInteger minPrice = new AtomicInteger(Integer.MAX_VALUE);
            AtomicInteger totalQuantity = new AtomicInteger(0);
            valueList.getFirst().forEach(value -> {
                ValueCombination valueCombination=ValueCombination.builder()
                        .value1(value)
                        .price(valueCombinationDTO.get(count.get()).getPrice())
                        .quantity(valueCombinationDTO.get(count.get()).getQuantity())
                        .product(product)
                        .build();
                        valueCombinationRepository.save(valueCombination);
                        totalQuantity.addAndGet(valueCombinationDTO.get(count.get()).getQuantity());
                        count.getAndIncrement();
                        if(valueCombination.getPrice()< minPrice.get()) minPrice.set(valueCombination.getPrice());
                        product.setPrice(minPrice.get());

                        product.setQuantity(totalQuantity.get());

                        productRepository.save(product);
                    }
                    );
        }
        if(valueList.size()==2){
            AtomicInteger minPrice = new AtomicInteger(Integer.MAX_VALUE);
            AtomicInteger totalQuantity = new AtomicInteger(0);

            valueList.get(0).forEach(value1->{

                valueList.get(1).forEach(value2 -> {
                    ValueCombination valueCombination = ValueCombination.builder()
                            .value1(value1)
                            .value2(value2)
                            .price(valueCombinationDTO.get(count.get()).getPrice())
                            .quantity(valueCombinationDTO.get(count.get()).getQuantity())
                            .product(product)
                            .build();
                    valueCombinationRepository.save(valueCombination);
                    totalQuantity.addAndGet(valueCombinationDTO.get(count.get()).getQuantity());

                    count.getAndIncrement();


                    if(valueCombination.getPrice()< minPrice.get()) minPrice.set(valueCombination.getPrice());
                    product.setPrice(minPrice.get());
                    product.setQuantity(totalQuantity.get());
                    productRepository.save(product);
                });
            });
        }


        return assortmentValueCombinationRequest;
    }

    @Override
    @Transactional
    public AssortmentValueCombinationRequest updateAssortmentValueCombination(AssortmentValueCombinationRequest assortmentValueCombinationRequest) {
        delete(assortmentValueCombinationRequest.getProductId());
        addAssortmentValueCombination(assortmentValueCombinationRequest);

        return null;
    }



    @Override
    public List<AssortmentValueCombinationRequest> getAssortmentValueCombination(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<AssortmentValueCombinationRequest> result = new ArrayList<>();

        List<Assortment> assortments = assortmentRepository.findAllByProductId(product.getId());
        List<ValueCombination> valueCombinations = valueCombinationRepository.findAllByProductId(productId);

        for (Assortment assortment : assortments) {
            AssortmentValueCombinationRequest combinationRequest = new AssortmentValueCombinationRequest();
            combinationRequest.setProductId(product.getId());

            List<Value> values = valueRepository.findAllByAssortment_ID(assortment.getID());
            List<ValueRequest> valueRequests = values.stream()
                    .map(value -> {
                        ValueRequest valueRequest = new ValueRequest();
                        valueRequest.setValue(value.getValue());
                        return valueRequest;
                    })
                    .collect(Collectors.toList());

            AssortmentRequest assortmentRequest = new AssortmentRequest();
            assortmentRequest.setName(assortment.getName());
            assortmentRequest.setValueRequestList(valueRequests);

            List<ValueCombinationDTO> valueCombinationDTOs = valueCombinations.stream()
                    .filter(vc -> vc.getProduct().getId().equals(product.getId())
                            && vc.getValue1() != null && vc.getValue1().getAssortment() != null
                            && vc.getValue2() != null && vc.getValue2().getAssortment() != null
                            && vc.getValue1().getAssortment().getID().equals(assortment.getID())
                            && vc.getValue2().getAssortment().getID().equals(assortment.getID()))
                    .map(vc -> {
                        ValueCombinationDTO dto = new ValueCombinationDTO();
                        dto.setPrice(vc.getPrice());
                        dto.setQuantity(vc.getQuantity());
                        return dto;
                    })
                    .collect(Collectors.toList());

            combinationRequest.setAssortmentList(List.of(assortmentRequest));
            combinationRequest.setValueCombination(valueCombinationDTOs);
            result.add(combinationRequest);
        }

        return result;
    }

}