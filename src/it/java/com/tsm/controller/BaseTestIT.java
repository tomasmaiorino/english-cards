package com.tsm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class BaseTestIT {

    public static Map<String, String> header = null;

    @Value(value = "${client.service.key}")
    private String clientServiceKey;

    public static final String ADMIN_TOKEN_HEADER = "AT";

    public Map<String, String> getHeader() {
        if (header == null) {
            header = new HashMap<>();
            header.put(ADMIN_TOKEN_HEADER, clientServiceKey);
        }
        return header;
    }
}
