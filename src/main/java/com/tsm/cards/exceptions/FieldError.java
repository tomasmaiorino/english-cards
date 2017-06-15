package com.tsm.cards.exceptions;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

public class FieldError {

	@Getter
	@Setter
	private String field;

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String message;

	public FieldError(String field2, String messageTemplate, String message2) {
		this.field = field2;
		this.code = messageTemplate;
		this.message = message2;
	}

	public static FieldError of(ConstraintViolation<?> constraintViolation) {

		String field = StringUtils.substringAfter(constraintViolation.getPropertyPath().toString(), ".");

		return new FieldError(field, constraintViolation.getMessageTemplate(), constraintViolation.getMessage());

	}

}
