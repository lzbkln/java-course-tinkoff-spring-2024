package edu.java.repository.entity;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastUpdatedAt;

    public Link(String url) {
        this.url = url;
        this.lastUpdatedAt = OffsetDateTime.now();
    }
}
