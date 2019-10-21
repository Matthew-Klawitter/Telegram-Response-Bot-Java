package cafe.seafarers.plugins;

import cafe.seafarers.plugins.gachacreator.Gacha;
import cafe.seafarers.plugins.gachacreator.GachaManager;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class GachaFighterPlugin implements BotPlugin {
    private final String[] COMMANDS = {"gsummon", "glist", "gview"};
    private GachaManager gm;

    @Override
    public BaseRequest onCommand(Update update) {
        Long channelID = update.message().chat().id();
        String message = update.message().text().substring(1);
        String command = message.split("[ @]")[0].toLowerCase();
        String args = message.substring(command.length()).trim();
        String user = update.message().from().username();

        switch (command){
            case ("gsummon"):
                return new SendMessage(channelID, gm.summonGacha(user));
            case ("glist"):
                return new SendMessage(channelID, gm.listOwned(user));
            case ("gview"):
                try {
                    return new SendMessage(channelID, gm.inspectGacha(user, Integer.parseInt(args)));
                } catch (NumberFormatException e){
                    e.printStackTrace();
                    return new SendMessage(channelID, "GF: The specified argument must be an integer!");
                }
        }
        return new SendMessage(channelID, "GF: Invalid specified command. Try /help GachaFighters");
    }

    @Override
    public String[] getCommands() {
        return COMMANDS;
    }

    @Override
    public String getName() {
        return "GachaFighters";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "V0.6";
    }

    @Override
    public String getHelp() {
        return "GF: Available Commands:\n '/gsummon' to summon a gacha for 100 credits\n'/glist' to view owned gacha\n'/gview [id]' to view stats of a gacha";
    }

    @Override
    public boolean enable() {
        gm = new GachaManager();
        return true;
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
