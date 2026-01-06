package com.example.summary_ext.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SummarizeRequest {
    @NotBlank
    @Size(max = 4000)
    private String text;
}