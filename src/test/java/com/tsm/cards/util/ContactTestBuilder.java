package com.tsm.cards.util;

import com.tsm.cards.resources.ContactResource;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by tomas on 6/12/18.
 */
public class ContactTestBuilder extends  BaseTestBuilder {

    public static ContactResource buildResource() {
        return buildResource(getValidEmail(), getSubject(), getMessage(), getName());
    }

    public static ContactResource buildResource(final String email, final String subject, final String message, final String name) {
        ContactResource resource = new ContactResource();
        resource.setEmail(email);
        resource.setName(name);
        resource.setMessage(message);
        resource.setSubject(subject);
        return resource;
    }

    public static String getSubject() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MAX_SUBJECT_SIZE, true, true);
    }

    public static String getSmallSubject() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MIN_SUBJECT_SIZE - 1, true, true);
    }

    public static String getLargeSubject() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MAX_SUBJECT_SIZE + 1, true, true);
    }

    public static String getName() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MAX_NAME_SIZE, true, true);
    }

    public static String getSmallName() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MIN_NAME_SIZE- 1, true, true);
    }

    public static String getLargeName() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MAX_NAME_SIZE + 1, true, true);
    }

    public static String getMessage() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MAX_MESSAGE_SIZE, true, true);
    }

    public static String getSmallMessage() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MIN_MESSAGE_SIZE - 1, true, true);
    }

    public static String getLargeMessage() {
        return RandomStringUtils.random(ContactConstants.CONTACT_MAX_MESSAGE_SIZE + 1, true, true);
    }
}
