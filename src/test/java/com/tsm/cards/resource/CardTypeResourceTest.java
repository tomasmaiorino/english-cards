package com.tsm.cards.resource;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.util.CardTypeTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.function.Supplier;

import static com.tsm.cards.util.CardTypeTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.CardTypeTestBuilder.SMALL_NAME;
import static com.tsm.cards.util.ErrorCodes.*;

@FixMethodOrder(MethodSorters.JVM)
public class CardTypeResourceTest extends BaseResourceTest {

    private Supplier<BaseResource> buildResourceFunction = CardTypeTestBuilder::buildResource;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void build_NullNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", null, REQUIRED_CARD_TYPE_NAME);
    }

    @Test
    public void build_SmallNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_CARD_TYPE_NAME_SIZE);
    }

    @Test
    public void build_LargeNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_CARD_TYPE_NAME_SIZE);
    }

    @Test
    public void build_EmptyNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", "", INVALID_CARD_TYPE_NAME_SIZE);
    }
    

    @Test
    public void build_NullStatusGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "status", null, REQUIRED_CARD_TYPE_STATUS);
    }

    @Test
    public void build_InvalidStatusGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "status", INVALID_CARD_TYPE_STATUS, INVALID_CARD_TYPE_STATUS);
    }

}
