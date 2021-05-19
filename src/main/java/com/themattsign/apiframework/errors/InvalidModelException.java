package com.themattsign.apiframework.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * Created by MatthewMiddleton on 5/23/2017.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class InvalidModelException extends RuntimeException {
    public InvalidModelException(String description) {
        super(description);
    }

    @Deprecated
    public InvalidModelException(final String description, final String message) {
        super(message);
    }

    public InvalidModelException(final String description, final Throwable cause) {
        super(description, cause);
    }

    @Deprecated
    public InvalidModelException(final String description, final String message, final Throwable cause) {
        super(message, cause);
    }
}
