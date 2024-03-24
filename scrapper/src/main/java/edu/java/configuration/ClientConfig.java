package edu.java.configuration;

import edu.java.clients.sites.GitHubClient;
import edu.java.clients.sites.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig config;
    private static final String STACKOVERFLOW_BASE_URL = "https://api.stackexchange.com/2.3";
    private static final String GITHUB_BASE_URL = "https://api.github.com";

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient(getGitHubBaseUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(getStackOverflowBaseUrl());
    }

    private String getGitHubBaseUrl() {
        String configUrl = config.gitHubUrl().defaultUrl();
        if (configUrl == null || configUrl.isBlank()) {
            return GITHUB_BASE_URL;
        }
        return configUrl;
    }

    private String getStackOverflowBaseUrl() {
        String configUrl = config.stackOverflowUrl().defaultUrl();
        if (configUrl == null || configUrl.isBlank()) {
            return STACKOVERFLOW_BASE_URL;
        }
        return configUrl;
    }
}
