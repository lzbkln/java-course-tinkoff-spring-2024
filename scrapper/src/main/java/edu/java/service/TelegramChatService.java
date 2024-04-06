package edu.java.service;

public interface TelegramChatService {
    void register(Long tgChatId);

    void unregister(Long tgChatId);
}
