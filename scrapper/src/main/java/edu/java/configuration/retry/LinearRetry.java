package edu.java.configuration.retry;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.retry.RetryException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class LinearRetry extends Retry {

    private final int maxAttempts;
    private final Duration delay;
    private final HttpStatusRetryPolicy httpStatusRetryPolicy;
    private static final String EXCEPTION_MSG = "Failed to process after max attempts";

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignals) {
        return retrySignals.flatMap(this::getRetry);
    }

    public Mono<Long> getRetry(RetrySignal rs) {
        if (!httpStatusRetryPolicy.canRetry(rs.failure())) {
            return Mono.error(rs.failure());
        }
        if (rs.totalRetries() < maxAttempts) {
            Duration newDelay = delay.multipliedBy(rs.totalRetries() + 1);
            return Mono.delay(newDelay).thenReturn(rs.totalRetries());
        } else {
            return Mono.error(new RetryException(EXCEPTION_MSG));
        }
    }
}



