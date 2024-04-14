package edu.java.configuration;

import edu.java.dto.requests.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaAdmin admin(ApplicationConfig config) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka().bootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic newTopic(ApplicationConfig config) {
        return TopicBuilder
            .name(config.kafka().topic().updatesTopicName())
            .partitions(config.kafka().topic().partitions())
            .replicas(config.kafka().topic().replicas())
            .build();
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate(ProducerFactory<String, LinkUpdateRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
