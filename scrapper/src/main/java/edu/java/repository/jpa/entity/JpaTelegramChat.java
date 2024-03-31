package edu.java.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@AllArgsConstructor
@Entity
@Table(name = "chats")
@NoArgsConstructor
public class JpaTelegramChat {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chatId")
    private Collection<JpaLinkage> linkages = new HashSet<>();

    public JpaTelegramChat(Long id) {
        this.id = id;
    }
}
