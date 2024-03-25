package edu.java.service.jpa;

import edu.java.clients.sites.GitHubClient;
import edu.java.clients.sites.StackOverflowClient;
import edu.java.clients.sites.util.Utils;
import edu.java.dto.responses.GithubBranchResponseDTO;
import edu.java.dto.responses.StackOverflowResponseDTO;
import edu.java.repository.jpa.JpaGithubBranchesRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaLinkageRepository;
import edu.java.repository.jpa.JpaStackOverflowQuestionRepository;
import edu.java.repository.jpa.entity.CommonLink;
import edu.java.repository.jpa.entity.JpaLink;
import edu.java.repository.jpa.entity.JpaLinkage;
import edu.java.repository.jpa.entity.JpaTelegramChat;
import edu.java.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class JpaLinkUpdaterService implements LinkUpdater {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaLinkageRepository jpaLinkageRepository;
    private final JpaGithubBranchesRepository jpaGithubBranchesRepository;
    private final JpaStackOverflowQuestionRepository jpaStackOverflowQuestionRepository;
    private final Utils utils;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public void update(CommonLink link) {
        jpaLinkRepository.updateById(link.getId(), link.getLastUpdatedAt());
    }

    @Override
    public List<JpaLink> findLinksToUpdate() {
        return jpaLinkRepository.findByLastUpdatedAtBefore(OffsetDateTime.now().minusHours(1));
    }

    @Override
    public List<Long> findTgChatIds(Long linkId) {
        return jpaLinkageRepository
            .findByLinkId(linkId)
            .stream()
            .map(JpaLinkage::getChatId)
            .map(JpaTelegramChat::getId)
            .collect(Collectors.toList());
    }

    @Override
    public void updateGitBranches(Long linkId, Set<String> branches) {
        jpaGithubBranchesRepository.updateData(linkId, branches);
    }

    @Override
    public void updateAnswerCount(Long linkId, int answerCount) {
        jpaStackOverflowQuestionRepository.updateData(linkId, answerCount);
    }

    @Override
    public Mono<String> getUpdateForGithub(CommonLink link) {
        String path = utils.extractPathForGithub(link.getUrl());
        return gitHubClient.getUserRepository(path)
            .filter(response -> response.pushedAt().isAfter(link.getLastUpdatedAt()))
            .flatMap(response -> getBranchesUpdate(link, utils.getBranches(link.getUrl())));
    }

    public Mono<String> getBranchesUpdate(CommonLink link, Mono<GithubBranchResponseDTO[]> branchesMono) {
        return branchesMono
            .flatMap(branchesArray -> {
                Set<String> newBranches = Arrays.stream(branchesArray)
                    .map(GithubBranchResponseDTO::name)
                    .collect(Collectors.toSet());
                Set<String> tempBranches = new HashSet<>(newBranches);
                Set<String> oldBranches = jpaGithubBranchesRepository.findByLinkId(link.getId()).getBranches();
                tempBranches.removeAll(oldBranches);
                if (!tempBranches.isEmpty()) {
                    updateGitBranches(link.getId(), newBranches);
                    return Mono.just("Добавление новыx веток по ссылке: %s. Добавлены: ".formatted(link.getUrl())
                        + String.join(", ", tempBranches));
                }
                return Mono.empty();
            });
    }

    @Override
    public Mono<String> getUpdateForStackOverflow(CommonLink link) {
        String questionId = utils.extractPathForStackoverflow(link.getUrl());
        return stackOverflowClient.getQuestionsInfo(questionId)
            .publishOn(Schedulers.boundedElastic())
            .mapNotNull(response -> {
                StackOverflowResponseDTO.Question question = response.items().getFirst();
                if (question.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
                    int oldCountQuestions =
                        jpaStackOverflowQuestionRepository.findByLinkId(link.getId()).getAnswerCount();
                    if (oldCountQuestions < question.answerCount()) {
                        updateAnswerCount(link.getId(), question.answerCount());
                        return "Новый ответ на вопрос: %s".formatted(link.getUrl());
                    }
                }
                return null;
            });
    }
}
