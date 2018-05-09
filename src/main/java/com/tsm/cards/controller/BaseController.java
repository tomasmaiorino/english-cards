package com.tsm.cards.controller;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.tsm.cards.model.BaseModel;

@SuppressWarnings(value = { "rawtypes" })
public abstract class BaseController<R, M extends BaseModel, I extends Serializable> {

    public static final String JSON_VALUE = "application/json";
    
    @Autowired
    private Validator validator;

	protected <T> void validate(final T object, Class clazz) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, clazz);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

}
