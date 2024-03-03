package edu.java.dto.responses;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
