package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Date;

public class WaterPlugin implements BotPlugin {
    private final int OZ_PER_HALFHOUR = 12;
    private final int EST_OZ_PER_DAY = 96;
    private final String[] COMMANDS = {"water", "watercalc"};
    private final String[] DESCRIPTIONS = {"Provides a rough estimate of what your current water intake should be.",
            "Gives water intake recommendations - '/watercalc <weight in lbs> <minutes of activity>'"};

    @Override
    public BaseRequest onCommand(Update update) {
        // (weight * (2/3)oz) + (minutes of activity * 12oz) = water per day in oz
        // convert to cups by multiplaying by .125
        String message = update.message().text().substring(1);
        String command = message.split("[ @]")[0];

        switch (command) {
            case "water":
                // assuming 8-12 or 16 hours
                LocalDateTime currentTime = LocalDateTime.now();
                int hour = currentTime.getHour();

                if (hour < 6) {
                    int diff = 6 - hour;
                    return new SendMessage(update.message().chat().id(), "WaterCalc: This bot is still asleep, we'll" +
                            " get tracking in " + diff + " hour(s).");
                }
                else if (hour >= 22) {
                    return new SendMessage(update.message().chat().id(), "WaterCalc: At this point you should be at" +
                            " the goal of 96 Ounces or 12 Cups of water. If not, you may want to drink just a little before" +
                            " you get to bed.");
                }
                else {
                    int consumed = hour - 6 + 1;
                    float totalWaterOz = consumed * ((float) EST_OZ_PER_DAY / 16);
                    float totalWaterCups = consumed * (((float) EST_OZ_PER_DAY / 16) * .125f);
                    BigDecimal oz = new BigDecimal(totalWaterOz).setScale(1, RoundingMode.HALF_UP);
                    BigDecimal cups = new BigDecimal(totalWaterCups).setScale(1, RoundingMode.HALF_UP);

                    StringBuilder sb = new StringBuilder("WaterCalc: By this point your goal should've been to drink either...");
                    sb.append(System.lineSeparator());
                    sb.append(oz.toString()).append(" Ounces or").append(System.lineSeparator());
                    sb.append(cups.toString()).append(" Cups of water").append(System.lineSeparator()).append(System.lineSeparator());
                    sb.append("The current set daily goal is ").append(EST_OZ_PER_DAY).append(" Ounces, or ").append(EST_OZ_PER_DAY/16).append(" Cups of water.");

                    return new SendMessage(update.message().chat().id(), sb.toString());
                }
            case "watercalc":
                String[] args = message.substring(command.length() + 1).split(" ");

                if (args.length == 2) {
                    try {
                        float weight = Float.parseFloat(args[0]);
                        int activityMins = Integer.parseInt(args[1]);

                        // calculate out measurements
                        float totalWaterOz = (weight * ((float)2/3)) + (((float) activityMins/30) * OZ_PER_HALFHOUR);
                        float totalWaterCups = totalWaterOz * .125f;
                        float totalWaterLiters = totalWaterOz * .02957f;
                        float totalWaterPints = totalWaterCups * .5f;

                        // round to second decimal place
                        BigDecimal oz = new BigDecimal(totalWaterOz).setScale(1, RoundingMode.HALF_UP);
                        BigDecimal cups = new BigDecimal(totalWaterCups).setScale(1, RoundingMode.HALF_UP);
                        BigDecimal liters = new BigDecimal(totalWaterLiters).setScale(1, RoundingMode.HALF_UP);
                        BigDecimal pints = new BigDecimal(totalWaterPints).setScale(1, RoundingMode.HALF_UP);

                        // string it up
                        StringBuilder sb = new StringBuilder("WaterCalc: Based on your inputs you should drink either...");
                        sb.append(System.lineSeparator());
                        sb.append(oz.toString()).append(" Ounces").append(System.lineSeparator());
                        sb.append(cups.toString()).append(" Cups").append(System.lineSeparator());
                        sb.append(liters.toString()).append(" Liters or").append(System.lineSeparator());
                        sb.append(pints.toString()).append(" Pints").append(System.lineSeparator());
                        sb.append("... of water every day!");

                        return new SendMessage(update.message().chat().id(), sb.toString());
                    } catch (NumberFormatException e) {
                        return new SendMessage(update.message().chat().id(),"WaterCalc: Could not calculate. Weight should be a float, and activity minutes an integer.");
                    }
                }
                return new SendMessage(update.message().chat().id(),"WaterCalc: Could not calculate. Two arguments are required!");
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
        return "WaterCalc";
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
        return "Helpful information on daily water consumption\n\n" +
                "Use '/water' for a rough estimate on how much water you should have drank by now.\n" +
                "Use '/watercalc <weight in lbs> <minutes of activity>' to determine daily water needs.";
    }

    @Override
    public boolean enable() {
        return true;
    }

    @Override
    public boolean disable() {
        return true;
    }
}
