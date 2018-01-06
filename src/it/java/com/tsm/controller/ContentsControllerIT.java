package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

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
import com.tsm.cards.util.ContentTestBuilder;
import com.tsm.resource.ContentResource;
import com.tsm.resource.ContentTypeResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class ContentsControllerIT extends BaseTestIT {

	private static final String CONTENT_ASSERT_KEY = "content";
	private static final String NAME_ASSERT_KEY = "name";
	public static final String SAVE_CONTENTS_END_POINT = "/api/v1/contents";
	public static final String PUT_CONTENTS_END_POINT = "/api/v1/contents/{id}";
	public static final String DELETE_CONTENTS_END_POINT = "/api/v1/contents/{id}";
	public static final String GET_CONTENTS_END_POINT = "/api/v1/contents/{id}";
	public static final String GET_ALL_CONTENTS_END_POINT = "/api/v1/contents";
	private static final String INVALID_NAME = "The name must be between 2 and 30 characters.";
	private static final String INVALID_CONTENT = "The content must be between 20 and 3000 characters.";
	private static final String REQUIRED_CONTENT = "The content is required.";

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void save_NoneHeaderGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().name(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_FIELD, is("Missing admin header."));
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields();
		Map<String, String> header = new HashMap<>();
		header.put(ADMIN_TOKEN_HEADER, "qwert");

		// Do Test
		given().headers(header).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT).then()
				.statusCode(HttpStatus.FORBIDDEN.value()).body(MESSAGE_FIELD, is("Access not allowed."));
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields()
				.name(ContentTestBuilder.getSmallName());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).name(ContentTestBuilder.getLargeName())
				.assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	//

	@Test
	public void save_EmptyStatusGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().status("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_STATUS), "[0].field", is(STATUS_KEY));
	}

	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields()
				.status(ContentTestBuilder.getInvalidEmail());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_STATUS), "[0].field", is(STATUS_KEY));
	}

	@Test
	public void save_NullStatusGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().status(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_STATUS), "[0].field", is(STATUS_KEY));
	}

	//

	@Test
	public void save_NullContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().content(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void save_EmptyContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().content("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void save_SmallContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields()
				.content(ContentTestBuilder.getSmallContent());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void save_LargeContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields()
				.content(ContentTestBuilder.getLargeContent());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void save_NotFoundContentTypeGiven_ShouldReturnError() {
		// Set Up
		ContentTypeResource contentType = ContentTypeResource.build();
		contentType.setId(RandomUtils.nextInt(100, 200));
		ContentResource resource = ContentResource.build().headers(getHeader()).contentType(contentType.getId())
				.assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is("Content type not found."));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(SAVE_CONTENTS_END_POINT)
				.then().statusCode(HttpStatus.CREATED.value()).body("name", is(resource.getName()), "status",
						is(resource.getStatus()), "imgUrl", is(resource.getImgUrl()), CONTENT_ASSERT_KEY,
						is(resource.getContent()), "isHtml", is(false), "id", notNullValue());
	}

	//

	@Test
	public void findById_NotFoundContentTypeGiven_ShouldReturnError() {
		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CONTENTS_END_POINT, RandomUtils.nextInt(100, 200)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("The content was not found."));
	}

	@Test
	public void findById_FoundContentTypeGiven_ShouldReturnContentType() {
		// Set up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CONTENTS_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.OK.value()).body("name", is(resource.getName()), "status",
						is(resource.getStatus()), "imgUrl", is(resource.getImgUrl()), CONTENT_ASSERT_KEY,
						is(resource.getContent()), "isHtml", is(false), "id", notNullValue());
	}

	//

	@Test
	public void update_NullNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	@Test
	public void update_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	@Test
	public void update_SmallNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().name(ContentTestBuilder.getSmallName());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	@Test
	public void update_LargeNameGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).name(ContentTestBuilder.getLargeName()).assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_NAME), "[0].field", is(NAME_ASSERT_KEY));
	}

	//

	@Test
	public void update_EmptyStatusGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().status("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_STATUS), "[0].field", is(STATUS_KEY));
	}

	@Test
	public void update_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields()
				.status(ContentTestBuilder.getInvalidEmail());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_STATUS), "[0].field", is(STATUS_KEY));
	}

	@Test
	public void update_NullStatusGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().status(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_STATUS), "[0].field", is(STATUS_KEY));
	}

	//

	@Test
	public void update_NullContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().content(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(REQUIRED_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void update_EmptyContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields().content("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void update_SmallContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields()
				.content(ContentTestBuilder.getSmallContent());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void update_LargeContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields()
				.content(ContentTestBuilder.getLargeContent());

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is(INVALID_CONTENT), "[0].field", is(CONTENT_ASSERT_KEY));
	}

	@Test
	public void update_NotFoundContentGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, RandomUtils.nextInt(100, 2000)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("The content was not found."));
	}

	@Test
	public void update_NotFoundContentTypeGiven_ShouldReturnError() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		ContentTypeResource contentType = ContentTypeResource.build();
		contentType.setId(RandomUtils.nextInt(100, 200));
		resource = ContentResource.build().headers(getHeader()).contentType(contentType.getId()).assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", is("Content type not found."));
	}

	@Test
	public void update_ValidResourceGiven_ShouldUpdateClient() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();
		resource = ContentResource.build().headers(getHeader()).assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when()
				.put(PUT_CONTENTS_END_POINT, contentId).then().statusCode(HttpStatus.OK.value()).body("name",
						is(resource.getName()), "status", is(resource.getStatus()), "imgUrl", is(resource.getImgUrl()),
						CONTENT_ASSERT_KEY, is(resource.getContent()), "isHtml", is(false), "id", notNullValue());
	}

	@Test
	public void delete_NotFoundContentGiven_ShouldReturnError() {

		// Do Test
		given().headers(getHeader()).contentType(ContentType.JSON).when()
				.delete(DELETE_CONTENTS_END_POINT, RandomUtils.nextInt(100, 2000)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("The content was not found."));
	}

	@Test
	public void delete_ValidResourceGiven_ShouldUpdateClient() {
		// Set Up
		ContentResource resource = ContentResource.build().headers(getHeader()).assertFields().create();
		Integer contentId = resource.getId();

		// Do Test
		given().headers(getHeader()).contentType(ContentType.JSON).when().delete(DELETE_CONTENTS_END_POINT, contentId)
				.then().statusCode(HttpStatus.NO_CONTENT.value());

		given().contentType(ContentType.JSON).when().get(GET_CONTENTS_END_POINT, contentId).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body("message", is("The content was not found."));
	}

}
