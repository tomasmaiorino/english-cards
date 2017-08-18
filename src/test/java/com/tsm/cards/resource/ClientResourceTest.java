package com.tsm.cards.resource;

import static com.tsm.cards.util.ClientTestBuilder.LARGE_NAME;
import static com.tsm.cards.util.ClientTestBuilder.LARGE_TOKEN;
import static com.tsm.cards.util.ClientTestBuilder.RESOURCE_INVALID_EMAIL;
import static com.tsm.cards.util.ClientTestBuilder.SMALL_NAME;
import static com.tsm.cards.util.ClientTestBuilder.SMALL_TOKEN;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_EMAIL;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_EMAIL_RECIPIENT;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_NAME_SIZE;
import static com.tsm.cards.util.ErrorCodes.INVALID_CLIENT_TOKEN_SIZE;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_EMAIL;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_EMAIL_RECIPIENT;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_HOSTS;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_NAME;
import static com.tsm.cards.util.ErrorCodes.REQUIRED_CLIENT_TOKEN;

import java.util.Collections;
import java.util.function.Supplier;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.util.ClientTestBuilder;

@FixMethodOrder(MethodSorters.JVM)
public class ClientResourceTest extends BaseResourceTest {

    private Supplier<? extends BaseResource> buildResourceFunction = ClientTestBuilder::buildResoure;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void build_NullNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", null, REQUIRED_CLIENT_NAME);
    }

    @Test
    public void build_SmallNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_CLIENT_NAME_SIZE);
    }

    @Test
    public void build_LargeNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_CLIENT_NAME_SIZE);
    }

    @Test
    public void build_EmptyNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", "", INVALID_CLIENT_NAME_SIZE);
    }

    //

    @Test
    public void build_NullTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", null, REQUIRED_CLIENT_TOKEN);
    }

    @Test
    public void build_SmallTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", SMALL_TOKEN, INVALID_CLIENT_TOKEN_SIZE);
    }

    @Test
    public void build_LargeTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", LARGE_TOKEN, INVALID_CLIENT_TOKEN_SIZE);
    }

    @Test
    public void build_EmptyTokenGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "token", "", INVALID_CLIENT_TOKEN_SIZE);
    }

    //

    @Test
    public void build_NullEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", null, REQUIRED_CLIENT_EMAIL);
    }

    @Test
    public void build_InvalidEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", RESOURCE_INVALID_EMAIL, INVALID_CLIENT_EMAIL);
    }

    @Test
    public void build_EmptyEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", "", REQUIRED_CLIENT_EMAIL);
    }

    @Test
    public void build_NullEmailRecipientGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "emailRecipient", null, REQUIRED_CLIENT_EMAIL_RECIPIENT);
    }

    @Test
    public void build_InvalidEmailRecipientGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "emailRecipient", RESOURCE_INVALID_EMAIL, INVALID_CLIENT_EMAIL_RECIPIENT);
    }

    @Test
    public void build_EmptyEmailRecipientGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "emailRecipient", "", REQUIRED_CLIENT_EMAIL_RECIPIENT);
    }

    //

    @Test
    public void build_NullHostsGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "hosts", null, REQUIRED_CLIENT_HOSTS);
    }

    @Test
    public void build_EmptyHostsGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "hosts", Collections.emptySet(), REQUIRED_CLIENT_HOSTS);
    }

    // TODO validate hosts content
}
