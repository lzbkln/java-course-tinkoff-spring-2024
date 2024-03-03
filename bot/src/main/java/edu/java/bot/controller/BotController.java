package edu.java.bot.controller;

import edu.java.bot.dto.requests.LinkUpdateRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot/update")
public class BotController {
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping
    public void update(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        LOGGER.info("New linkUpdateRequest {}", linkUpdateRequest);
    }
}
