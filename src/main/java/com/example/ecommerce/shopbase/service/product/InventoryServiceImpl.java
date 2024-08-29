package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.inventory.CombinationValueCommand;
import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.exception.AppException;
import com.example.ecommerce.shopbase.exception.ErrorCode;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.repository.ValueCombinationRepository;
import com.example.ecommerce.shopbase.repository.ValueRepository;
import com.example.ecommerce.shopbase.service.inventory.InventoryCommandService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private ValueCombinationRepository valueCombinationRepository;

    @Autowired
    InventoryCommandService inventoryCommandService;

    @Override
    @Transactional
    public void reduceProduct(int assortment_value_id,int quantity) {
         valueRepository.reductQuantity(assortment_value_id,quantity);
    }

    @Override
    @Transactional
    public void increaseProduct(int assortment_value_id,int quantity) {
         valueRepository.increaseQuantity(assortment_value_id,quantity);
    }

    @Override
    public Integer getPriceByValue(int id) {
//        Value value = valueRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));;
//        if (value != null) {
//            return value.getPrice();
//        }
        return null;
    }

    @Override
    public Integer getMaxPriceByValueIds(List<Integer> valueIds) {
//        if (valueIds.isEmpty()) {
//            return null;
//        }
//
//        List<Value> values = valueRepository.findAllById(valueIds);
//        if (values.isEmpty()) {
//            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
//        }
//
//        return values.stream()
//                .mapToInt(Value::getPrice)
//                .max()
//                .orElse(0);
        return 0;
    }

    @Override
    public boolean reduceProducts(List<Value> valueCombination, int quantity) {
        if(valueCombination.size()==1){
            ValueCombination valueCombination1 = valueCombinationRepository.findFirstByValue1(valueCombination.getFirst()).get();

            if(valueCombination1.getQuantity()>quantity){
                valueCombination1.setQuantity(valueCombination1.getQuantity()-quantity);
                valueCombinationRepository.save(valueCombination1);
                return true;
            }
            return false;
        }
        else if(valueCombination.size()==2){
            ValueCombination valueCombination1 = valueCombinationRepository.findFirstByValue1AndValue2(valueCombination.getFirst(),valueCombination.getFirst()).get();
            if(valueCombination1.getQuantity()>quantity){
                valueCombination1.setQuantity(valueCombination1.getQuantity()-quantity);
                valueCombinationRepository.save(valueCombination1);
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    public boolean increaseProducts(List<Value> valueCombination, int quantity) {
        if(valueCombination.size()==1){
            ValueCombination valueCombination1 = valueCombinationRepository.findFirstByValue1(valueCombination.getFirst()).get();
            if(valueCombination1.getQuantity()>quantity){
                valueCombination1.setQuantity(valueCombination1.getQuantity()+quantity);
                valueCombinationRepository.save(valueCombination1);
                return true;
            }
            return false;
        }
        else if(valueCombination.size()==2){
            ValueCombination valueCombination1 = valueCombinationRepository.findFirstByValue1AndValue2(valueCombination.getFirst(),valueCombination.get(1)).get();
            if(valueCombination1.getQuantity()>quantity){
                valueCombination1.setQuantity(valueCombination1.getQuantity()+quantity);
                valueCombinationRepository.save(valueCombination1);
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    public boolean reduceValueCombination(ValueCombination valueCombination, int quantity) {
        return true;
    }

    @Override
    public boolean increaseValueCombination(ValueCombination valueCombination, int quantity) {
        return true;
    }

    @Override
    public boolean reduceQuantity(String id, List<CombinationValueCommand> combinationValueCommands) {
        CommandEventData commandEventData = CommandEventData.builder()
                .id(id)
                .command(combinationValueCommands)
                .build();
        inventoryCommandService.receiveCommand(commandEventData);
        return true;
    }

    @Override
    public boolean increaseQuantity(String id, List<CombinationValueCommand> combinationValueCommands) {
        CommandEventData commandEventData = CommandEventData.builder()
                .id(id)
                .command(combinationValueCommands)
                .build();
        inventoryCommandService.receiveCommand(commandEventData);
        return true;
    }
}
