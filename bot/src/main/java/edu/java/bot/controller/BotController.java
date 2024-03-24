package edu.java.bot.controller;

import edu.java.bot.dto.requests.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bot/update")
public class BotController implements BotControllerAnnotations {
    private final LinkUpdateService linkUpdateService;

    @PostMapping
    public void update(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        linkUpdateService.sendMessage(linkUpdateRequest);
    }
}
