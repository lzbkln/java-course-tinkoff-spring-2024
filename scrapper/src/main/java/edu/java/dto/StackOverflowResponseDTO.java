package edu.java.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponseDTO(List<Question> items) {
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public record Question(
        String title,
        int answerCount,
        OffsetDateTime lastActivityDate
    ) {
    }
}
