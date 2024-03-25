package edu.java.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;

@Embeddable
@Data
public class JpaLinkageId implements Serializable {

    @Column(name = "chat_id")
    long chatId;

    @Column(name = "link_id")
    long linkId;

}
