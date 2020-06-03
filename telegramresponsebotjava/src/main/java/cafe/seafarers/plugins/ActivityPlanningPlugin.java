package cafe.seafarers.plugins;

import cafe.seafarers.config.Resources;
import cafe.seafarers.currencies.Account;
import cafe.seafarers.currencies.BankManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.HashMap;

public class ActivityPlanningPlugin implements BotPlugin{
    private final String[] COMMANDS = {"bid", "viewplan"};
    private final String[] DESCRIPTIONS = {"/bid [day: valid options:sun, mon, tue, wed, thu, fri, sat] [amount] <activity>", "/viewplan"};

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
        String message = update.message().text().substring(1);
        String[] args = message.split("[ ]");
        String command = args[0].toLowerCase();

        switch (command) {
            case "bid":
                if (args.length >= 4){ //command day value activity
                    try {
                        String day = args[1].toLowerCase();
                        int bidAmount = Integer.parseInt(args[2]);

                        String activity = "";

                        for (int i = 3; i < args.length; i++){
                            activity += " " + args[i];
                        }

                        ActivityPlan plan = new ActivityPlan();

                        if (BankManager.getFunds(user) > bidAmount){
                            if (plan.updateActivity(day, user, activity, bidAmount)){
                                BankManager.charge(user, bidAmount);
                                return new SendMessage(update.message().chat().id(),"APP: Congrats " + user + " you bought " + day + "!");
                            }
                            return new SendMessage(update.message().chat().id(),"APP: Your bid was either too low, or you already own this day.");
                        }
                        return new SendMessage(update.message().chat().id(),"APP: Your bid exceeds your account balance!");

                    } catch (NumberFormatException e) {
                        return new SendMessage(update.message().chat().id(),"APP: Unable to complete transaction. Invalid value for bid amount.");
                    }
                }
                return new SendMessage(update.message().chat().id(),"APP: Invalid format.");
            case "viewplan":
                ActivityPlan plan = new ActivityPlan();
                return new SendMessage(update.message().chat().id(), plan.toString());
        }
        return new SendMessage(update.message().chat().id(),"APP: Invalid format.");
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
        return "Activity Planning";
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
        return null;
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public boolean disable() {
        return false;
    }

    class ActivityPlan {
        private final String DIRECTORY = "ActivityPlanning";
        private final String FILENAME = "plan.json";
        private final String[] VALID_DAYS = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
        private HashMap<String, Activity> plan;


        public ActivityPlan(){
            try {
                File f = Resources.LoadFile(DIRECTORY, FILENAME);

                if (f != null) {
                    TypeToken<HashMap<String, Activity>> token = new TypeToken<HashMap<String, Activity>>() {
                    };
                    Gson gson = new Gson();
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    plan = gson.fromJson(br, token.getType());
                } else {
                    plan = new HashMap<String, Activity>();
                    plan = new HashMap<String, Activity>();
                    plan.put(VALID_DAYS[0], new Activity("None", "Nothing", 0));
                    plan.put(VALID_DAYS[1], new Activity("None", "Nothing", 0));
                    plan.put(VALID_DAYS[2], new Activity("None", "Nothing", 0));
                    plan.put(VALID_DAYS[3], new Activity("None", "Nothing", 0));
                    plan.put(VALID_DAYS[4], new Activity("None", "Nothing", 0));
                    plan.put(VALID_DAYS[5], new Activity("None", "Nothing", 0));
                    plan.put(VALID_DAYS[6], new Activity("None", "Nothing", 0));
                    save();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                plan = new HashMap<String, Activity>();
                plan.put(VALID_DAYS[0], new Activity("None", "Nothing", 0));
                plan.put(VALID_DAYS[1], new Activity("None", "Nothing", 0));
                plan.put(VALID_DAYS[2], new Activity("None", "Nothing", 0));
                plan.put(VALID_DAYS[3], new Activity("None", "Nothing", 0));
                plan.put(VALID_DAYS[4], new Activity("None", "Nothing", 0));
                plan.put(VALID_DAYS[5], new Activity("None", "Nothing", 0));
                plan.put(VALID_DAYS[6], new Activity("None", "Nothing", 0));
                save();
            }
        }

        // Return true if the new bid, amount, is greater than the last bid
        public boolean isOutBid(String day, int amount){
            return amount > plan.get(day).getValue();
        }

        public boolean updateActivity(String day, String user, String activity, int newValue){
            for (int i = 0; i < VALID_DAYS.length; i++){
                if (VALID_DAYS[i].equals(day)){
                    if (newValue > plan.get(VALID_DAYS[i]).value){
                        Activity a = new Activity(user, activity, newValue);
                        plan.put(VALID_DAYS[i], a);
                        System.out.println(save());
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "APP - Current Activity Plan:\n" +
                    "Sun -" + plan.get("sun").toString() + "\n" +
                    "Mon -" + plan.get("mon").toString() + "\n" +
                    "Tue -" + plan.get("tue").toString() + "\n" +
                    "Wed -" + plan.get("wed").toString() + "\n" +
                    "Thu -" + plan.get("thu").toString() + "\n" +
                    "Fri -" + plan.get("fri").toString() + "\n" +
                    "Sat -" + plan.get("sat").toString();
        }

        public boolean save() {
            Gson gson = new Gson();
            String json = gson.toJson(plan);
            if (Resources.SaveFile(DIRECTORY, FILENAME, json))
                return true;
            return false;
        }

    }

    class Activity {
        private String user;
        private String activity;
        private int value;

        public Activity(String user, String activity, int value){
            this.user = user;
            this.activity = activity;
            this.value = value;
        }

        @Override
        public String toString() {
            return "    Owner: " + user + "    Event: " + activity + "    Bid: " + value;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
