package com.example.ecommerce.shopbase.service.product;

import com.example.ecommerce.shopbase.dto.product.AssortmentValueListDTO;
import com.example.ecommerce.shopbase.dto.request.AssortmentValueCombinationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssortmentService {

    List<AssortmentValueListDTO> getAssortmentsByProductId(int productId);

    void delete(int id);

    AssortmentValueCombinationRequest addAssortmentValueCombination(AssortmentValueCombinationRequest assortmentValueCombinationRequest);
    AssortmentValueCombinationRequest updateAssortmentValueCombination(AssortmentValueCombinationRequest assortmentValueCombinationRequest);

    List<AssortmentValueCombinationRequest> getAssortmentValueCombination(int productId);

}
