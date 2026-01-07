package com.example.summary_ext.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiClient {

        private final WebClient webClient = WebClient.builder().build();

        @Value("${gemini.api-key}")
        private String apiKey;

        @Value("${gemini.endpoint}")
        private String endpoint;

        @Value("${gemini.model}")
        private String model;

        public String summarize(String text, String systemPrompt) {
                String prompt = (systemPrompt != null && !systemPrompt.isEmpty())
                                ? systemPrompt + "\n\nNội dung cần xử lý: " + text
                                : """
                                                Tóm tắt đoạn văn sau thành 3–5 gạch đầu dòng.
                                                Giữ ý chính, văn phong học thuật, không suy diễn:
                                                """ + text;

                Map<String, Object> body = Map.of(
                                "contents", List.of(
                                                Map.of("parts", List.of(Map.of("text", prompt)))));

                try {
                        Map res = webClient.post()
                                        .uri(endpoint + "/" + model + ":generateContent?key=" + apiKey)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(body)
                                        .retrieve()
                                        .bodyToMono(Map.class)
                                        .block(Duration.ofSeconds(10));

                        return extractText(res);
                } catch (Exception e) {
                        return "Lỗi kết nối AI (DNS/Network): " + e.getMessage();
                }
        }

        private String extractText(Map<String, Object> res) {
                try {
                        if (res == null || !res.containsKey("candidates")) {
                                return "Lỗi: Google AI không trả về kết quả hợp lệ. Kiểm tra API Key hoặc Model.";
                        }

                        List<Map<String, Object>> candidates = (List<Map<String, Object>>) res.get("candidates");

                        if (candidates == null || candidates.isEmpty()) {
                                return "Lỗi: Không tìm thấy nội dung tóm tắt trong phản hồi.";
                        }

                        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");

                        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                        return parts.get(0).get("text").toString();
                } catch (Exception e) {
                        return "Lỗi phân giải kết quả AI: " + e.getMessage();
                }
        }
}
