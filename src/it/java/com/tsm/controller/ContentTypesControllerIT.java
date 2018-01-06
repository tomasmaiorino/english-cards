package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.cards.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.ClientTestBuilder.SMALL_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

import java.util.HashMap;
import java.util.Map;

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

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.EnglishCardsApplication;
import com.tsm.cards.model.Content.ContentStatus;
import com.tsm.cards.model.ContentType.ContentTypeStatus;
import com.tsm.cards.util.ContentTypeTestBuilder;
import com.tsm.resource.ContentResource;
import com.tsm.resource.ContentTypeResource;

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
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_FIELD, is("Missing admin header."));
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields();
		Map<String, String> header = new HashMap<>();
		header.put(ADMIN_TOKEN_HEADER, "qwert");

		// Do Test
		given().headers(header).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_TYPES_END_POINT)
				.then().statusCode(HttpStatus.FORBIDDEN.value()).body(MESSAGE_FIELD, is("Access not allowed."));
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_NAME), "[0].field", is("name"));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is("name"));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is("name"));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is("name"));
	}

	//

	@Test
	public void save_EmptyStatusGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().status("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_STATUS), "[0].field", is("status"));
	}

	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().status("INV");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_STATUS), "[0].field", is("status"));
	}

	@Test
	public void save_NullStatusGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().status(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_STATUS), "[0].field", is("status"));
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
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.post(SAVE_CONTENTS_TYPES_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_FIELD, is("Duplicated content type name."));
	}

	@Test
	public void update_NullNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_NAME), "[0].field", is("name"));
	}

	@Test
	public void update_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is("name"));
	}

	@Test
	public void update_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is("name"));
	}

	@Test
	public void update_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().assertFields().headers(getHeader()).create();
		ContentTypeResource newResource = ContentTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_TYPES_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is("name"));
	}

	@Test
	public void update_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		ContentTypeResource resource = ContentTypeResource.build().rules(3).assertFields().headers(getHeader())
				.create();
		ContentTypeResource newResource = ContentTypeResource.build().rules(2).assertFields();

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
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
		given().headers(getHeader()).body(secondResource).contentType(ContentType.JSON).when()
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
	public void findAll_FoundContentTypeGiven_ShouldReturnCards() {
		// Set up
		ContentTypeResource.build().status(ContentTypeStatus.ACTIVE.name()).assertFields().headers(getHeader())
				.create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_ALL_CONTENTS_TYPES_END_POINT).then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", is(greaterThan(0)), "[0].status", is(ContentTypeStatus.ACTIVE.name()));
	}

}
