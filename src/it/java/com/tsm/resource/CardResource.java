package com.tsm.resource;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsm.cards.model.Card.CardStatus;
import com.tsm.cards.model.Client.ClientStatus;
import com.tsm.controller.CardsControllerIT;

import lombok.Getter;
import lombok.Setter;

public class CardResource {

    public static CardResource build() {
        return new CardResource();
    }

    public CardResource assertFields() {
        if (Objects.isNull(name)) {
            name();
        }
        if (Objects.isNull(imgUrl)) {
            imgUrl();
        }
        if (Objects.isNull(status)) {
            status();
        }
        if (Objects.isNull(cardType)) {
            cardType();
        }
        return this;
    }

    private CardResource() {
    }

    @JsonIgnore
    @Getter
    @Setter
    private Map<String, String> headers;

    @Getter
    @Setter
    private Integer id;

    @Getter
    private String name;

    @Getter
    private String imgUrl;

    @Getter
    private String status;

    @Getter
    private Integer cardType;

    public CardResource create() {
        assertFields();
        return given().headers(getHeaders()).contentType("application/json").body(this).when().post(CardsControllerIT.CARDS_END_POINT)
            .as(CardResource.class);
    }

    public CardResource imgUrl(final String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public CardResource imgUrl() {
        return imgUrl("http://" + random(20, true, false) + ".com");
    }

    public CardResource headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public CardResource cardType(final Integer cardType) {
        this.cardType = cardType;
        return this;
    }

    public CardResource cardType() {
        CardTypeResource type = CardTypeResource.build().assertFields().headers(getHeaders()).create();
        return cardType(type.getId());
    }

    public CardResource status() {
        return status(CardStatus.values()[RandomUtils.nextInt(0, ClientStatus.values().length - 1)].name());
    }

    public CardResource status(final String status) {
        this.status = status;
        return this;
    }

    public CardResource name() {
        return name(random(30, true, true));
    }

    public CardResource name(final String name) {
        this.name = name;
        return this;
    }
}
