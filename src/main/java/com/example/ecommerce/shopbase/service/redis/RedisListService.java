package com.example.ecommerce.shopbase.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisListService {

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisListService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToList(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public void addAllToList(String key, List<Integer> values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    public List<Object> getEntireList(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return redisTemplate.opsForList().range(key, 0, size - 1);
    }

    public List<Object> getRangeFromList(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void updateInList(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    public void removeFromList(String key, Object value) {
        redisTemplate.opsForList().remove(key, 1, value);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    // Xóa toàn bộ danh sách
    public void deleteList(String key) {
        redisTemplate.delete(key);
    }
}