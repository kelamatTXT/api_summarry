package com.example.summary_ext.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CacheService {

    private final StringRedisTemplate redis;

    public String get(String key) {
        try {
            return redis.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis error (get): {}", e.getMessage());
            return null;
        }
    }

    public void set(String key, String value) {
        try {
            redis.opsForValue().set(key, value, Duration.ofDays(30));
        } catch (Exception e) {
            log.error("Redis error (set): {}", e.getMessage());
        }
    }

    public void addToRecent(String text) {
        try {
            redis.opsForList().leftPush("recent_texts", text);
            redis.opsForList().trim("recent_texts", 0, 99);
        } catch (Exception e) {
            log.error("Redis error (addToRecent): {}", e.getMessage());
        }
    }

    public java.util.List<String> getRecentTexts() {
        try {
            return redis.opsForList().range("recent_texts", 0, -1);
        } catch (Exception e) {
            log.error("Redis error (getRecentTexts): {}", e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}
