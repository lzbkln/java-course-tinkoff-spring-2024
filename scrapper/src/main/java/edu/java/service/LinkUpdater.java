package edu.java.service;

import edu.java.repository.jpa.entity.CommonLink;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface LinkUpdater {
    @Transactional
    void update(CommonLink link);

    @Transactional <T extends CommonLink> List<T> findLinksToUpdate();

    @Transactional
    List<Long> findTgChatIds(Long linkId);

    @Transactional
    void updateGitBranches(Long linkId, Set<String> branches);

    @Transactional
    void updateAnswerCount(Long linkId, int answerCount);

    @Transactional
    Mono<String> getUpdateForGithub(CommonLink link);

    @Transactional
    Mono<String> getUpdateForStackOverflow(CommonLink link);
}
