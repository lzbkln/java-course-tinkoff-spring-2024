package edu.java.bot.configuration;

import edu.java.bot.dto.requests.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaConfig {
    private final ApplicationConfig config;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
            .name(config.kafka().topic().updatesTopicName())
            .partitions(config.kafka().topic().partitions())
            .replicas(config.kafka().topic().replicas())
            .build();
    }

    @Bean
    public NewTopic newDlqTopic() {
        return TopicBuilder
            .name(config.kafka().topic().updatesTopicName() + config.dlqTopicSuffix())
            .partitions(config.kafka().topic().partitions())
            .replicas(config.kafka().topic().replicas())
            .build();
    }

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka().bootstrapServers());

        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
        kafkaAdmin.createOrModifyTopics(newTopic(), newDlqTopic());
        return kafkaAdmin;
    }

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka().bootstrapServers());

        return props;
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, LinkUpdateRequest>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProperties());
    }

    public Map<String, Object> consumerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka().bootstrapServers());
        return props;
    }
}
