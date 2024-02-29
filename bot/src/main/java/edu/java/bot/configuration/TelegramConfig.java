package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.service.CommandService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class TelegramConfig {

    public CommandService commandService;

    @Bean
    TelegramBot createBot(ApplicationConfig config) {
        TelegramBot bot = new TelegramBot(config.telegramToken());

        BotCommand[] botCommands = commandService.getAllBotCommands();

        bot.execute(new SetMyCommands(botCommands));

        return bot;
    }
}
