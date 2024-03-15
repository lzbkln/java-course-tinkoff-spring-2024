package edu.java.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkageTable {
    private Long id;
    private Long chatId;
    private Long linkId;

    public LinkageTable(Long chatId, Long linkId) {
        this.chatId = chatId;
        this.linkId = linkId;
    }
}
