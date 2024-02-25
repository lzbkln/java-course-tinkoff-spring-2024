package edu.java.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdaterScheduler {
    public static final Logger LOGGER = LogManager.getLogger();

    @Scheduled(fixedDelayString = "PT${app.scheduler.interval}")
    public void update() {
        LOGGER.info("Searching for updates..");
    }
}
