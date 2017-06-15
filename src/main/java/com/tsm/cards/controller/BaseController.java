package com.tsm.cards.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.tsm.cards.exceptions.FieldError;

@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class BaseController {

	@RequestMapping(produces = "application/json")
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ResponseEntity<List<FieldError>> handleConstraintViolationException(
			ConstraintViolationException ex) {
		return new ResponseEntity(getErrors(ex.getConstraintViolations()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	protected @ResponseBody ResponseEntity<Map<String, String>> handleException(final IllegalArgumentException e) {
		Map<String, String> response = new HashMap<>();
		response.put("message", e.getMessage());
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	public static List<FieldError> getErrors(Set<ConstraintViolation<?>> constraintViolations) {
		return constraintViolations.stream().map(FieldError::of).collect(Collectors.toList());
	}
}
