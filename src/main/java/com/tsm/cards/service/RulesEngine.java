package com.tsm.cards.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

@Component
@Slf4j
public class RulesEngine {

	public boolean isValid(final String rule, final String text) {
		Assert.hasText(rule, "The rule must not be empty!");
		Assert.hasText(text, "The text must not be empty!");
		log.debug("Checking rule [{}] against [{}].", rule, text);

		return Pattern.compile(rule).matcher(text).matches();
	}
}
