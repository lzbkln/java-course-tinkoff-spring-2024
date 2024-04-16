package edu.java.bot.service;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.requests.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaUpdatesListener {
    private final LinkUpdateService linkUpdateService;
    private final ApplicationConfig config;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @KafkaListener(topics = "${app.kafka.topic.updates-topic-name}",
                   groupId = "group",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listen(LinkUpdateRequest request) {
        try {
            linkUpdateService.sendMessage(request.tgChatIds(), request.description());
            log.info("Get update {}", request);
        } catch (Exception e) {
            kafkaTemplate.send(config.kafka().topic().updatesTopicName() + config.dlqTopicSuffix(), request);
            log.info("Error. Send to dlq {}", request);
        }
    }
}
