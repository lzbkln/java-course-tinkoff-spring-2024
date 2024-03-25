package edu.java.service;

import edu.java.dto.responses.ListLinksResponse;
import java.net.URI;
import org.springframework.transaction.annotation.Transactional;

public interface LinkService {
    @Transactional
    void saveLink(Long tgChatId, URI url);

    @Transactional
    void deleteLink(Long tgChatId, URI url);

    @Transactional
    ListLinksResponse getAllLinksResponse(Long tgChatId);
}
