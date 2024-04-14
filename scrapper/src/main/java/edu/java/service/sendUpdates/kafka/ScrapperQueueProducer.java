package edu.java.service.sendUpdates.kafka;

import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.service.sendUpdates.SendUpdatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer implements SendUpdatesService {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final NewTopic topic;

    @Override
    public void sendUpdate(LinkUpdateRequest update) {
        String updatesTopicName = topic.name();
        log.warn("Try sending message to kafka");
        try {
            log.warn("Topic name: {}", updatesTopicName);
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
