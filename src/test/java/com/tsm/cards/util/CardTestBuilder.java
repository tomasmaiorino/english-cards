package com.tsm.cards.util;

import static org.apache.commons.lang3.RandomStringUtils.random;

import org.apache.commons.lang3.RandomUtils;

import com.tsm.cards.model.Card;
import com.tsm.cards.model.Card.CardStatus;
import com.tsm.cards.model.CardType;
import com.tsm.cards.resources.CardResource;

public class CardTestBuilder {

    public static final String LARGE_NAME = random(31, true, true);
    public static final String SMALL_NAME = random(1, true, true);
    public static final String INVALID_IMG_URL = random(31, true, true);
    public static final String LARGE_IMG_URL = random(151, true, true);
    public static final String SMALL_IMG_URL = random(1, true, true);
    public static final String INVALID_STATUS = "INVD";

    public static final String CARD_NAME = random(30, true, true);
    public static final String CARD_TYPE_NAME = random(30, true, true);
    public static final CardType CARD_TYPE = buildCardType();
    public static final String CARD_STATUS = CardStatus.ACTIVE.name();
    public static final String CARD_IMG_URL = "http://" + random(20, true, false) + ".com";

    private static final CardStatus STATUS = CardStatus.ACTIVE;
    private static final Integer CARD_TYPE_ID = RandomUtils.nextInt(10, 1000);

    public static CardType buildCardType() {
        CardType cardType = new CardType();
        cardType.setName(CARD_TYPE_NAME);
        return cardType;
    }

    public static Card buildModel() {
        return buildModel(CARD_NAME, CARD_IMG_URL, CARD_TYPE, STATUS);
    }

    public static Card buildModel(final String name, final String imgUrl, final CardType cardType, final CardStatus cardStatus) {
        Card card = new Card();
        card.setName(name);
        card.setImgUrl(imgUrl);
        card.setCardType(cardType);
        card.setCardStatus(cardStatus);
        return card;
    }
    //
    // public static Set<String> buildClientHost(Set<ClientHosts> hosts) {
    // return hosts.stream().map(h -> h.getHost()).collect(Collectors.toSet());
    // }
    //
    // public static Set<ClientHosts> buildClientHost(final String... hosts) {
    // Set<ClientHosts> clientHosts = new HashSet<>(hosts.length);
    // ClientHosts ch = null;
    // for (String s : hosts) {
    // ch = new ClientHosts();
    // ch.setHost(s);
    // clientHosts.add(ch);
    // }
    // return clientHosts;
    // }
    //
    // public static Resource buildResource(final String name, final String token, final String email,
    // final Set<String> hosts, final String status, final String emailRecipient) {
    // ClientResource resource = new ClientResource();
    // resource.setName(name);
    // resource.setEmail(email);
    // resource.setHosts(hosts);
    // resource.setToken(token);
    // resource.setStatus(status);
    // resource.setEmailRecipient(emailRecipient);
    // return resource;
    // }
    //
    // public static ClientResource buildResoure() {
    // return buildResource(CLIENT_NAME, CLIENT_TOKEN, CLIENT_EMAIL, buildClientHost(CLIENT_HOSTS),
    // STATUS.toString(), CLIENT_EMAIL_RECEIPIENT);
    // }

    public static CardResource buildResource() {
        return buildResource(CARD_TYPE_ID, CARD_NAME, CARD_IMG_URL, CARD_STATUS);
    }

    public static CardResource buildResource(final Integer cardTypeId, final String name, final String imgUrl, final String status) {
        CardResource resource = new CardResource();
        resource.setCardType(cardTypeId);
        resource.setImgUrl(imgUrl);
        resource.setName(name);
        resource.setStatus(status);
        return resource;
    }
}
