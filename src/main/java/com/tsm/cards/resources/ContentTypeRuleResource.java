package com.tsm.cards.resources;

import lombok.Getter;
import lombok.Setter;

public class ContentTypeRuleResource implements BaseResource {

	@Getter
	@Setter
	private String attribute;

	@Getter
	@Setter
	private String rule;

}
