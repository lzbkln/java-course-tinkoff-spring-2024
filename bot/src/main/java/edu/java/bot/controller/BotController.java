package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdate;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot/api")
public class BotController {
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody LinkUpdate linkUpdate) {
        LOGGER.info("New linkUpdate");
        return ResponseEntity.ok().build();
    }
}
