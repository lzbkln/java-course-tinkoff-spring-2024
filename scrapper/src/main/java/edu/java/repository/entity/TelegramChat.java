package edu.java.repository.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TelegramChat {
    private Long chatId;
    private LocalDateTime createdAt;

    public TelegramChat(Long chatId) {
        this(chatId, LocalDateTime.now());
    }
}
