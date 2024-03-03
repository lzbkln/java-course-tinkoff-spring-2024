package edu.java.dto.requests;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LinkUpdateRequest(
    Long id,
    @NotNull
    String url,
    @NotNull
    String description,
    List<Long> tgChatIds
) {
}
