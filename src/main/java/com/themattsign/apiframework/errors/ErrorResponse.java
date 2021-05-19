package com.themattsign.apiframework.errors;

/**
 *
 * Created by MatthewMiddleton on 5/22/2017.
 */
public class ErrorResponse {

    private final String description;
    private final String cause;

    public ErrorResponse(String description, String cause) {
        this.description = description;
        this.cause = cause;
    }

    public String getDescription() {
        return description;
    }

    public String getCause() {
        return cause;
    }
}
