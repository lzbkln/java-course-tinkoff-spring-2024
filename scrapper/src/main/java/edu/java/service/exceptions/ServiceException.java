package edu.java.service.exceptions;

public class ServiceException extends RuntimeException {
    private static final String DESCRIPTION = "Error...";

    ServiceException(String message) {
        super(message);
    }

    public String getDescription() {
        return DESCRIPTION;
    }

}
