package edu.java.dto.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record GitHubResponseDTO(String fullName,
                                OffsetDateTime createdAt,
                                OffsetDateTime updatedAt,
                                OffsetDateTime pushedAt) {
}
