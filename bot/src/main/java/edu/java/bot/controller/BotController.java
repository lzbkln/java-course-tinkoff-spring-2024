package edu.java.bot.controller;

import edu.java.bot.dto.requests.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bot/update")
public class BotController implements BotControllerAnnotations {
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping
    public ResponseEntity<Void> update(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        LOGGER.info("New linkUpdateRequest {}", linkUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
