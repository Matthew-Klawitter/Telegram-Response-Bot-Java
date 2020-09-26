package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPoll;

import java.util.HashMap;
import java.util.HashSet;

public class PollingPlugin implements BotPlugin {
    private final String[] COMMANDS = {"suggest", "startvote", "suggestclear"};
    private final String[] DESCRIPTIONS = { "'/suggest [string]' prepares an option for a poll", "'/startvote' Attempts to create a poll based on up to 10 provided options", "'/suggestclear' resets all suggestions"};
    private HashMap<Long, HashSet<String>> channelSuggestions;

    @Override
    public BaseRequest onCommand(Update update) {
        Long channel = update.message().chat().id();
        String message = update.message().text().substring(1);
        String[] commands = message.split(" ");

        switch (commands[0]) {
            case "suggest":
                if (commands.length > 1){
                    String suggestion = message.substring(message.indexOf(" "));

                    if (channelSuggestions.containsKey(channel)){
                        if (channelSuggestions.get(channel).size() < 10){
                            if (channelSuggestions.get(channel).add(suggestion))
                                return new SendMessage(channel,"Polling: Successfully added the suggestion!");
                            else
                                return new SendMessage(channel,"Polling: That suggestion has already been submitted!");
                        }
                        else
                            return new SendMessage(channel,"Polling: Sorry, I can only hold 10 suggestions at a time! Run a poll to start a new one with /startvote or clear suggestions with /suggestclear");
                    }
                    else{
                        HashSet<String> set = new HashSet<String>();
                        set.add(suggestion);
                        channelSuggestions.put(channel, set);
                        return new SendMessage(channel,"Polling: Successfully added the suggestion!");
                    }
                }
                return new SendMessage(update.message().chat().id(), "Polling: You must offer at least one suggestion. Make a suggestion with /suggest [suggestion].");
            case "startvote":
                if (channelSuggestions.containsKey(channel)){
                    if (channelSuggestions.get(channel).size() > 1){
                        SendPoll poll = new SendPoll(channel, "Please select your favorite suggestions", channelSuggestions.get(channel).toArray(new String[channelSuggestions.get(channel).size()]));
                        poll.isAnonymous(false);
                        poll.allowsMultipleAnswers(true);
                        return poll;
                    }
                }
                return new SendMessage(update.message().chat().id(),"Polling: There are not enough suggestions to start the poll!");
            case "suggestclear":
                if (channelSuggestions.containsKey(channel)){
                    channelSuggestions.get(channel).clear();
                    return new SendMessage(channel,"Polling: Successfully cleared suggestions!");
                }
                else
                    return new SendMessage(update.message().chat().id(),"Polling: There are currently no suggestions!");
        }
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
        return "Polling";
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
        return  "Allows for adding up to 10 suggestions to a list and auto creating a poll based off them.\n" +
                "'/suggest [string]' prepares an option for a poll\n" +
                "'/startvote' Attempts to create a poll based on up to 10 provided suggestions\n" +
                "'/suggestclear' resets all suggestions";
    }

    @Override
    public boolean enable() {
        channelSuggestions = new HashMap<Long, HashSet<String>>();
        return false;
    }

    @Override
    public boolean disable() {
        return false;
    }
}
