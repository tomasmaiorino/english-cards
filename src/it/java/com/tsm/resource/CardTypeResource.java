package com.tsm.resource;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsm.controller.CardsTypeControllerIT;

import lombok.Getter;
import lombok.Setter;

public class CardTypeResource {

    public static CardTypeResource build() {
        return new CardTypeResource();
    }

    public CardTypeResource assertFields() {
        if (Objects.isNull(name)) {
            name();
        }
        return this;
    }

    public CardTypeResource create() {
        assertFields();
        return given().headers(getHeaders()).contentType("application/json").body(this).when()
            .post(CardsTypeControllerIT.CARDS_TYPE_END_POINT)
            .as(CardTypeResource.class);
    }

    private CardTypeResource() {
    }

    @JsonIgnore
    @Getter
    @Setter
    private Map<String, String> headers;

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Set<CardResource> cards;

    public CardTypeResource headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public CardTypeResource name() {
        return name(random(30, true, true));
    }

    public CardTypeResource name(final String name) {
        this.name = name;
        return this;
    }
}
