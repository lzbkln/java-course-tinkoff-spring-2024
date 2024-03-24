package edu.java.configuration;

import edu.java.clients.sites.util.Utils;
import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.LinkageRepository;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.TelegramChatRepository;
import edu.java.repository.jooq.JooqGithubBranchesRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqLinkageRepository;
import edu.java.repository.jooq.JooqStackOverflowQuestionRepository;
import edu.java.repository.jooq.JooqTelegramChatRepository;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.TelegramChatService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqLinkUpdaterService;
import edu.java.service.jooq.JooqTelegramChatService;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {
    @Bean
    public LinkRepository linkRepository(DSLContext dslContext) {
        return new JooqLinkRepository(dslContext);
    }

    @Bean
    public LinkageRepository linkageRepository(DSLContext dslContext) {
        return new JooqLinkageRepository(dslContext);
    }

    @Bean
    public TelegramChatRepository telegramChatRepository(DSLContext dslContext) {
        return new JooqTelegramChatRepository(dslContext);
    }

    @Bean
    public GithubBranchesRepository githubBranchesRepository(DSLContext dslContext) {
        return new JooqGithubBranchesRepository(dslContext);
    }

    @Bean
    public StackOverflowQuestionRepository stackOverflowQuestionRepository(DSLContext dslContext) {
        return new JooqStackOverflowQuestionRepository(dslContext);
    }

    @Bean
    public LinkUpdater linkUpdater(LinkRepository linkRepository, LinkageRepository linkageRepository) {
        return new JooqLinkUpdaterService(linkRepository, linkageRepository);
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
        return new JooqLinkService(
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
        return new JooqTelegramChatService(telegramChatRepository);
    }
}
