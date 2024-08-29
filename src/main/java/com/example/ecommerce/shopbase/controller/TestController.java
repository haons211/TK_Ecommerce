package com.example.ecommerce.shopbase.controller;

import com.example.ecommerce.shopbase.service.redis.BaseRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    BaseRedisService baseRedisService;
    @GetMapping
    public String test() {
        return "Hello World";
    }
    @PostMapping("/redis")
    public void testRedis(){
        baseRedisService.set("one","hao");
    }

}
