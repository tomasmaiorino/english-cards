package com.tsm.cards.security;

public class SecurityConstants {
	private SecurityConstants() {
	}

	public static final long EXPIRATION_TIME = 300_000; // 5 minutes
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/api/v1/users/auth";
	public static final String CREATE_MESSAGE_URL = "/api/v1/messages/**";
	public static final String CARDS_URL = "/api/v1/cards/**";
	public static final String DEFINITIONS_URL = "/api/v1/definitions/**";
	public static final String CARDS_TYPE_URL = "/api/v1/cards-type/**";
	public static final String CONTENT_TYPE_URL = "/api/v1/content-types/**";
	public static final String CONTENTS_URL = "/api/v1/contents/**";
	public static final String CARDS_TYPE_URL_POST = "/api/v1/cards-type";
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
	public static final String SIGNING_KEY = "4uerw@@3err";

}