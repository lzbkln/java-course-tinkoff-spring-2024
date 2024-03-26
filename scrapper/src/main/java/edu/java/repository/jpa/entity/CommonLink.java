package edu.java.repository.jpa.entity;

import java.time.OffsetDateTime;

public interface CommonLink {
    Long getId();

    String getUrl();

    OffsetDateTime getLastUpdatedAt();
}
