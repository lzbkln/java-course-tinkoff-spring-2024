package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperChatClient;
import edu.java.bot.clients.ScrapperLinksClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapperConfig {
    private static final String BASE_URL = "http://localhost:8080";

    @Bean
    public ScrapperChatClient createChatClient() {
        return new ScrapperChatClient(BASE_URL);
    }

    @Bean
    public ScrapperLinksClient createLinkClient() {
        return new ScrapperLinksClient(BASE_URL);
    }
}
