package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import edu.java.clients.sites.linkCheckers.UpdateChecker;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.repository.entity.Link;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;
    private final BotClient botClient;
    private final List<UpdateChecker> updateCheckers;

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    @ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
    public void update() {
        List<Link> linksToUpdate = linkUpdater.findLinksToUpdate();

        for (Link link : linksToUpdate) {
            sendUpdate(link);
        }
    }

    private void sendUpdate(Link link) {
        Optional<UpdateChecker> updateChecker = updateCheckers.stream()
            .filter(checker -> checker.isMatched(URI.create(link.getUrl())))
            .findFirst();

        updateChecker.ifPresent(checker -> {
            checker.getUpdate(link)
                .filter(Objects::nonNull)
                .doOnNext(update -> {
                    LinkUpdateRequest request = new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        update,
                        linkUpdater.findTgChatIds(link.getId())
                    );
                    botClient.sendUpdate(request).subscribe();
                })
                .subscribe(
                    update -> {
                    },
                    error -> {
                    },
                    () -> {
                        link.setLastUpdatedAt(OffsetDateTime.now());
                        linkUpdater.update(link);
                    }
                );
        });
    }
}


