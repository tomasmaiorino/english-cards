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
import com.tsm.cards.model.CardType.CardTypeStatus;
import com.tsm.cards.util.CardTypeTestBuilder;
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
				.statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is("Missing admin header."));
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields();
		Map<String, String> header = new HashMap<>();
		header.put(ADMIN_TOKEN_HEADER, "qwert");

		// Do Test
		given().headers(header).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
				.statusCode(HttpStatus.FORBIDDEN.value()).body("message", is("Access not allowed."));
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name is required."), "[0].field", is("name"));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
	}

	//

	@Test
	public void save_EmptyStatusGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().status("");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
						is("The status must be either 'INACTIVE' or 'ACTIVE'."), "[0].field", is("status"));
	}

	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().status("INV");

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message",
						is("The status must be either 'INACTIVE' or 'ACTIVE'."), "[0].field", is("status"));
	}

	@Test
	public void save_NullStatusGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().status(null);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The status is required."), "[0].field", is("status"));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().imgUrl().assertFields();

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.CREATED.value()).body("name", is(resource.getName()), "status",
						is(resource.getStatus()), "imgUrl", is(resource.getImgUrl()))
				.body("id", notNullValue());
	}

	@Test
	// @Ignore
	public void save_DuplicatedNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		String name = resource.getName();
		CardTypeResource newResource = CardTypeResource.build().name(name).assertFields();

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT)
				.then().statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is("Duplicated card type name."));
	}

	@Test
	public void update_NullNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name(null);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name is required."), "[0].field", is("name"));
	}

	@Test
	public void update_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name("");

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
	}

	@Test
	public void update_SmallNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
	}

	@Test
	public void update_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
	}

	@Test
	public void update_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardTypeResource newResource = CardTypeResource.build().assertFields();

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_TYPE_END_POINT, resource.getId()).then().statusCode(HttpStatus.CREATED.value())
				.body("name", is(newResource.getName()), "status", is(newResource.getStatus()))
				.body("id", notNullValue());
	}

	@Test
	// @Ignore
	public void update_DuplicatedNameGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardTypeResource secondResource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		secondResource.setName(resource.getName());
		secondResource.setStatus(CardTypeTestBuilder.getStatus());

		// Do Test
		given().headers(getHeader()).body(secondResource).contentType(ContentType.JSON).when()
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
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(getHeader()).create();
		CardResource.build().cardType(resource.getId()).assertFields().headers(getHeader()).create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CARDS_TYPE_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.OK.value())
				.body("name", is(resource.getName()), "cards.size()", is(greaterThan(0)));
	}

	@Test
	public void findAll_FoundCardTypeGiven_ShouldReturnCards() {
		// Set up
		CardTypeResource.build().status(CardTypeStatus.ACTIVE.name()).assertFields().headers(getHeader()).create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_ALL_CARDS_TYPE_END_POINT).then()
				.statusCode(HttpStatus.OK.value())
				.body("size()", is(greaterThan(0)), "[0].status", is(CardTypeStatus.ACTIVE.name()));
	}

}
