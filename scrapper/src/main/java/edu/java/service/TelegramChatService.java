package edu.java.service;

import org.springframework.transaction.annotation.Transactional;

public interface TelegramChatService {
    @Transactional
    void register(Long tgChatId);

    @Transactional
    void unregister(Long tgChatId);
}
