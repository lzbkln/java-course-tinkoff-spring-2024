package edu.java.bot.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LinkUpdate {
    Long id;
    @NotNull
    String url;
    @NotNull
    String description;
    List<Long> tgChatIds;
}
