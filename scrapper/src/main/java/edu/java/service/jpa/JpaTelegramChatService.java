package edu.java.service.jpa;

import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import edu.java.service.TelegramChatService;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaTelegramChatService implements TelegramChatService {
    private final JpaTelegramChatRepository jpaTelegramChatRepository;

    @Override
    public void register(Long tgChatId) {
        if (jpaTelegramChatRepository.existsById(tgChatId)) {
            throw new ReRegistrationException(tgChatId);
        }
        jpaTelegramChatRepository.save(new JpaTelegramChat(tgChatId));
    }

    @Override
    public void unregister(Long tgChatId) {
        if (!jpaTelegramChatRepository.existsById(tgChatId)) {
            throw new NonRegisterChatException(tgChatId);
        }
        jpaTelegramChatRepository.deleteById(tgChatId);
    }
}
