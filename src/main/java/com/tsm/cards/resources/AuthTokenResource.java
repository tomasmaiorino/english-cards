package com.tsm.cards.resources;

import lombok.Data;

@Data
public class AuthTokenResource {

	public AuthTokenResource(final String token) {
		this.token = token;
	}

	private String token;
	
	
}
