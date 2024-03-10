package edu.java.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ReRegistrationException extends ServiceException {
    private static final String DESCRIPTION = "Already register chat: %d";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.CONFLICT;

    public ReRegistrationException(String message) {
        super(message);
    }

    public ReRegistrationException(Long id) {
        super(DESCRIPTION.formatted(id));
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return HTTP_STATUS_CODE;
    }
}
