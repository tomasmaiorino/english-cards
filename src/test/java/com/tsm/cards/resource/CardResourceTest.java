package com.tsm.cards.resource;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.util.CardTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.function.Supplier;

import static com.tsm.cards.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.ClientTestBuilder.SMALL_NAME;
import static com.tsm.cards.util.ErrorCodes.*;

@FixMethodOrder(MethodSorters.JVM)
public class CardResourceTest extends BaseResourceTest {

    private Supplier<BaseResource> buildResourceFunction = CardTestBuilder::buildResource;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void build_NullNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", null, REQUIRED_CARD_NAME);
    }

    @Test
    public void build_SmallNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_CARD_NAME_SIZE);
    }

    @Test
    public void build_LargeNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_CARD_NAME_SIZE);
    }

    @Test
    public void build_EmptyNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", "", INVALID_CARD_NAME_SIZE);
    }

    //

    @Test
    public void build_NullImgUrlGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "imgUrl", null, REQUIRED_CARD_IMG_URL);
    }

    @Test
    public void build_SmallImgUrlGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "imgUrl", CardTestBuilder.SMALL_IMG_URL, INVALID_CARD_IMG_URL_SIZE);
    }

    @Test
    public void build_LargeImgUrlGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "imgUrl", CardTestBuilder.LARGE_IMG_URL, INVALID_CARD_IMG_URL_SIZE);
    }

    @Test
    public void build_EmptyImgUrlGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "imgUrl", "", INVALID_CARD_IMG_URL_SIZE);
    }

    //

    @Test
    public void build_NullStatusGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "status", null, REQUIRED_CARD_STATUS);
    }

    @Test
    public void build_InvalidStatusGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "status", INVALID_CARD_STATUS, INVALID_CARD_STATUS);
    }

    @Test
    public void build_NullCardTypeGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "cardType", null, REQUIRED_CARD_TYPE);
    }

    @Test
    public void build_InvalidCardTypeGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "cardType", 0, INVALID_CARD_TYPE);
    }

}
