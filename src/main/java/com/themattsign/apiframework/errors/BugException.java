package com.themattsign.apiframework.errors;

/**
 *
 * Created by MatthewMiddleton on 5/23/2017.
 */
public class BugException extends InrangeException {

    public BugException() {
        super();
    }

    public BugException(final String message) {
        super(message);
    }

    public BugException(final Throwable cause) {
        super(cause);
    }

    public BugException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
