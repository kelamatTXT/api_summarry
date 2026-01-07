package com.example.summary_ext.service;

import com.example.summary_ext.dto.GeminiClient;
import com.example.summary_ext.dto.SummarizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummarizeService {

    private final CacheService cache;
    private final GeminiClient gemini;
    private final TextUtil util;

    public SummarizeResponse summarize(String rawText, String systemPrompt) {
        String text = util.normalize(rawText);
        String key = (systemPrompt != null ? util.hash(systemPrompt) : "default") + ":" + util.hash(text);

        // 1. Kiểm tra cache chính xác (Perfect Match)
        String cached = cache.get(key);
        if (cached != null) {
            return new SummarizeResponse(cached, true);
        }

        // 3. Nếu hoàn toàn mới, gọi AI
        String summary = gemini.summarize(text, systemPrompt);

        // 4. Lưu cache
        cache.set(key, summary);

        return new SummarizeResponse(summary, false);
    }
}