package cafe.seafarers.plugins;

import cafe.seafarers.plugins.gachacreator.Gacha;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class GachaFighterPlugin implements BotPlugin {
    private final String[] COMMANDS = {"gsummon"};

    @Override
    public BaseRequest onCommand(Update update) {
        Long channelID = update.message().chat().id();
        String message = update.message().text().substring(1);
        String command = message.split("[ @]")[0].toLowerCase();
        String args = message.substring(command.length()).trim();

        try {
            int rarity = Integer.parseInt(args);
            if (rarity >= 5 || rarity < 0){
                return new SendMessage(channelID, "GF: Invalid rarity, must be between 0 and 4! Use /gsummon [0-4] to summon a testing gacha (will not be saved)");
            }

            Gacha g = new Gacha(Integer.parseInt(args));
            return new SendMessage(channelID, "GF: You summoned...\n" + g.toString());
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }

        return new SendMessage(channelID, "GF: Invalid use! Use /gsummon [0-4] to summon a testing gacha (will not be saved)");
    }

    @Override
    public String[] getCommands() {
        return COMMANDS;
    }

    @Override
    public String getName() {
        return "Gacha Fighters";
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
        return "GF: Use /gsummon to summon a testing gacha (will not be saved)";
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public boolean disable() {
        return false;
    }

    @Override
    public BaseRequest onMessage(Update update) {
        return null;
    }

    @Override
    public boolean hasMessageAccess() {
        return false;
    }

    @Override
    public BaseRequest periodicUpdate() {
        return null;
    }
}
