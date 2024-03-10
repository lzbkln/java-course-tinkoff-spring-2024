package edu.java.service.exceptions;

public class ReRegistrationException extends ServiceException {
    private static final String DESCRIPTION = "Already register chat: %d";

    public ReRegistrationException(Long id) {
        super(DESCRIPTION.formatted(id));
    }
}
