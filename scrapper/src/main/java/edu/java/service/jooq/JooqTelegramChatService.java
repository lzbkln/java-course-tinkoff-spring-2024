package edu.java.service.jooq;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import edu.java.service.TelegramChatService;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqTelegramChatService implements TelegramChatService {
    private final TelegramChatRepository telegramChatRepository;

    @Override
    @Transactional
    public void register(Long tgChatId) {
        try {
            telegramChatRepository.saveUser(new TelegramChat(tgChatId));
        } catch (DuplicateKeyException e) {
            throw new ReRegistrationException(tgChatId);
        }
    }

    @Override
    @Transactional
    public void unregister(Long tgChatId) {
        try {
            telegramChatRepository.deleteById(tgChatId);
        } catch (EmptyResultDataAccessException e) {
            throw new NonRegisterChatException(tgChatId);
        }
    }
}
