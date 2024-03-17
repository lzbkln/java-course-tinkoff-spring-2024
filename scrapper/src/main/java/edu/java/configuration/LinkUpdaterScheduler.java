package edu.java.configuration;

import edu.java.clients.bot.BotClient;
import edu.java.clients.sites.linkCheckers.UpdateChecker;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.repository.entity.Link;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
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
        for (UpdateChecker updateChecker : updateCheckers) {
            if (updateChecker.isMatched(URI.create(link.getUrl()))) {
                updateChecker.getUpdate(link)
                    .subscribe(update -> {
                        if (update != null) {
                            LinkUpdateRequest request = new LinkUpdateRequest(
                                link.getId(),
                                link.getUrl(),
                                update,
                                linkUpdater.findTgChatIds(link.getId())
                            );
                            botClient.sendUpdate(request).subscribe();
                        }
                        link.setLastUpdatedAt(OffsetDateTime.now());
                        linkUpdater.update(link);
                    });
                break;
            }
        }
    }
}


