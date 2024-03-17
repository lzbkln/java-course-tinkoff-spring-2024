package edu.java.repository.entity;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TelegramChat {
    private Long id;
    private OffsetDateTime createdAt;

    public TelegramChat(Long chatId) {
        this(chatId, OffsetDateTime.now());
    }
}
