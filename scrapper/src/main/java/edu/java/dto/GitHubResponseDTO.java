package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubResponseDTO(@JsonProperty("full_name") String fullName,
                                @JsonProperty("created_at") OffsetDateTime createdAt,
                                @JsonProperty("updated_at") OffsetDateTime updatedAt,
                                @JsonProperty("pushed_at") OffsetDateTime pushedAt) {
}
