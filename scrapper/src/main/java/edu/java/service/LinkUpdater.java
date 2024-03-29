package edu.java.service;

import edu.java.repository.jpa.entity.CommonLink;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface LinkUpdater {
    @Transactional
    void update(CommonLink link);

    @Transactional <T extends CommonLink> List<T> findLinksToUpdate();

    @Transactional
    List<Long> findTgChatIds(Long linkId);

    void updateGitBranches(CommonLink link, List<String> branches);

    void updateAnswerCount(CommonLink link, int answerCount);

    @Transactional
    Mono<String> getUpdateForGithub(CommonLink link);

    @Transactional
    Mono<String> getUpdateForStackOverflow(CommonLink link);
}
