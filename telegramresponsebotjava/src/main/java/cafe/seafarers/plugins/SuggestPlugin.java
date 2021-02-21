package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.Random;

public class SuggestPlugin implements BotPlugin {
    private final String[] COMMANDS = {"sug", "sugview", "sugclear"};
    private final String[] DESCRIPTIONS = { "/sug [choice]", "/sugview", "sugclear"};
    private ArrayList<String> suggestions;

    @Override
    public BaseRequest onCommand(Update update) {
        String message = update.message().text().substring(1);
        String command = message.split("[ @]")[0];

        switch(command){
            case "sug":
                String arg = message.substring(message.indexOf(' ') + 1);
                suggestions.add(arg);
                return new SendMessage(update.message().chat().id(),String.format("Suggest: Successfully suggested %s.", arg));
            case "sugview":
                StringBuilder sb = new StringBuilder("Current suggestions:\n");
                for (String s : suggestions){
                    sb.append(s);
                    sb.append("\n");
                }
                return new SendMessage(update.message().chat().id(), sb.toString());
            case "sugclear":
                suggestions.clear();
                return new SendMessage(update.message().chat().id(),"Suggest: Successfully cleared suggestions.");
        }
        return new SendMessage(update.message().chat().id(), "Suggest: Invalid command");
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
        return "Suggestion Plugin";
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
        return "Used to collectively suggest things:\n'/sug [choice]' to make a suggestion,\n'/sugview' to view suggestions,\n'/sugclear' to clear suggestions";
    }

    @Override
    public boolean enable() {
        suggestions = new ArrayList<String>();
        return true;
    }

    @Override
    public boolean disable() {
        suggestions.clear();
        return true;
    }
}
