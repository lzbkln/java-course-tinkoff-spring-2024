package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.service.sendUpdates.SendUpdatesService;
import edu.java.service.sendUpdates.client.ClientLinkUpdateSender;
import edu.java.service.sendUpdates.kafka.KafkaLinkUpdateSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class LinkUpdateSenderConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public SendUpdatesService clientLinkUpdateSender(BotClient botClient) {
        return new ClientLinkUpdateSender(botClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public SendUpdatesService kafkaLinkUpdateSender(
        KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate,
        ApplicationConfig config
    ) {
        return new KafkaLinkUpdateSender(kafkaTemplate, config);
    }
}
