package edu.java.entity;

import java.net.URI;

public record Link(
    Long id,
    URI url
) {
}
