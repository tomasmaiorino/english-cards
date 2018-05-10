package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.cards.util.CardTestBuilder.LARGE_IMG_URL;
import static com.tsm.cards.util.CardTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.CardTestBuilder.SMALL_IMG_URL;
import static com.tsm.cards.util.CardTestBuilder.SMALL_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

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
import com.tsm.cards.util.ClientTestBuilder;
import com.tsm.resource.CardResource;
import com.tsm.resource.CardTypeResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class CardsControllerIT extends BaseTestIT {

	public static final String CARDS_END_POINT = "/api/v1/cards";
	public static final String PUT_CARDS_END_POINT = "/api/v1/cards/{id}";
	public static final String GET_CARDS_END_POINT = "/api/v1/cards/{id}";
	public static final String DELETE_CARDS_END_POINT = "/api/v1/cards/{id}";

	@LocalServerPort
	private int port;

	private Map<String, String> localHeader;

	@Before
	public void setUp() {
		RestAssured.port = port;
		localHeader = getTokenHeader();
	}

	private CardResource buildCardResource() {
		CardTypeResource cardTypeResource = CardTypeResource.build().headers(localHeader).assertFields().create();
		return CardResource.build().cardType(cardTypeResource.getId());
	}

	@Test
	public void save_NoneHeaderGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields();

		// Do Test
		given().body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void save_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		CardTypeResource cardTypeResource = CardTypeResource.build().headers(localHeader).assertFields().create();
		CardResource resource = CardResource.build().cardType(cardTypeResource.getId()).assertFields();
		localHeader.put(AUTHORIZATION_KEY, ClientTestBuilder.CLIENT_TOKEN);

		// Do Test
		given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.FORBIDDEN.value()).body(MESSAGE_FIELD, is("Access Denied"));
	}

	//

	@Test
	public void save_NullNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).name(null);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card name is required."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().name("");

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The card name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_SmallNameGiven_ShouldReturnError() {
		// Set Up
		Map<String, String> tokenHeader = localHeader;
		CardResource resource = buildCardResource().assertFields().headers(tokenHeader).name(SMALL_NAME);

		// Do Test
		given().headers(tokenHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The card name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void save_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The card name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY, is("name"));
	}

	//
	@Test
	public void save_NullImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().imgUrl(null);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The img url is required."), MESSAGE_FIELD_KEY, is("imgUrl"));
	}

	@Test
	public void save_EmptyImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().imgUrl("");

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The url img must be between 2 and 150 characters."), MESSAGE_FIELD_KEY, is("imgUrl"));
	}

	@Test
	public void save_SmallImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().imgUrl(SMALL_IMG_URL);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The url img must be between 2 and 150 characters."), MESSAGE_FIELD_KEY, is("imgUrl"));
	}

	@Test
	public void save_LargeImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().imgUrl(LARGE_IMG_URL);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The url img must be between 2 and 150 characters."), MESSAGE_FIELD_KEY, is("imgUrl"));
	}

	//
	@Test
	public void save_NullCardTypeGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = CardResource.build().headers(localHeader).assertFields().cardType(null);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card type is required."), MESSAGE_FIELD_KEY, is("cardType"));
	}

	@Test
	public void save_InvalidCardTypeGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = CardResource.build().headers(localHeader).assertFields().cardType(0);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_CHECK_KEY,
						is("The card type must be greater than 0."), MESSAGE_FIELD_KEY, is("cardType"));
	}

	@Test
	public void save_NotFoundCardTypeGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = CardResource.build().headers(localHeader).assertFields()
				.cardType(RandomUtils.nextInt(100, 1000));

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("Card type not found."));
	}

	//
	@Test
	public void save_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().status(INVALID_STATUS);

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The status must be either 'INACTIVE' or 'ACTIVE'."));
	}

	@Test
	// @Ignore
	public void save_DuplicatedCardGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		String imgUrl = resource.getImgUrl();
		CardResource newResource = buildCardResource().imgUrl(imgUrl).assertFields();

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.BAD_REQUEST.value()).body(MESSAGE_FIELD, is("Duplicated card."));
	}

	@Test
	public void save_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		CardResource resource = buildCardResource().assertFields();

		// Do Test
		given().headers(localHeader).body(resource).contentType(ContentType.JSON).when().post(CARDS_END_POINT).then()
				.statusCode(HttpStatus.CREATED.value()).body("name", is(resource.getName()))
				.body("cardType", is(resource.getCardType())).body("imgUrl", is(resource.getImgUrl()))
				.body("status", is(resource.getStatus())).body("id", notNullValue());
	}

	@Test
	public void update_NoneHeaderGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().name(null);

		// Do Test
		given().body(newResource).contentType(ContentType.JSON).when().put(PUT_CARDS_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void update_InvalidHeaderGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields();
		getHeader().put(AUTHORIZATION_KEY, ClientTestBuilder.CLIENT_TOKEN);

		// Do Test
		given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.FORBIDDEN.value())
				.body(MESSAGE_FIELD, is("Access Denied"));
	}

	//

	@Test
	public void update_NullNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().name(null);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card name is required."), MESSAGE_FIELD_KEY, is("name"));
	}

	@Test
	public void update_EmptyNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().name("");

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY,
						is("name"));
	}

	@Test
	public void update_SmallNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().name(SMALL_NAME);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY,
						is("name"));
	}

	@Test
	public void update_LargeNameGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().name(LARGE_NAME);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card name must be between 2 and 30 characters."), MESSAGE_FIELD_KEY,
						is("name"));
	}

	//
	@Test
	public void update_NullImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().imgUrl(null);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The img url is required."), MESSAGE_FIELD_KEY, is("imgUrl"));
	}

	@Test
	public void update_EmptyImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().imgUrl("");

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The url img must be between 2 and 150 characters."), MESSAGE_FIELD_KEY,
						is("imgUrl"));
	}

	@Test
	public void update_SmallImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().imgUrl(SMALL_IMG_URL);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The url img must be between 2 and 150 characters."), MESSAGE_FIELD_KEY,
						is("imgUrl"));
	}

	@Test
	public void update_LargeImgUrlGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().imgUrl(LARGE_IMG_URL);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The url img must be between 2 and 150 characters."), MESSAGE_FIELD_KEY,
						is("imgUrl"));
	}

	//
	@Test
	public void update_NullCardTypeGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().cardType(null);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card type is required."), MESSAGE_FIELD_KEY, is("cardType"));
	}

	@Test
	public void update_InvalidCardTypeGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().cardType(0);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The card type must be greater than 0."), MESSAGE_FIELD_KEY,
						is("cardType"));
	}

	@Test
	public void update_NotFoundCardTypeGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields()
				.cardType(RandomUtils.nextInt(100, 1000));

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.NOT_FOUND.value())
				.body(MESSAGE_FIELD, is("Card type not found."));
	}

	//
	@Test
	public void update_InvalidStatusGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields().status(INVALID_STATUS);

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_CHECK_KEY, is("The status must be either 'INACTIVE' or 'ACTIVE'."));
	}

	@Test
	// @Ignore
	public void update_DuplicatedCardGiven_ShouldReturnError() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource resource2 = buildCardResource().assertFields().headers(localHeader).create();
		String imgUrl = resource.getImgUrl();
		CardResource newResource = CardResource.build().headers(localHeader).imgUrl(imgUrl).assertFields();

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource2.getId()).then().statusCode(HttpStatus.BAD_REQUEST.value())
				.body(MESSAGE_FIELD, is("Duplicated card."));
	}

	@Test
	public void update_ValidResourceGiven_ShouldSaveClient() {
		// Set Up
		CardResource resource = buildCardResource().assertFields().headers(localHeader).create();
		CardResource newResource = CardResource.build().headers(localHeader).assertFields();

		// Do Test
		given().headers(localHeader).body(newResource).contentType(ContentType.JSON).when()
				.put(PUT_CARDS_END_POINT, resource.getId()).then().statusCode(HttpStatus.OK.value())
				.body("name", is(newResource.getName())).body("cardType", is(newResource.getCardType()))
				.body("imgUrl", is(newResource.getImgUrl())).body("status", is(newResource.getStatus()))
				.body("id", is(resource.getId()));
	}

	@Test
	public void findById_NotFoundCardGiven_ShouldReturnError() {
		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CARDS_END_POINT, RandomUtils.nextInt(100, 200)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("Card not found."));
	}

	@Test
	public void findById_FoundCardGiven_ShouldReturnCardType() {
		// Set up
		CardResource resource = CardResource.build().headers(localHeader).assertFields().create();

		// Do Test
		given().contentType(ContentType.JSON).when().get(GET_CARDS_END_POINT, resource.getId()).then()
				.statusCode(HttpStatus.OK.value()).body("name", is(resource.getName()))
				.body("cardType", is(resource.getCardType())).body("imgUrl", is(resource.getImgUrl()))
				.body("status", is(resource.getStatus())).body("id", is(resource.getId()));
	}

	@Test
	public void delete_NotFoundCardGiven_ShouldReturnError() {
		// Do Test
		given().headers(localHeader).contentType(ContentType.JSON).when()
				.delete(DELETE_CARDS_END_POINT, RandomUtils.nextInt(100, 200)).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("Card not found."));
	}

	@Test
	public void delete_FoundCardGiven_ShouldDelete() {
		// Set up
		CardTypeResource resource = CardTypeResource.build().assertFields().headers(localHeader).create();
		CardResource cardResource = CardResource.build().cardType(resource.getId()).assertFields().headers(localHeader)
				.create();

		// Do Test
		given().headers(localHeader).contentType(ContentType.JSON).when()
				.delete(DELETE_CARDS_END_POINT, cardResource.getId()).then().statusCode(HttpStatus.NO_CONTENT.value());

		given().contentType(ContentType.JSON).when().get(GET_CARDS_END_POINT, cardResource.getId()).then()
				.statusCode(HttpStatus.NOT_FOUND.value()).body(MESSAGE_FIELD, is("Card not found."));
	}
}
