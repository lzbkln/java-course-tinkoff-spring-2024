package edu.java.repository.jpa.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "linkage")
@NoArgsConstructor
@Data
public class JpaLinkage {
    @EmbeddedId
    private JpaLinkageId id = new JpaLinkageId();

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private JpaTelegramChat chatId;

    @ManyToOne
    @MapsId("linkId")
    @JoinColumn(name = "link_id")
    private JpaLink linkId;

    public JpaLinkage(JpaTelegramChat chatId, JpaLink linkId) {
        this.chatId = chatId;
        this.linkId = linkId;
    }
}
