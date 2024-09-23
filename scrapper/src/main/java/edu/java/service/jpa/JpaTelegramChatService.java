package edu.java.service.jpa;

import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import edu.java.service.TelegramChatService;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaTelegramChatService implements TelegramChatService {
    private final JpaTelegramChatRepository jpaTelegramChatRepository;

    @Override
    @Transactional
    public void register(Long tgChatId) {
        if (jpaTelegramChatRepository.existsById(tgChatId)) {
            throw new ReRegistrationException(tgChatId);
        }
        jpaTelegramChatRepository.saveAndFlush(new JpaTelegramChat(tgChatId));
    }

    @Override
    @Transactional
    public void unregister(Long tgChatId) {
        if (!jpaTelegramChatRepository.existsById(tgChatId)) {
            throw new NonRegisterChatException(tgChatId);
        }
        jpaTelegramChatRepository.deleteById(tgChatId);
    }
}
