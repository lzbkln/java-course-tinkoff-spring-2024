package edu.java.configuration.retry;

import edu.java.configuration.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryException;
import reactor.util.retry.Retry;

@Configuration
@Slf4j
public class RetryConfig {
    private static final String RETRY_MSG = "Perform retry on {}";
    private static final String EXCEPTION_MSG = "Failed to process after max attempts";
    private static final String FAILED_MSG = "Retry failed";

    @Bean
    public HttpStatusRetryPolicy httpStatusRetryPolicy(ApplicationConfig config) {
        return new HttpStatusRetryPolicy(config.retry().retryOnStatuses());
    }

    @Bean
    public Retry retry(ApplicationConfig config, HttpStatusRetryPolicy httpStatusRetryPolicy) {
        RetryType retryType = config.retry().type();
        return switch (retryType) {
            case CONSTANT -> constantRetry(config, httpStatusRetryPolicy);
            case LINEAR -> linearRetry(config, httpStatusRetryPolicy);
            case EXPONENTIAL -> exponentialRetry(config, httpStatusRetryPolicy);
            default -> throw new IllegalArgumentException("Unknown retry type: " + retryType);
        };
    }

    public Retry exponentialRetry(ApplicationConfig config, HttpStatusRetryPolicy httpStatusRetryPolicy) {
        return Retry.backoff(config.retry().maxAttempts(), config.retry().delay())
            .filter(httpStatusRetryPolicy::canRetry)
            .doBeforeRetry(s -> log.info(RETRY_MSG, s.failure().getLocalizedMessage()))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                log.error(FAILED_MSG);
                throw new RetryException(EXCEPTION_MSG);
            });
    }

    public Retry linearRetry(ApplicationConfig config, HttpStatusRetryPolicy httpStatusRetryPolicy) {
        return new LinearRetry(
            config.retry().maxAttempts(),
            config.retry().delay(),
            httpStatusRetryPolicy
        );
    }

    public Retry constantRetry(ApplicationConfig config, HttpStatusRetryPolicy httpStatusRetryPolicy) {
        return Retry.fixedDelay(config.retry().maxAttempts(), config.retry().delay())
            .filter(httpStatusRetryPolicy::canRetry)
            .doBeforeRetry(s -> log.info(RETRY_MSG, s.failure().getLocalizedMessage()))
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                log.error(FAILED_MSG);
                throw new RetryException(EXCEPTION_MSG);
            });
    }
}
