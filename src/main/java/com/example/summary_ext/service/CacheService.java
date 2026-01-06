package com.example.summary_ext.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final StringRedisTemplate redis;

    public String get(String key) {
        return redis.opsForValue().get(key);
    }

    public void set(String key, String value) {
        redis.opsForValue().set(key, value, Duration.ofDays(30));
    }

    public void addToRecent(String text) {
        // Lưu vào một Set hoặc List trong Redis để kiểm tra substring
        redis.opsForList().leftPush("recent_texts", text);
        redis.opsForList().trim("recent_texts", 0, 99); // Chỉ giữ 100 bản tin gần nhất
    }

    public java.util.List<String> getRecentTexts() {
        return redis.opsForList().range("recent_texts", 0, -1);
    }
}
