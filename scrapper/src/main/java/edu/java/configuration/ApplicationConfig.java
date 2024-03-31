package edu.java.configuration;

import edu.java.configuration.access.AccessType;
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
    GitHubUrl gitHubUrl,
    @NotNull
    BotLink botLink,
    @NotNull
    AccessType databaseAccessType
) {
    public record BotLink(String link) {
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record StackOverflowUrl(String defaultUrl) {
    }

    public record GitHubUrl(String defaultUrl) {
    }
}
