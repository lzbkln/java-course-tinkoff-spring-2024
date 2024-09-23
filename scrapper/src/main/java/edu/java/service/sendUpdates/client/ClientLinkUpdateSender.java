package edu.java.service.sendUpdates.client;

import edu.java.clients.bot.BotClient;
import edu.java.dto.requests.LinkUpdateRequest;
import edu.java.service.sendUpdates.SendUpdatesService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientLinkUpdateSender implements SendUpdatesService {
    private final BotClient botClient;

    @Override
    public void sendUpdate(LinkUpdateRequest request) {
        botClient.sendUpdate(request).subscribe();
    }
}
