package com.tsm.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.tsm.cards.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.ClientTestBuilder.SMALL_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

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
import com.tsm.resource.CardTypeResource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnglishCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "it" })
@FixMethodOrder(MethodSorters.JVM)
public class CardsTypeControllerIT extends BaseTestIT {

    public static final String CARDS_TYPE_END_POINT = "/api/v1/cards-type";

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
        given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("[0].message", is("The name is required."), "[0].field", is("name"));
    }

    @Test
    public void save_EmptyNameGiven_ShouldReturnError() {
        // Set Up
        CardTypeResource resource = CardTypeResource.build().assertFields().name("");

        // Do Test
        given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
    }

    @Test
    public void save_SmallNameGiven_ShouldReturnError() {
        // Set Up
        CardTypeResource resource = CardTypeResource.build().assertFields().name(SMALL_NAME);

        // Do Test
        given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
    }

    @Test
    public void save_LargeNameGiven_ShouldReturnError() {
        // Set Up
        CardTypeResource resource = CardTypeResource.build().assertFields().name(LARGE_NAME);

        // Do Test
        given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("[0].message", is("The name must be between 2 and 30 characters."), "[0].field", is("name"));
    }

    @Test
    public void save_ValidResourceGiven_ShouldSaveClient() {
        // Set Up
        CardTypeResource resource = CardTypeResource.build().assertFields();

        // Do Test
        given().headers(getHeader()).body(resource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
            .statusCode(HttpStatus.CREATED.value()).body("name", is(resource.getName()))
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
        given().headers(getHeader()).body(newResource).contentType(ContentType.JSON).when().post(CARDS_TYPE_END_POINT).then()
            .statusCode(HttpStatus.BAD_REQUEST.value()).body("message", is("Duplicated card type name."));
    }

}
