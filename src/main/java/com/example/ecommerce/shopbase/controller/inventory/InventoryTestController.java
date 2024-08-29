package com.example.ecommerce.shopbase.controller.inventory;

import com.example.ecommerce.shopbase.dto.inventory.CommandEventData;
import com.example.ecommerce.shopbase.dto.response.ApiResponse;
import com.example.ecommerce.shopbase.repository.ProductRepository;
import com.example.ecommerce.shopbase.service.inventory.InventoryCommandService;
import com.example.ecommerce.shopbase.service.product.ProductService;
import com.example.ecommerce.shopbase.service.product.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryTestController {

    @Autowired
    InventoryCommandService inventoryTestService;

    @Autowired
    ProductService productService;

    @PutMapping("/increse-quantity")
    public ApiResponse<String> increaseQuantity(@RequestBody CommandEventData data){
        inventoryTestService.receiveCommand(data);
        return ApiResponse.<String>builder()
            .result("success")
            .build();
    }

    @PutMapping("/test-inventory")
    public void updateInventory(@RequestBody CommandEventData data){
        inventoryTestService.receiveCommand(data);
    }

}
