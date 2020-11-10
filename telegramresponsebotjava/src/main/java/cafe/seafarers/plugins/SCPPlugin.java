package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Random;

public class SCPPlugin implements BotPlugin {
    private final String[] COMMANDS = {"scp"};
    private final String[] DESCRIPTIONS = {"returns a link to a random scp page."};

    @Override
    public BaseRequest onCommand(Update update) {
        StringBuilder sb = new StringBuilder("http://www.scpwiki.com/scp-");

        // between 001 and 5999
        Random r = new Random();

        int first = r.nextInt(6);
        int second = r.nextInt(10);
        int third = r.nextInt(9) + 1; //SCP dataset starts at 001, thusly the third digit must at least be 1
        int fourth = r.nextInt(10);

        sb.append(first);
        sb.append(second);
        sb.append(third);

        // Coin flip if we enter four digits
        if (r.nextInt(2) == 0){
            sb.append(fourth);
        }

        return new SendMessage(update.message().chat().id(), sb.toString());
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
        return "SCP";
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
        return "Used to find a random SCP and lose your entire day.\n\n" +
                "Use '/scp' to find a random scp.";
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
