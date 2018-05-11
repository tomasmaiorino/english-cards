package com.tsm.cards.resources;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import static com.tsm.cards.util.ErrorCodes.*;

public class UserResource implements BaseResource {

	@Getter
	@Setter
	@NotEmpty(message = REQUIRED_LOGIN_EMAIL)
	@Email(message = INVALID_LOGIN_EMAIL)
	private String email;

	@Getter
	@Setter
	@NotNull(message = REQUIRED_LOGIN_PASSWORD)
	private String password;

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		UserResource other = (UserResource) obj;
		if (getEmail() == null || other.getEmail() == null) {
			return false;
		}
		return getEmail().equalsIgnoreCase(other.getEmail());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getEmail()).toHashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
