package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    private static final String BASE_URL = "http://localhost:8090";

    @Bean
    public BotClient createChatClient() {
        return new BotClient(BASE_URL);
    }
}
