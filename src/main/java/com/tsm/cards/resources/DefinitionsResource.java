package com.tsm.cards.resources;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class DefinitionsResource {
	
	@Getter @Setter
	private String word;
	
	@Getter @Setter
	private Map<String, String> definitions;

}
