package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.inventory.CombinationValueCommand;
import com.example.ecommerce.shopbase.entity.Value;
import com.example.ecommerce.shopbase.entity.ValueCombination;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService
{

    public void reduceProduct(int assortment_value_id,int quantity);
    public void increaseProduct(int assortment_value_id,int quantity);
    Integer getPriceByValue(int id);
    Integer getMaxPriceByValueIds(List<Integer> valueIds);
    boolean reduceProducts(List<Value> valueCombination, int quantity);
    boolean increaseProducts(List<Value> valueCombination,int quantity);


    boolean reduceValueCombination(ValueCombination valueCombination, int quantity);
    boolean increaseValueCombination(ValueCombination valueCombination,int quantity);

    boolean reduceQuantity(String id, List<CombinationValueCommand> combinationValueCommands);
    boolean increaseQuantity(String id, List<CombinationValueCommand> combinationValueCommands);

}
