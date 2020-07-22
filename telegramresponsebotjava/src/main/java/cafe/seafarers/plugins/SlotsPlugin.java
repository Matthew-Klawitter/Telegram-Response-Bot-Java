package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.currencies.BankManager;

import java.util.HashMap;
import java.util.Random;

public class SlotsPlugin implements BotPlugin {
    private final int COST = 3;
    private final String[] COMMANDS = {"slots"};
    private final String[] DESCRIPTIONS = {String.format("spend $%d to play roulette", COST)};

    private String getCanonicalName(User user) {
        if (user.username() == null) {
            return user.firstName();
        } else {
            return user.username();
        }
    }

    @Override
    public BaseRequest onCommand(Update update) {
        String user = getCanonicalName(update.message().from());

        if (BankManager.charge(user, COST)){
            SlotGame g = new SlotGame();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s inserts %d coins and pulls the crank...", user, COST));
            sb.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
            sb.append("The slots display the following:").append(System.getProperty("line.separator"));
            sb.append(g.getRolledPattern()[0]).append(System.getProperty("line.separator"));
            sb.append(g.getRolledPattern()[1]).append(System.getProperty("line.separator"));
            sb.append(g.getRolledPattern()[2]).append(System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));

            if (g.payout > 0){
                sb.append("Wow, you're a big winner!").append(System.getProperty("line.separator"));
                sb.append("You won: $").append(g.payout);
                BankManager.deposit(user, g.payout);
            }
            else {
                sb.append("Not a winner! Sad! Play again? /slots");
            }

            return new SendMessage(update.message().chat().id(), sb.toString());
        }
        return new SendMessage(update.message().chat().id(), String.format("Slots: You require %d points to play slots.", COST));
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
        return "Slots";
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
        return String.format("Available commands for Slots:\n'slots' spend $%d to roll the slots.", COST);
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

    class SlotGame {
        private HashMap<String, Integer> validPatterns;
        private String[] rolledPattern;
        private int payout;

        public SlotGame(){
            validPatterns = configurePatternValues();
            rolledPattern = rollSlots();
            payout = checkWinnings(rolledPattern, validPatterns);
        }

        private HashMap<String, Integer> configurePatternValues(){
            HashMap<String, Integer> p = new HashMap<>();
            p.put("777", 100);
            p.put("$$$", 50);
            p.put("@@@", 25);
            p.put("***", 10);
            p.put("xxx", 5);

            return p;
        }

        private String[] rollSlots(){
            Random r = new Random();
            String[] p = new String[3];

            for (int i = 0; i < 3; i++){
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 3; j++){
                    int roll = r.nextInt(100) + 1;

                    if (95 < roll & roll <= 100){ // 5%
                        sb.append("7");
                    }
                    else if (80 < roll & roll <= 95){ // 15%
                        sb.append("$");
                    }
                    else if (60 < roll && roll <= 80){ // 20%
                        sb.append("@");
                    }
                    else if (35 < roll && roll <= 60){ // 25%
                        sb.append("*");
                    }
                    else { // 35%
                        sb.append("x");
                    }
                }

                p[i] = sb.toString();
            }

            return p;
        }

        private int checkWinnings(String[] pattern, HashMap<String, Integer> valid){
            int totalWinnings = 0;

            for (int i = 0; i < pattern.length; i++){
                if (valid.containsKey(pattern[i])){
                    totalWinnings += valid.get(pattern[i]);
                }
            }

            return totalWinnings;
        }

        public String[] getRolledPattern() {
            return rolledPattern;
        }

        public int getPayout() {
            return payout;
        }
    }
}
