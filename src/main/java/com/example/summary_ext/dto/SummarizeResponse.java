package com.example.summary_ext.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummarizeResponse {
    private String summary;
    private boolean cached;
}