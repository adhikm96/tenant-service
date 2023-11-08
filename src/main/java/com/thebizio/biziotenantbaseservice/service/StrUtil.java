package com.thebizio.biziotenantbaseservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StrUtil {
    public String getRandomDBPassword() {
        return UUID.randomUUID().toString();
    }
}
