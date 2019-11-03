package cafe.seafarers.plugins;

import cafe.seafarers.config.Resources;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendAudio;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OhHiMarkPlugin implements BotPlugin {
    private final String FILE = "ohhimark.mp3";
    private String triggerDate;

    @Override
    public BaseRequest onCommand(Update update) {
        return null;
    }

    @Override
    public BaseRequest onMessage(Update update) {
        Long channelID = update.message().chat().id();
        String firstName = update.message().chat().firstName();
        String user = update.message().from().username();
        File audio = Resources.LoadFile(this, FILE);

        try {
            if (user.equals("Steve_Clarney") || firstName.equals("Mark")){
                Calendar today = Calendar.getInstance();
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                today.setTime(date);

                if (format.format(today.getTime()).equals(triggerDate) || triggerDate.isEmpty()){
                    assert audio != null;
                    today.add(Calendar.DAY_OF_YEAR, 1);
                    triggerDate = format.format(today.getTime());
                    return new SendAudio(channelID, audio);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String[] getCommands() {
        return new String[0];
    }

    @Override
    public boolean hasMessageAccess() {
        return true;
    }

    @Override
    public BaseRequest periodicUpdate() {
        return null;
    }

    @Override
    public String getName() {
        return "OhHiMark";
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
        return "Ahaha, you must be kidding aren't you?";
    }

    @Override
    public boolean enable() {
        triggerDate = "";
        return true;
    }

    @Override
    public boolean disable() {
        return false;
    }
}
