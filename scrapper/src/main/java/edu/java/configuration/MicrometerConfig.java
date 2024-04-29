package edu.java.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MicrometerConfig {
    private final MeterRegistry meterRegistry;
    private final ApplicationConfig applicationConfig;

    @Bean
    public Counter processedLinkUpdatesMetric() {
        return meterRegistry.counter(applicationConfig.nameOfMetric());
    }
}
