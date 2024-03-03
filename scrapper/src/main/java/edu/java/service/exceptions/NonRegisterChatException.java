package edu.java.service.exceptions;

import org.springframework.http.HttpStatus;

public class NonRegisterChatException extends ServiceException {
    private static final String DESCRIPTION = "Telegram chat %d doesnt register";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.NOT_FOUND;

    public NonRegisterChatException(Long id) {
        super(HTTP_STATUS_CODE, DESCRIPTION.formatted(id));
    }
}
