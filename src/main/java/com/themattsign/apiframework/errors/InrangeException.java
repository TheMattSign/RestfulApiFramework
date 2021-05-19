package com.themattsign.apiframework.errors;

public class InrangeException extends RuntimeException {

    public InrangeException() {
        super();
    }

    public InrangeException(final String message) {
        super(message);
    }

    public InrangeException(final Throwable cause) {
        super(cause);
    }

    public InrangeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
