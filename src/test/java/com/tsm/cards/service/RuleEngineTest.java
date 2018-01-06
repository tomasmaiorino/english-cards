package com.tsm.cards.service;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class RuleEngineTest {

	private RulesEngine rulesEngine = new RulesEngine();

	@Test(expected = IllegalArgumentException.class)
	public void isValid_NullRuleGiven_ShouldThrowException() {

		// Set up
		String rule = null;
		String text = random(100, true, true);

		// Do test
		rulesEngine.isValid(rule, text);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isValid_EmptyRuleGiven_ShouldThrowException() {

		// Set up
		String rule = "";
		String text = random(100, true, true);

		// Do test
		rulesEngine.isValid(rule, text);
	}

	//

	@Test(expected = IllegalArgumentException.class)
	public void isValid_NullTextGiven_ShouldThrowException() {

		// Set up
		String rule = "[\\D]";
		String text = null;

		// Do test
		rulesEngine.isValid(rule, text);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isValid_EmptyTextGiven_ShouldThrowException() {

		// Set up
		String rule = "[\\D]";
		String text = "";

		// Do test
		rulesEngine.isValid(rule, text);
	}

	@Test
	public void isValid_ValidRuleTextGiven_ShouldValidate() {
		// Set up
		String rule = "[a-z]+";
		String text = "test";

		// Do test
		boolean result = rulesEngine.isValid(rule, text);

		// Assertions
		assertThat(result, is(true));
	}

	@Test
	public void isValid_ValidRuleTextGiven_ShouldReturnFalse() {
		// Set up
		String rule = "[a-z]+";
		String text = "123";
   
		// Do test
		boolean result = rulesEngine.isValid(rule, text);

		assertThat(result, is(false));
	}
}
