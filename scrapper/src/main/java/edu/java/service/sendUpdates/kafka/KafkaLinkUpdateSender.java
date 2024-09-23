package edu.java.service.sendUpdates.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.service.sendUpdates.SendUpdatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j
public class KafkaLinkUpdateSender implements SendUpdatesService {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final ApplicationConfig config;

    @Override
    public void sendUpdate(LinkUpdateRequest update) {
        String updatesTopicName = config.kafka().topic().updatesTopicName();
        log.info("Try sending message to kafka");
        try {
            log.info("Topic name: {}", updatesTopicName);
            kafkaTemplate.send(updatesTopicName, update).whenComplete(
                (sendResult, throwable) -> {
                    if (throwable != null) {
                        log.error("Kafka error: {}", throwable.getMessage());
                    } else {
                        log.info("Successfully sent message to kafka");
                    }
                }
            );
        } catch (Exception e) {
            log.warn("Cant send message to kafka: {}", e.getMessage());
        }
    }
}
