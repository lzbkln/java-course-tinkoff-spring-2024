package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponseDTO(@JsonProperty("items") List<Question> items) {
    public record Question(
        @JsonProperty("title") String title,
        @JsonProperty("answer_count") int answerCount,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
    ) {
    }
}
