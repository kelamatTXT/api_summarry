package com.example.summary_ext.controller;

import com.example.summary_ext.dto.SummarizeRequest;
import com.example.summary_ext.dto.SummarizeResponse;
import com.example.summary_ext.service.SummarizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SummarizeController {

    private final SummarizeService service;

    @PostMapping("/summarize")
    public SummarizeResponse summarize(
        @Valid @RequestBody SummarizeRequest req
    ) {
        return service.summarize(req.getText());
    }
}