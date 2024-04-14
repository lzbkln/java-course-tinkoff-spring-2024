package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.service.sendUpdates.client.ClientLinkUpdateSender;
import edu.java.service.sendUpdates.kafka.ScrapperQueueProducer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class LinkUpdateSenderConfig {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public ScrapperQueueProducer scrapperQueueProducer(
        NewTopic topic,
        KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate
    ) {
        return new ScrapperQueueProducer(kafkaTemplate, topic);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public ClientLinkUpdateSender clientLinkUpdateSender(BotClient botClient) {
        return new ClientLinkUpdateSender(botClient);
    }
}
