package com.tsm.cards.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tomas on 6/17/18.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ExternalCallException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String errorCode;

    public ExternalCallException(final String errorCode) {
        this.errorCode = errorCode;
    }
}
