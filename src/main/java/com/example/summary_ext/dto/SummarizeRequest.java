package com.example.summary_ext.dto;

import lombok.Data;

@Data
public class SummarizeRequest {
    private String text;
    private String systemPrompt;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
}