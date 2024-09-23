package edu.java.service;

import edu.java.dto.responses.ListLinksResponse;
import java.net.URI;

public interface LinkService {
    void saveLink(Long tgChatId, URI url);

    void deleteLink(Long tgChatId, URI url);

    ListLinksResponse getAllLinksResponse(Long tgChatId);
}
