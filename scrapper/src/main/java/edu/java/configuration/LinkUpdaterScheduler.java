package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import edu.java.clients.sites.linkCheckers.UpdateChecker;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.repository.entity.Link;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    public static final Logger LOGGER = LogManager.getLogger();
    private final LinkUpdater linkUpdater;
    private final BotClient botClient;
    private final List<UpdateChecker> updateCheckers;

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    @ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
    public void update() {
        List<Link> linksToUpdate = linkUpdater.findLinksToUpdate();

        for (Link link : linksToUpdate) {
            Mono<String> updateMono = updateCheckers.stream()
                .filter(checker -> checker.isMatched(URI.create(link.getUrl())))
                .findFirst()
                .map(checker -> checker.getUpdate(link))
                .orElse(Mono.empty());

            LOGGER.info(updateMono.subscribe());

            updateMono.subscribe(update -> {
                if (update != null) {
                    LinkUpdateRequest request = new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        update,
                        linkUpdater.findTgChatIds(link.getId())
                    );
                    botClient.sendUpdate(request).subscribe();
                }
                link.setLastUpdatedAt(LocalDateTime.now());
                linkUpdater.update(link);
            });
        }
    }
}
