package com.tsm.cards.parser;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.model.ContentType;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.model.ContentTypeRule;
import com.tsm.cards.resources.ContentTypeResource;
import com.tsm.cards.resources.ContentTypeRuleResource;
import com.tsm.cards.util.ContentTypeRuleTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ContentTypeParserTest {

	private static final Integer CONTENT_TYPE_RULE_ID = 1;

	private static final Integer CONTENT_TYPE_ID = 1;

	@InjectMocks
	private ContentTypeParser parser;

	@Mock
	private ContentTypeRuleParser ruleParser;

	@Mock
	private ContentParser contentParser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toModel_NullResourceGiven_ShouldThrowException() {
		// Set up
		ContentTypeResource resource = null;

		// Do test
		parser.toModel(resource);
	}

	@Test
	public void toModel_ValidResourceGiven_ShouldCreateContentTypeModel() {
		// Set up
		ContentTypeRuleResource rule = ContentTypeRuleTestBuilder.buildResource();
		Set<ContentTypeRuleResource> rules = new HashSet<>();
		rules.add(rule);
		ContentTypeResource resource = ContentTypeTestBuilder.buildResource();
		resource.setRules(rules);

		ContentTypeRule ruleModel = ContentTypeRuleTestBuilder.buildModel(CONTENT_TYPE_RULE_ID);
		Set<ContentTypeRule> rulesModel = new HashSet<>();
		rulesModel.add(ruleModel);

		// Expectation
		when(ruleParser.toModel(eq(rule), anyObject())).thenReturn(ruleModel);

		// Do test
		ContentType result = parser.toModel(resource);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", nullValue()), hasProperty("name", is(resource.getName())),
						hasProperty("imgUrl", is(resource.getImgUrl())), hasProperty("rules", is(rulesModel)),
						hasProperty("status", is(ContentTypeStatus.valueOf(resource.getStatus())))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void toResource_NullContentTypeGiven_ShouldThrowException() {
		// Set up
		ContentType client = null;

		// Do test
		parser.toResource(client);
	}

	@Test
	public void toResource_ValidContentTypeParserGiven_ShouldCreateResourceModel() {
		// Set up
		ContentType contentType = ContentTypeTestBuilder.buildModel(CONTENT_TYPE_ID);
		ContentTypeRule ruleModel = ContentTypeRuleTestBuilder.buildModel(CONTENT_TYPE_RULE_ID);
		contentType.addRule(ruleModel);

		ContentTypeRuleResource ruleResource = ContentTypeRuleTestBuilder.buildResource();

		// Expectations
		when(ruleParser.toResource(ruleModel)).thenReturn(ruleResource);

		// Do test
		ContentTypeResource result = parser.toResource(contentType);

		// Assertions
		assertNotNull(result);
		assertThat(result,
				allOf(hasProperty("id", is(contentType.getId())), hasProperty("name", is(contentType.getName())),
						hasProperty("imgUrl", is(contentType.getImgUrl())), hasProperty("rules", notNullValue()),
						hasProperty("status", is(contentType.getStatus().name()))));

	}

}
