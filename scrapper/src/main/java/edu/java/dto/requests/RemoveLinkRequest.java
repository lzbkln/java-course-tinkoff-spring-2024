package edu.java.dto.requests;

import java.net.URI;

public record RemoveLinkRequest(
    URI link
) {
}
