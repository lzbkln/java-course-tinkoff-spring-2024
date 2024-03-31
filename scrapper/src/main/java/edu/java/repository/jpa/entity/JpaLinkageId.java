package edu.java.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class JpaLinkageId implements Serializable {
    @Column(name = "chat_id")
    Long chatId;

    @Column(name = "link_id")
    Long linkId;
}
