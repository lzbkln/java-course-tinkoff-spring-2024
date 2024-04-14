package edu.java.configuration;

import edu.java.configuration.access.AccessType;
import edu.java.configuration.retry.HttpStatusCodes;
import edu.java.configuration.retry.RetryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Set;
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
    AccessType databaseAccessType,
    @NotNull
    Retry retry,
    @NotNull
    Kafka kafka,
    @NotNull
    Boolean useQueue
) {
    public record BotLink(String link) {
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record StackOverflowUrl(String defaultUrl) {
    }

    public record GitHubUrl(String defaultUrl) {
    }

    public record Retry(@NotNull RetryType type, @NotNull Duration delay, @NotNull Integer maxAttempts,
                        Set<HttpStatusCodes> retryOnStatuses) {
    }

    public record Kafka(List<String> bootstrapServers, Topic topic) {
        public record Topic(@NotBlank String updatesTopicName, Integer partitions, Integer replicas) {
        }
    }
}
