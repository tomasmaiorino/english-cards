package com.tsm.cards.resources;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.tsm.cards.util.ErrorCodes.*;

/**
 * Created by tomas on 6/10/18.
 */
public class ContactResource implements BaseResource {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CONTACT_NAME)
    @Size(min = 2, max = 30, message = INVALID_CONTACT_NAME_SIZE)
    private String name;

    @Getter
    @Setter
    @NotEmpty(message = REQUIRED_CONTACT_EMAIL)
    @Email(message = INVALID_CONTACT_EMAIL)
    private String email;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CONTACT_MESSAGE)
    @Size(min = 2, max = 300, message = INVALID_CONTACT_MESSAGE_SIZE)
    private String message;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CONTACT_SUBJECT)
    @Size(min = 2, max = 30, message = INVALID_CONTACT_SUBJECT_SIZE)
    private String subject;
}
