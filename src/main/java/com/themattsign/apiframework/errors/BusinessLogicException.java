package com.themattsign.apiframework.errors;

/**
 *
 * Created by MatthewMiddleton on 5/23/2017.
 */
public class BusinessLogicException extends InrangeException {

    public BusinessLogicException() {
        super();
    }

    public BusinessLogicException(final String message) {
        super(message);
    }

    public BusinessLogicException(final Throwable cause) {
        super(cause);
    }

    public BusinessLogicException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
