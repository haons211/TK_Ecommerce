package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.request.ValueRequest;
import com.example.ecommerce.shopbase.entity.Value;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ValueService {
    Value addValue( Value value);
    Value addValue( ValueRequest value);
    List<Value> getAllValue();
    void delete(int assortmentId);
    Value updateValue(int id,ValueRequest value);

    //void delete(int id);
   /* Value updateValue(int id,Value value);*/
   //Value updateValue(int id,ValueRequest value);
    Integer getProductIdByValueId(Integer id);

}
