package edu.java.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NonRegisterChatException extends ServiceException {
    private static final String DESCRIPTION = "Telegram chat %d doesnt register";
    private static final HttpStatus HTTP_STATUS_CODE = HttpStatus.NOT_FOUND;

    public NonRegisterChatException(String message) {
        super(message);
    }

    public NonRegisterChatException(Long id) {
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
