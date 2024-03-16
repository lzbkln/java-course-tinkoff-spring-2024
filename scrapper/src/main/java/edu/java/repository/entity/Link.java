package edu.java.repository.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Link {
    private Long id;
    private String url;
    private LocalDateTime lastUpdatedAt;

    public Link(String url) {
        this.url = url;
        this.lastUpdatedAt = LocalDateTime.MIN;
    }
}
