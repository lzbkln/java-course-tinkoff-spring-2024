package edu.java.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "links")
@NoArgsConstructor
@AllArgsConstructor
public class JpaLink implements CommonLink {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "last_updated_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime lastUpdatedAt = OffsetDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "linkId")
    private Collection<JpaLinkage> linkages = new HashSet<>();

    public JpaLink(String url) {
        this.url = url;
    }

    public JpaLink(Long id, String url, OffsetDateTime lastUpdatedAt) {
        this.id = id;
        this.url = url;
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
