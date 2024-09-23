package edu.java.service;

import edu.java.repository.jpa.entity.CommonLink;
import java.util.List;
import reactor.core.publisher.Mono;

public interface LinkUpdater {
    void update(CommonLink link);

    <T extends CommonLink> List<T> findLinksToUpdate();

    List<Long> findTgChatIds(Long linkId);

    void updateGitBranches(CommonLink link, List<String> branches);

    void updateAnswerCount(CommonLink link, int answerCount);

    Mono<String> getUpdateForGithub(CommonLink link);

    Mono<String> getUpdateForStackOverflow(CommonLink link);
}
