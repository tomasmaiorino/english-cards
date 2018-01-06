package com.tsm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class BaseTestIT {

	public static Map<String, String> header = null;
	protected static final String REQUIRED_NAME = "The name is required.";
	protected static final String INVALID_STATUS = "The status must be either 'INACTIVE' or 'ACTIVE'.";
	protected static final String REQUIRED_STATUS = "The status is required.";
	protected static final String MESSAGE_FIELD = "message";
	protected static final String STATUS_KEY = "status";

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
