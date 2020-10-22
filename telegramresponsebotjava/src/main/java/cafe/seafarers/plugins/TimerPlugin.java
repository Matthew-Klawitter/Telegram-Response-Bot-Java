package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public class TimerPlugin implements BotPlugin {
    private static final String[] COMMANDS = { "timer" };
    private static final String[] DESCRIPTIONS = { "'/timer [set,view,delete] [name] [date]' to set, view, and delete a timer." };

    @Override
    public BaseRequest onCommand(Update update) {
        return null;
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
        return "Timer";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "V0.1";
    }

    @Override
    public String getHelp() {
        return "Helps you schedule countdowns between different dates and times.";
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
