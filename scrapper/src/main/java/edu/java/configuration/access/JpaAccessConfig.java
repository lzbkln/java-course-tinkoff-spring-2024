package edu.java.configuration.access;

import edu.java.clients.sites.GitHubClient;
import edu.java.clients.sites.StackOverflowClient;
import edu.java.clients.sites.util.Utils;
import edu.java.repository.jpa.JpaGithubBranchesRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.JpaLinkageRepository;
import edu.java.repository.jpa.JpaStackOverflowQuestionRepository;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.TelegramChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdaterService;
import edu.java.service.jpa.JpaTelegramChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    @Bean
    public LinkUpdater linkUpdater(
        JpaLinkRepository linkRepository,
        JpaLinkageRepository linkageRepository,
        JpaGithubBranchesRepository githubBranchesRepository,
        JpaStackOverflowQuestionRepository stackOverflowQuestionRepository,
        Utils utils,
        GitHubClient gitHubClient,
        StackOverflowClient stackOverflowClient
    ) {
        return new JpaLinkUpdaterService(
            linkRepository,
            linkageRepository,
            githubBranchesRepository,
            stackOverflowQuestionRepository,
            utils,
            gitHubClient,
            stackOverflowClient
        );
    }

    @Bean
    public LinkService linkService(
        JpaLinkRepository linkRepository,
        JpaTelegramChatRepository telegramChatRepository,
        JpaLinkageRepository linkageRepository,
        JpaGithubBranchesRepository githubBranchesRepository,
        JpaStackOverflowQuestionRepository stackOverflowQuestionRepository,
        Utils utils
    ) {
        return new JpaLinkService(
            utils,
            linkRepository,
            linkageRepository,
            githubBranchesRepository,
            telegramChatRepository,
            stackOverflowQuestionRepository
        );
    }

    @Bean
    public TelegramChatService telegramChatService(JpaTelegramChatRepository telegramChatRepository) {
        return new JpaTelegramChatService(telegramChatRepository);
    }
}
