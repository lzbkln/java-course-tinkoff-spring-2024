package edu.java.service;

import edu.java.entity.TelegramChat;
import edu.java.repository.TelegramChatRepository;
import edu.java.service.exceptions.NonRegisterChatException;
import edu.java.service.exceptions.ReRegistrationException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final TelegramChatRepository telegramChatRepository;

    public void addNewChat(Long id) {
        if (telegramChatRepository.findById(id) != null) {
            throw new ReRegistrationException(id);
        }
        telegramChatRepository.saveUser(new TelegramChat(id, new ArrayList<>()));
    }

    public void deleteChat(Long id) {
        if (telegramChatRepository.findById(id) == null) {
            throw new NonRegisterChatException(id);
        }
        telegramChatRepository.deleteById(id);
    }
}
