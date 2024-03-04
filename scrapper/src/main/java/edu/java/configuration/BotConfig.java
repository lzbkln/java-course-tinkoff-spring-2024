package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BotConfig {
    private final ApplicationConfig config;
    private static final String BASE_URL = "http://localhost:8090";

    @Bean
    public BotClient createChatClient() {
        return new BotClient(getUrl());
    }

    private String getUrl() {
        String configUrl = config.botLink().link();
        if (configUrl == null || configUrl.isBlank()) {
            return BASE_URL;
        }
        return configUrl;
    }
}
