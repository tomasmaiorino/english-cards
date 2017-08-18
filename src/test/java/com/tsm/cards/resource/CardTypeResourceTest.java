package com.tsm.cards.resource;

import static com.tsm.cards.util.CardTypeTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.CardTypeTestBuilder.SMALL_NAME;
import static com.tsm.cards.util.ErrorCodes.INVALID_CARD_TYPE_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CARD_TYPE_NAME;

import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.util.CardTypeTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class CardTypeResourceTest extends BaseResourceTest {

    private Supplier<? extends BaseResource> buildResourceFunction = CardTypeTestBuilder::buildResource;

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

}