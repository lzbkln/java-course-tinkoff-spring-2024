package edu.java.service.exceptions;

import org.springframework.http.HttpStatus;

public class ReRegistrationException extends ServiceException {
    private static final String DESCRIPTION = "Already register chat: %d";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.CONFLICT;

    public ReRegistrationException(Long id) {
        super(HTTP_STATUS_CODE, DESCRIPTION.formatted(id));
    }
}
