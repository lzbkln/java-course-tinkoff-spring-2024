package edu.java.service;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final TelegramChatRepository telegramChatRepository;

    public void addNewChat(Long id) {
        if (telegramChatRepository.findById(id).isPresent()) {
            throw new ReRegistrationException(id);
        }
        telegramChatRepository.saveUser(new TelegramChat(id));
    }

    public void deleteChat(Long id) {
        if (telegramChatRepository.findById(id).isEmpty()) {
            throw new NonRegisterChatException(id);
        }
        telegramChatRepository.deleteById(id);
    }
}
