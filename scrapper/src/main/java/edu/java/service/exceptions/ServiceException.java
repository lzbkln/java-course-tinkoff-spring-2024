package edu.java.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ServiceException extends RuntimeException {
    private static final String DESCRIPTION = "Error...";

    private static final HttpStatusCode HTTP_STATUS_CODE = HttpStatus.NOT_FOUND;

    ServiceException(String message) {
        super(message);
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public HttpStatusCode getHttpStatusCode() {
        return HTTP_STATUS_CODE;
    }
}
