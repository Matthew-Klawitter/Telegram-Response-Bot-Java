package cafe.seafarers.plugins;

import cafe.seafarers.currencies.BankManager;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class WikiPlugin implements BotPlugin {
    private final String[] COMMANDS = {"wiki", "wikirand"};
    private final String[] DESCRIPTIONS = {"/wiki <search quarry>", "/wikirand"};

    @Override
    public BaseRequest onCommand(Update update) {
        String message = update.message().text().substring(1);
        String[] command = message.split(" ");
        message = message.replace(' ', '_');

        if (command.length > 1){
            StringBuilder sb = new StringBuilder("https://en.wikipedia.org/wiki/");
            sb.append(message.substring(message.indexOf("_") + 1));
            return new SendMessage(update.message().chat().id(), sb.toString());
        }
        else if (message.equals("wikirand")) {
            // wiki url for random page, need redirect https://en.wikipedia.org/wiki/Special:Random
            try {
                URLConnection con = new URL("https://en.wikipedia.org/wiki/Special:Random").openConnection();
                con.connect();
                InputStream is = con.getInputStream();
                is.close();
                return new SendMessage(update.message().chat().id(), con.getURL().toString());
            } catch (MalformedURLException e){
                e.printStackTrace();
                return new SendMessage(update.message().chat().id(), "Wiki: An unknown error occurred when parsing the url.");
            } catch (IOException e) {
                e.printStackTrace();
                return new SendMessage(update.message().chat().id(), "Wiki: An unknown error occurred when opening the url connection.");
            }
        }

        return new SendMessage(update.message().chat().id(), "Wiki: Please enter something to search for after typing '/wiki'");
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
        return "Wiki";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "V1.1";
    }

    @Override
    public String getHelp() {
        return "Used to search for helpful information on a variety of topics\n\n" +
                "Use '/wiki <quarry>' to find a wiki page.\n" +
                "Use '/wikirand' to discover a random wiki page.";
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
