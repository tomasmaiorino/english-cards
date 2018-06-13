package com.tsm.cards.resource;

import com.tsm.cards.resources.BaseResource;
import com.tsm.cards.util.ClientTestBuilder;
import com.tsm.cards.util.ContactTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.function.Supplier;

import static com.tsm.cards.util.ClientTestBuilder.*;
import static com.tsm.cards.util.ErrorCodes.*;

@FixMethodOrder(MethodSorters.JVM)
public class ContactResourceTest extends BaseResourceTest {

    private Supplier<BaseResource> buildResourceFunction = ContactTestBuilder::buildResource;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    public void build_NullNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", null, REQUIRED_CONTACT_NAME);
    }

    @Test
    public void build_SmallNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", SMALL_NAME, INVALID_CONTACT_NAME_SIZE);
    }

    @Test
    public void build_LargeNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", LARGE_NAME, INVALID_CONTACT_NAME_SIZE);
    }

    @Test
    public void build_EmptyNameGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "name", "", INVALID_CONTACT_NAME_SIZE);
    }

    //

    @Test
    public void build_NullEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", null, REQUIRED_CONTACT_EMAIL);
    }

    @Test
    public void build_InvalidEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", RESOURCE_INVALID_EMAIL, INVALID_CONTACT_EMAIL);
    }

    @Test
    public void build_EmptyEmailGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "email", "", REQUIRED_CONTACT_EMAIL);
    }
    //

    @Test
    public void build_NullSubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", null, REQUIRED_CONTACT_SUBJECT);
    }

    @Test
    public void build_SmallSubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", SMALL_TOKEN, INVALID_CONTACT_SUBJECT_SIZE);
    }

    @Test
    public void build_LargeSubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", LARGE_TOKEN, INVALID_CONTACT_SUBJECT_SIZE);
    }

    @Test
    public void build_EmptySubjectGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "subject", "", INVALID_CONTACT_SUBJECT_SIZE);
    }

    //

    @Test
    public void build_NullMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", null, REQUIRED_CONTACT_MESSAGE);
    }

    @Test
    public void build_SmallMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", ContactTestBuilder.getSmallMessage(), INVALID_CONTACT_MESSAGE_SIZE);
    }

    @Test
    public void build_LargeMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", ContactTestBuilder.getLargeMessage(), INVALID_CONTACT_MESSAGE_SIZE);
    }

    @Test
    public void build_EmptyMessageGiven_ShouldThrowException() {
        // Set up
        checkResource(buildResourceFunction, "message", "", INVALID_CONTACT_MESSAGE_SIZE);
    }


}
