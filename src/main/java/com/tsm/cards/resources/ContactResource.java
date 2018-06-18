package com.tsm.cards.resources;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.tsm.cards.util.ContactConstants.*;
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
    @Size(min = CONTACT_MIN_NAME_SIZE, max = CONTACT_MAX_NAME_SIZE, message = INVALID_CONTACT_NAME_SIZE)
    private String name;

    @Getter
    @Setter
    @NotEmpty(message = REQUIRED_CONTACT_EMAIL)
    @Email(message = INVALID_CONTACT_EMAIL)
    private String email;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CONTACT_MESSAGE)
    @Size(min = CONTACT_MIN_MESSAGE_SIZE, max = CONTACT_MAX_MESSAGE_SIZE, message = INVALID_CONTACT_MESSAGE_SIZE)
    private String message;

    @Getter
    @Setter
    @NotNull(message = REQUIRED_CONTACT_SUBJECT)
    @Size(min = CONTACT_MIN_SUBJECT_SIZE, max = CONTACT_MAX_SUBJECT_SIZE, message = INVALID_CONTACT_SUBJECT_SIZE)
    private String subject;
    
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
