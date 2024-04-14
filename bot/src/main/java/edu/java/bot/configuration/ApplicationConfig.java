package edu.java.bot.configuration;

import edu.java.bot.configuration.retry.HttpStatusCodes;
import edu.java.bot.configuration.retry.RetryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull
    ScrapperLink scrapperLink,
    @NotNull
    Retry retry
) {
    public record ScrapperLink(String link) {
    }

    public record Retry(@NotNull RetryType type, @NotNull Duration delay, @NotNull Integer maxAttempts,
                        Set<HttpStatusCodes> retryOnStatuses) {
    }
}
