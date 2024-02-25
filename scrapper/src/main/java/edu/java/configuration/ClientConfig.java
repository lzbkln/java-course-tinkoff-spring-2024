package edu.java.configuration;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig config;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient(getGitHubBaseUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(getStackOverflowBaseUrl());
    }

    private String getGitHubBaseUrl() {
        String defaultUrl = config.gitHubUrl().defaultUrl();
        String baseUrl = config.gitHubUrl().baseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            return defaultUrl;
        }
        return baseUrl;
    }

    private String getStackOverflowBaseUrl() {
        String defaultUrl = config.stackOverflowUrl().defaultUrl();
        String baseUrl = config.stackOverflowUrl().baseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            return defaultUrl;
        }
        return baseUrl;
    }
}
