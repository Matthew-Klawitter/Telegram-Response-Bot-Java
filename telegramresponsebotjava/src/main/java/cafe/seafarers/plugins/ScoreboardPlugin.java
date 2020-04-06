package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.currencies.BankManager;

import java.lang.reflect.Array;
import java.util.*;

public class ScoreboardPlugin implements BotPlugin {
    private final String[] COMMANDS = { "scoreboard"};
    private final String[] DESCRIPTIONS = { "views the current point scoreboard"};

    @Override
    public BaseRequest onCommand(Update update) {
        HashMap<String, Integer> accounts = BankManager.getAccountContents();
        Iterator<String> itr = accounts.keySet().iterator();
        List<ScoreboardWrapper> wrappedAccounts = new ArrayList<ScoreboardWrapper>();

        while (itr.hasNext()){
            String key = itr.next();
            int value = accounts.get(key);

            wrappedAccounts.add(new ScoreboardWrapper(key, value));
        }

        Collections.sort(wrappedAccounts, new ScoreboardWrapper());

        StringBuilder sb = new StringBuilder("|Scoreboard Standings|");
        sb.append(System.getProperty("line.separator"));

        int count = 1;
        for (ScoreboardWrapper w: wrappedAccounts) {
            sb.append(count).append(": ").append(w.username).append(" | ").append(w.amount);
            sb.append(System.getProperty("line.separator"));
            count++;
        }

        return new SendMessage(update.message().chat().id(), sb.toString());
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
    public String getName() {
        return "Scoreboard";
    }

    @Override
    public String getAuthor() {
        return "Matthew Klawitter";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getHelp() {
        return "Available commands for Scoreboard:\n'scoreboard' to view current point totals";
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

    class ScoreboardWrapper implements Comparator<ScoreboardWrapper> {
        private String username;
        private int amount;

        public ScoreboardWrapper() {

        }

        public ScoreboardWrapper(String username, int amount){
            this.username = username;
            this.amount = amount;
        }

        @Override
        public int compare(ScoreboardWrapper o1, ScoreboardWrapper o2) {
            return o2.amount - o1.amount;
        }
    }
}
