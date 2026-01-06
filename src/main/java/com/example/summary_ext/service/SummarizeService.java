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

        // 1. Kiểm tra cache chính xác (Perfect Match)
        String cached = cache.get(key);
        if (cached != null) {
            return new SummarizeResponse(cached, true);
        }

        // 2. Kiểm tra xem text này có phải là con của một đoạn văn nào đã từng tóm tắt
        // không
        // (Giải quyết vấn đề bôi đen câu B nằm trong đoạn A)
        java.util.List<String> recentTexts = cache.getRecentTexts();
        for (String parentText : recentTexts) {
            if (parentText.contains(text)) {
                String parentKey = "sum:v1:" + util.hash(parentText);
                String parentSummary = cache.get(parentKey);
                if (parentSummary != null) {
                    // Trả về kèm ghi chú đây là tóm tắt từ ngữ cảnh lớn hơn
                    return new SummarizeResponse(
                            parentSummary + "\n\n(Lưu ý: Đây là tóm tắt từ đoạn văn chứa nội dung này)", true);
                }
            }
        }

        // 3. Nếu hoàn toàn mới, gọi AI
        String summary = gemini.summarize(text);

        // 4. Lưu cache và đăng ký vào danh sách "Recent" nếu text đủ dài
        cache.set(key, summary);
        if (text.length() > 100) {
            cache.addToRecent(text);
        }

        return new SummarizeResponse(summary, false);
    }
}