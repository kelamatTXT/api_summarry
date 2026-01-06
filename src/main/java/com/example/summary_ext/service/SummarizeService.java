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

    public SummarizeResponse summarize(String rawText) {
        String text = util.normalize(rawText);
        String key = "sum:v1:" + util.hash(text);

        String cached = cache.get(key);
        if (cached != null) {
            return new SummarizeResponse(cached, true);
        }

        String summary = gemini.summarize(text);
        cache.set(key, summary);

        return new SummarizeResponse(summary, false);
    }
}