package edu.java.bot.service;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CommandService {
    private Map<String, Command> allCommands;
    private static final String NO_SUCH_COMMAND = "This command doesn't exist, try /help";

    public CommandService(List<Command> commandList) {
        fillAllCommands(commandList);
    }

    private void fillAllCommands(List<Command> commandList) {
        allCommands = HashMap.newHashMap(commandList.size());
        commandList.forEach(command -> allCommands.put(command.nameCommand(), command));
    }

    public String doCommand(Update update) {
        String userCommand = update.message().text().split(" ")[0];
        for (String command : allCommands.keySet()) {
            if (command.equals(userCommand) && userCommand.startsWith("/")) {
                return allCommands.get(command).execute(update);
            }
        }
        return NO_SUCH_COMMAND;
    }

    public BotCommand[] getAllBotCommands() {
        return allCommands.values().stream()
            .map(Command::getBotCommand).toArray(BotCommand[]::new);
    }

}
