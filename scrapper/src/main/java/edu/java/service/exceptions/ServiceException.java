package edu.java.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceException extends ResponseStatusException {
    ServiceException(HttpStatus status, String error) {
        super(status, error);
    }
}
