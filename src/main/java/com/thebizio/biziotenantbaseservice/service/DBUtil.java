package com.thebizio.biziotenantbaseservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DBUtil {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    public String getDbUrl(String db) {
        String[] __ = dbUrl.split("/");
        __[__.length-1] = db;
        return String.join("/", __);
    }

}
