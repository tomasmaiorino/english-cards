package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.cards.util.CardTypeTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.CardTypeTestBuilder.SMALL_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

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
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.util.CardTypeTestBuilder;
import com.tsm.cards.util.ClientTestBuilder;
import com.tsm.resource.CardResource;
import com.tsm.resource.CardTypeResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class CardsTypeControllerIT extends BaseTestIT {

	public static final String CARDS_TYPE_END_POINT = "/api/v1/cards-type";
	public static final String PUT_CARDS_TYPE_END_POINT = "/api/v1/cards-type/{id}";
	public static final String GET_CARDS_TYPE_END_POINT = "/api/v1/cards-type/{id}";
	public static final String GET_ALL_CARDS_TYPE_END_POINT = "/api/v1/cards-type";

	@LocalServerPort
	private int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void save_NoneHeaderGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(null);

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields();
		getTokenHeader();
		getTokenHeader().put(AUTHORIZATION_KEY, ClientTestBuilder.CLIENT_TOKEN);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.FORBIDDEN.value());
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is(REQUIRED_NAME), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	//

	@Test
	public void save_EmptyStatusGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().status("");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is(INVALID_STATUS), MESSAGE_FIELD_KEY, is(STATUS_KEY));
	}

	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().status("INV");

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The status must be either 'INACTIVE' or 'ACTIVE'."), MESSAGE_FIELD_KEY, is(STATUS_KEY));
	}

	@Test
	public void save_NullStatusGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().status(null);

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The status is required."), MESSAGE_FIELD_KEY, is(STATUS_KEY));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveCardType() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().imgUrl().assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(resource).contentType(ContentType.JSON).when()
				.post(CARDS_TYPE_END_POINT).then().statusCode(HttpStatus.CREATED.value()).body("name",
						is(resource.getName()), STATUS_KEY, is(resource.getStatus()), "imgUrl", is(resource.getImgUrl()))
				.body("id", notNullValue());
	}

	@Test
	// @Ignore
	public void save_DuplicatedNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		String name = resource.getName();
		CardTypeResource newResource = CardTypeResource.build().name(name).assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.post(CARDS_TYPE_END_POINT).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", is("Duplicated card type name."));
	}

	@Test
	public void update_NullNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name is required."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_SmallNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_ValidResourceGiven_ShouldSaveCardType() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields();

		// Do Test
		given().headers(getTokenHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.OK.value())
				.body("name", is(newResource.getName()), STATUS_KEY, is(newResource.getStatus()))
				.body("id", notNullValue());
	}

	@Test
	// @Ignore
	public void update_DuplicatedNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardTypeResource secondResource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		secondResource.setName(resource.getName());
		secondResource.setStatus(CardTypeTestBuilder.getStatus());

		// Do Test
		given().headers(getTokenHeader()).body(secondResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, secondResource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", is("Duplicated card type name."));
	}

	@Test
	public void findById_NotFoundCardTypeGiven_ShouldReturnError() {
		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CARDS_TYPE_END_POINT, RandomUtils.nextInt(100, 200)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body("message", is("Card type not found."));
	}

	@Test
	public void findById_FoundCardTypeGiven_ShouldReturnCardType() {
		// Set up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getTokenHeader()).create();
		CardResource.build().cardType(resource.getId()).assertFields().headers(getTokenHeader()).create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CARDS_TYPE_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.OK.value())
				.body("name", is(resource.getName()), "cards.size()", is(greaterThan(0)));
	}

	@Test
	public void findAll_FoundCardTypeGiven_ShouldReturnCards() {
		// Set up
		CardTypeResource.build().status(CardTypeStatus.ACTIVE.name()).assertFields().headers(getTokenHeader()).create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_ALL_CARDS_TYPE_END_POINT).then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", is(greaterThan(0)), "[0].status", is(CardTypeStatus.ACTIVE.name()));
	}

}
