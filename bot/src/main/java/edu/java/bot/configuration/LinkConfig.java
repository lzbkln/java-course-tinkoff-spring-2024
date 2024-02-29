package edu.java.bot.configuration;

import edu.java.bot.handlers.GithubLinkHandler;
import edu.java.bot.handlers.LinkHandler;
import edu.java.bot.handlers.StackOverflowLinkHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkConfig {
    @Bean
    LinkHandler linkHandler() {
        StackOverflowLinkHandler stackOverFlow = new StackOverflowLinkHandler();
        stackOverFlow.setNextHandler(new GithubLinkHandler());
        return stackOverFlow;
    }
}
