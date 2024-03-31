package edu.java.service.jooq;

import edu.java.clients.sites.GitHubClient;
import edu.java.clients.sites.StackOverflowClient;
import edu.java.clients.sites.util.Utils;
import edu.java.dto.responses.GithubBranchResponseDTO;
import edu.java.dto.responses.StackOverflowResponseDTO;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageRepository;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.Linkage;
import edu.java.repository.entity.StackOverflowQuestion;
import edu.java.repository.jpa.entity.CommonLink;
import edu.java.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class JooqLinkUpdaterService implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final LinkageRepository linkageTableRepository;
    private final GithubBranchesRepository githubBranchesRepository;
    private final StackOverflowQuestionRepository stackOverflowQuestionRepository;
    private final Utils utils;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public void update(CommonLink link) {
        linkRepository.updateLink(new Link(link.getId(), link.getUrl(), link.getLastUpdatedAt()));
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public List<Link> findLinksToUpdate() {
        return linkRepository.findByLastUpdatedAtBefore(OffsetDateTime.now().minusHours(1));
    }

    @Override
    public List<Long> findTgChatIds(Long linkId) {
        return linkageTableRepository
            .getByLinkId(linkId)
            .stream()
            .map(Linkage::getChatId)
            .collect(Collectors.toList());
    }

    @Override
    public void updateGitBranches(CommonLink link, List<String> branches) {
        githubBranchesRepository.updateData(new GithubBranches(link.getId(), branches));
    }

    @Override
    public void updateAnswerCount(CommonLink link, int answerCount) {
        stackOverflowQuestionRepository.updateData(new StackOverflowQuestion(link.getId(), answerCount));
    }

    @Override
    public Mono<String> getUpdateForGithub(CommonLink link) {
        String path = utils.extractPathForGithub(link.getUrl());
        return gitHubClient.getUserRepository(path)
            .filter(response -> response.pushedAt().isAfter(link.getLastUpdatedAt()))
            .flatMap(response -> getBranchesUpdate(link, utils.getBranches(link.getUrl())));
    }

    private Mono<String> getBranchesUpdate(CommonLink link, Mono<GithubBranchResponseDTO[]> branchesMono) {
        return branchesMono
            .flatMap(branchesArray -> {
                List<String> newBranches = Arrays.stream(branchesArray)
                    .map(GithubBranchResponseDTO::name)
                    .collect(Collectors.toList());
                List<String> tempBranches = new ArrayList<>(newBranches);
                List<String> oldBranches = githubBranchesRepository.getByLinkId(link.getId()).getBranches();
                tempBranches.removeAll(oldBranches);
                if (!tempBranches.isEmpty()) {
                    updateGitBranches(link, newBranches);
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
                        stackOverflowQuestionRepository.getByLinkId(link.getId()).getAnswerCount();
                    if (oldCountQuestions < question.answerCount()) {
                        updateAnswerCount(link, question.answerCount());
                        return "Новый ответ на вопрос: %s".formatted(link.getUrl());
                    }
                }
                return null;
            });
    }
}
