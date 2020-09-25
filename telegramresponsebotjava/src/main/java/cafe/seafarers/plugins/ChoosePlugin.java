package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Random;

public class ChoosePlugin implements BotPlugin {
    private final String[] COMMANDS = {"choose"};
    private final String[] DESCRIPTIONS = { "/choose [option 1] [option 2] ..."};

    @Override
    public BaseRequest onCommand(Update update) {
        String message = update.message().text().substring(1);
        message = message.replace("\n", " ");
        String[] command = message.split(" ");

        if (command.length > 1){
            Random r = new Random();
            return new SendMessage(update.message().chat().id(), "I choose: " + command[r.nextInt(command.length - 1) + 1]);
        }

        return new SendMessage(update.message().chat().id(), "Choice: I need to be given at least one decision to make!");
    }

    @Override
    public BaseRequest onMessage(Update update) {
        return null;
    }

    @Override
    public BotCommand[] getCommands() {
        BotCommand[] botCommands = new BotCommand[COMMANDS.length];
        for (int i = 0; i < botCommands.length; i++) {
            botCommands[i] = new BotCommand(COMMANDS[i], DESCRIPTIONS[i]);
        }
        return botCommands;
    }

    @Override
    public boolean hasMessageAccess() {
        return false;
    }

    @Override
    public BaseRequest periodicUpdate() {
        return null;
    }

    @Override
    public String getName() {
        return "Choice Plugin";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "V1.0";
    }

    @Override
    public String getHelp() {
        return "Used to make a random choice: '/choose [option1] [option2] ..'";
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public boolean disable() {
        return false;
    }
}
