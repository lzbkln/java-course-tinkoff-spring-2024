package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull
    StackOverflowUrl stackOverflowUrl,
    @NotNull
    GitHubUrl gitHubUrl
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record StackOverflowUrl(String defaultUrl, String baseUrl) {
    }

    public record GitHubUrl(String defaultUrl, String baseUrl) {
    }
}
