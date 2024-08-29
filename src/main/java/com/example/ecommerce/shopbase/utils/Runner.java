package com.example.ecommerce.shopbase.utils;

import com.example.ecommerce.shopbase.service.product.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Runner {

    @Autowired
    InventoryService inventoryService;



}
