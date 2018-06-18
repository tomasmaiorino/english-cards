package com.tsm.cards.controller;

import com.tsm.cards.exceptions.*;
import exception.FieldError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerAdvice {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ResponseEntity<List<FieldError>> handleConstraintViolationException(
			ConstraintViolationException ex) {
		List<FieldError> response = ex.getConstraintViolations().stream().map(generateFieldErrorFunction)
				.collect(Collectors.toList());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MessageException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ResponseEntity<FieldError> handleMessageException(MessageException exception) {
		String messageError = resolveLocalizedMessage(exception.getErrorCode());
		return new ResponseEntity<>(new FieldError(messageError, ""), HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ResponseEntity<FieldError> handleGenericError(Exception exception) {
		String messageError = resolveLocalizedMessage(StringUtils.isNotBlank(exception.getMessage()) ? exception.getMessage() : "genericError");
		return new ResponseEntity<>(new FieldError(messageError, ""), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ExternalCallException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ResponseEntity<FieldError> handleExternalCallException(ExternalCallException exception) {
		String messageError = resolveLocalizedMessage(exception.getErrorCode());
		return new ResponseEntity<>(new FieldError(messageError, ""), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody ResponseEntity<FieldError> handleNotFoundMessageException(
			final ResourceNotFoundException exception) {
		String messageError = resolveLocalizedMessage(exception.getErrorCode());
		return new ResponseEntity<>(new FieldError(messageError, ""), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ForbiddenRequestException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody ResponseEntity<FieldError> handleForbiddenRequestException(
			final ForbiddenRequestException exception) {
		String messageError = resolveLocalizedMessage(exception.getErrorCode());
		return new ResponseEntity<>(new FieldError(messageError, ""), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ResponseEntity<FieldError> handleBadRequestMessageException(
			final BadRequestException exception) {
		String messageError = resolveLocalizedMessage(exception.getErrorCode());
		return new ResponseEntity<>(new FieldError(messageError, ""), HttpStatus.BAD_REQUEST);
	}

	private String resolveLocalizedMessage(final String key, final Object... args) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, args, "General Error", currentLocale);
	}

	private Function<ConstraintViolation<?>, FieldError> generateFieldErrorFunction = (c) -> {
		String errorMessage = resolveLocalizedMessage(c.getMessageTemplate());
		return new FieldError(errorMessage, c.getPropertyPath().toString());
	};

}
