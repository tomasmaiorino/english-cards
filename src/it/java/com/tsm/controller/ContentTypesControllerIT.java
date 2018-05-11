package com.tsm.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.EnglishCardsApplication;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.util.ClientTestBuilder;
import com.tsm.cards.util.ContentTypeTestBuilder;
import com.tsm.resource.ContentResource;
import com.tsm.resource.ContentTypeResource;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.cards.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.ClientTestBuilder.SMALL_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class ContentTypesControllerIT extends BaseTestIT {

	public static final String SAVE_CONTENTS_TYPES_END_POINT = "/api/v1/content-types";
	public static final String PUT_CONTENTS_TYPES_END_POINT = "/api/v1/content-types/{id}";
	public static final String GET_CONTENTS_TYPES_END_POINT = "/api/v1/content-types/{id}";
	public static final String GET_ALL_CONTENTS_TYPES_END_POINT = "/api/v1/content-types";
	private static final String INVALID_NAME = "The name must be between 2 and 30 characters.";

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void save_NoneHeaderGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_TYPES_END_POINT).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields();
		getTokenHeader();
		getHeader().put(AUTHORIZATION_KEY, ClientTestBuilder.CLIENT_TOKEN);

		// Do Test
		given().headers(header).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_TYPES_END_POINT)
				.then().statusCode(HttpStatus.FORBIDDEN.value()).body(MESSAGE_FIELD, is("Access Denied"));
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(REQUIRED_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	//

	@Test
	public void save_EmptyStatusGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().status("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_STATUS), MESSAGE_FIELD_KEY, is("status"));
	}

	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().status("INV");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_STATUS), MESSAGE_FIELD_KEY, is("status"));
	}

	@Test
	public void save_NullStatusGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().status(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(REQUIRED_STATUS), MESSAGE_FIELD_KEY, is("status"));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().imgUrl().rules(2).assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.CREATED.value())
				.body("name", is(resource.getName()), "rules.size()", is(resource.getRules().size()), "status",
						is(resource.getStatus()), "imgUrl", is(resource.getImgUrl()))
				.body("id", notNullValue());
	}

	@Test
	// @Ignore
	public void save_DuplicatedNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		String name = resource.getName();
		ContentTypeResource newResource = ContentTypeResource.build().name(name).assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_FIELD, is("Duplicated content type name."));
	}

	@Test
	public void update_NullNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(REQUIRED_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(INVALID_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().rules(3).assertFields().headers(getHeader())
				.create();
		ContentTypeResource newResource = ContentTypeResource.build().rules(2).assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.OK.value())
				.body("name", is(newResource.getName()), "status", is(newResource.getStatus()), "rules.size()",
						is(newResource.getRules().size()), "id", notNullValue());
	}

	@Test
	// @Ignore
	public void update_DuplicatedNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource secondResource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		secondResource.setName(resource.getName());
		secondResource.setStatus(ContentTypeTestBuilder.getStatus());

		// Do Test
		given().headers(getTokenHeader()).body(secondResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, secondResource.getId()).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_FIELD, is("Duplicated content type name."));
	}

	@Test
	public void findById_NotFoundContentTypeGiven_ShouldReturnError() {
		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CONTENTS_TYPES_END_POINT, RandomUtils.nextInt(100, 200))
				.then().statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("Content type not found."));
	}

	@Test
	public void findById_FoundContentTypeGiven_ShouldReturnContentType() {
		// Set up
		ContentTypeResource resource = ContentTypeResource.build().imgUrl().assertFields().headers(getHeader())
				.create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CONTENTS_TYPES_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.OK.value())
				.body("name", is(resource.getName()), "imgUrl", is(resource.getImgUrl()));
	}

	@Test
	public void findById_FoundContentTypeWithContentGiven_ShouldReturnContentType() {
		// Set up
		ContentTypeResource resource = ContentTypeResource.build().imgUrl().assertFields().headers(getHeader())
				.create();

		ContentResource.build().headers(getHeader()).contentType(resource.getId()).status(ContentStatus.ACTIVE.name())
				.assertFields().create();

		ContentResource.build().headers(getHeader()).contentType(resource.getId()).status(ContentStatus.INACTIVE.name())
				.assertFields().create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CONTENTS_TYPES_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.OK.value())
				.body("name", is(resource.getName()), "contents.size()", is(1), "imgUrl", is(resource.getImgUrl()));
	}

	@Test
	public void findAll_FoundContentTypeGiven_ShouldReturnContentType() {
		// Set up
		ContentTypeResource.build().status(ContentTypeStatus.ACTIVE.name()).assertFields().headers(getHeader())
				.create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_ALL_CONTENTS_TYPES_END_POINT).then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", is(greaterThan(0)), "[0].status", is(ContentTypeStatus.ACTIVE.name()));
	}

	@Test
	public void findAll_FoundContentTypeWithNameQueryGiven_ShouldReturnContentType() {
		// Set up
		Map<String, String> header = getTokenHeader();
		ContentTypeResource contentType = ContentTypeResource.build().status(ContentTypeStatus.ACTIVE.name())
				.assertFields().headers(header).create();

		ContentResource.build().headers(header).contentType(contentType.getId()).status(ContentStatus.ACTIVE.name())
				.assertFields().create();

		// Do Test
		given().contentType(ContentType.JSON).when()
				.get(GET_ALL_CONTENTS_TYPES_END_POINT + "?q=" + contentType.getName()).then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", is(greaterThan(0)), "[0].status", is(ContentTypeStatus.ACTIVE.name()));
	}

	@Test
	public void findAll_NotFoundContentTypeWithNameQueryGiven_ShouldReturnError() {

		// Do Test
		given().contentType(ContentType.JSON).when()
				.get(GET_ALL_CONTENTS_TYPES_END_POINT + "?q=" + ContentTypeTestBuilder.getName()).then()
				.statusCode(HttpStatus.NOT_FOUND.value());

	}

}
