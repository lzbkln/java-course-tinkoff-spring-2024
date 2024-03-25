package edu.java.service.jdbc;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import edu.java.service.TelegramChatService;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

@RequiredArgsConstructor
public class JdbcTelegramChatService implements TelegramChatService {
    private final TelegramChatRepository telegramChatRepository;

    @Override
    public void register(Long tgChatId) {
        try {
            telegramChatRepository.saveUser(new TelegramChat(tgChatId));
        } catch (DuplicateKeyException e) {
            throw new ReRegistrationException(tgChatId);
        }
    }

    @Override
    public void unregister(Long tgChatId) {
        try {
            telegramChatRepository.deleteById(tgChatId);
        } catch (EmptyResultDataAccessException e) {
            throw new NonRegisterChatException(tgChatId);
        }
    }
}
