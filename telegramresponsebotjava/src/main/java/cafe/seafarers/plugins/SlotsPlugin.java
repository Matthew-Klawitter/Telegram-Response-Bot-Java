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
    private final int COST = 5;
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
            sb.append(System.lineSeparator()).append(System.lineSeparator());
            sb.append("The slots display the following:").append(System.lineSeparator());
            sb.append(g.toString()).append(System.lineSeparator());
            sb.append(System.lineSeparator());

            if (g.payout > 0){
                sb.append("Wow, you're a big winner!").append(System.lineSeparator());
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
        private char[][] rolledPatterns;
        private int payout;

        public SlotGame(){
            validPatterns = configurePatternValues();
            rolledPatterns = rollSlots();
            payout = checkCrossWinnings(rolledPatterns, validPatterns) +
                            checkCrossWinnings(getTransposedSquareMatrix(rolledPatterns), validPatterns) +
                            checkDiagonalWinnings(rolledPatterns, validPatterns);
        }

        private HashMap<String, Integer> configurePatternValues(){
            HashMap<String, Integer> p = new HashMap<>();
            p.put("777", 80);
            p.put("$$$", 35);
            p.put("@@@", 15);
            p.put("***", 10);
            p.put("xxx", 5);

            return p;
        }

        private char[][] rollSlots(){
            Random r = new Random();
            char[][] p = new char[3][3];

            for (int i = 0; i < p.length; i++){
                for (int j = 0; j < p[i].length; j++){
                    int roll = r.nextInt(100) + 1;

                    if (95 < roll & roll <= 100){ // 5%
                        p[i][j] = '7';
                    }
                    else if (80 < roll & roll <= 95){ // 15%
                        p[i][j] = '$';
                    }
                    else if (60 < roll && roll <= 80){ // 20%
                        p[i][j] = '@';
                    }
                    else if (35 < roll && roll <= 60){ // 25%
                        p[i][j] = '*';
                    }
                    else { // 35%
                        p[i][j] = 'x';
                    }
                }
            }

            return p;
        }

        private int checkCrossWinnings(char[][] pattern, HashMap<String, Integer> valid){
            int winnings = 0;

            for (int row = 0; row < pattern.length; row++){
                StringBuilder sb = new StringBuilder();
                for (int col = 0; col < pattern[row].length; col++){
                    sb.append(pattern[row][col]);
                }

                String rolledPattern = sb.toString();
                if (valid.containsKey(rolledPattern)){
                    winnings += valid.get(rolledPattern);
                }
            }

            return winnings;
        }

        private int checkDiagonalWinnings(char[][] pattern, HashMap<String, Integer> valid){
            int winnings = 0;

            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < pattern.length; row++){
                sb.append(pattern[row][row]);
            }

            if (valid.containsKey(sb.toString())){
                winnings += valid.get(sb.toString());
            }

            sb.delete(0, sb.length());
            for (int row = 0; row < pattern.length; row++){
                sb.append(pattern[row][pattern.length - row - 1]);
            }

            if (valid.containsKey(sb.toString())){
                winnings += valid.get(sb.toString());
            }

            return winnings;
        }

        private char[][] getTransposedSquareMatrix(char[][] pattern){
            char[][] transposedMatrix = new char[pattern[0].length][pattern.length];

            for (int i = 0; i < pattern[0].length; i++){
                for (int j = 0; j < pattern.length; j++){
                    transposedMatrix[i][j] = pattern[j][i];
                }
            }

            return transposedMatrix;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < rolledPatterns.length; i++){
                for (int j = 0; j < rolledPatterns.length; j++){
                    sb.append(rolledPatterns[i][j]);
                }
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }

        public int getPayout() {
            return payout;
        }
    }
}
