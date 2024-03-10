package edu.java.service.exceptions;

public class NonRegisterChatException extends ServiceException {
    private static final String DESCRIPTION = "Telegram chat %d doesnt register";

    public NonRegisterChatException(Long id) {
        super(DESCRIPTION.formatted(id));
    }
}
