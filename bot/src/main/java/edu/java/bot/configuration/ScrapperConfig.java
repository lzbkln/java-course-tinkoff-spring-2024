package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperChatClient;
import edu.java.bot.clients.ScrapperLinksClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class ScrapperConfig {
    private final ApplicationConfig config;
    private static final String BASE_URL = "http://localhost:8080";

    @Bean
    public ScrapperChatClient createChatClient(Retry retry) {
        return new ScrapperChatClient(getUrl(), retry);
    }

    @Bean
    public ScrapperLinksClient createLinkClient(Retry retry) {
        return new ScrapperLinksClient(getUrl(), retry);
    }

    private String getUrl() {
        String configUrl = config.scrapperLink().link();
        if (configUrl == null || configUrl.isBlank()) {
            return BASE_URL;
        }
        return configUrl;
    }
}
