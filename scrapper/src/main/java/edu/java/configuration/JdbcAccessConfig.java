package edu.java.configuration;

import edu.java.clients.sites.util.Utils;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageRepository;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.TelegramChatRepository;
import edu.java.repository.jdbc.JdbcGithubBranchesRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.jdbc.JdbcLinkageRepository;
import edu.java.repository.jdbc.JdbcStackOverflowQuestionRepository;
import edu.java.repository.jdbc.JdbcTelegramChatRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.TelegramChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcLinkUpdaterService;
import edu.java.service.jdbc.JdbcTelegramChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    @Bean
    public LinkRepository linkRepository(JdbcClient jdbcClient) {
        return new JdbcLinkRepository(jdbcClient);
    }

    @Bean
    public LinkageRepository linkageRepository(JdbcClient jdbcClient) {
        return new JdbcLinkageRepository(jdbcClient);
    }

    @Bean
    public TelegramChatRepository telegramChatRepository(JdbcClient jdbcClient) {
        return new JdbcTelegramChatRepository(jdbcClient);
    }

    @Bean
    public GithubBranchesRepository githubBranchesRepository(JdbcClient jdbcClient) {
        return new JdbcGithubBranchesRepository(jdbcClient);
    }

    @Bean
    public StackOverflowQuestionRepository stackOverflowQuestionRepository(JdbcClient jdbcClient) {
        return new JdbcStackOverflowQuestionRepository(jdbcClient);
    }

    @Bean
    public LinkUpdater linkUpdater(LinkRepository linkRepository, LinkageRepository linkageRepository) {
        return new JdbcLinkUpdaterService(linkRepository, linkageRepository);
    }

    @Bean
    public LinkService linkService(
        LinkRepository linkRepository,
        TelegramChatRepository telegramChatRepository,
        LinkageRepository linkageRepository,
        GithubBranchesRepository githubBranchesRepository,
        StackOverflowQuestionRepository stackOverflowQuestionRepository,
        Utils utils
    ) {
        return new JdbcLinkService(
            linkRepository,
            telegramChatRepository,
            linkageRepository,
            githubBranchesRepository,
            stackOverflowQuestionRepository,
            utils
        );
    }

    @Bean
    public TelegramChatService telegramChatService(TelegramChatRepository telegramChatRepository) {
        return new JdbcTelegramChatService(telegramChatRepository);
    }
}
