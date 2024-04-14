package edu.java.configuration;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaAdmin admin(ApplicationConfig config) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka().bootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic scrapperUpdatesTopic(ApplicationConfig config) {
        return TopicBuilder
            .name(config.kafka().topic().updatesTopicName())
            .partitions(config.kafka().topic().partitions())
            .replicas(config.kafka().topic().replicas())
            .build();
    }
}
