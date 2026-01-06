package com.example.summary_ext.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class TextUtil {

    public String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    public String hash(String text) {
        return DigestUtils.sha256Hex(text);
    }
}