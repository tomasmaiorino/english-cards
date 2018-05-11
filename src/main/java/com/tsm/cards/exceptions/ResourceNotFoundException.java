package com.tsm.cards.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String errorCode;

    public ResourceNotFoundException(final String errorCode) {
        this.setErrorCode(errorCode);
    }

    public ResourceNotFoundException(final String errorCode, String message) {
        super(message);
    }

}
