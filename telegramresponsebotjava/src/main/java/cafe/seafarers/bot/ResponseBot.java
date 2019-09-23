package cafe.seafarers.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class ResponseBot {
    private TelegramBot bot;
    private boolean isRunning;

    public ResponseBot(String token){
        bot = new TelegramBot(token);
        isRunning = false;
    }

    public boolean startUpdateListener(){
        if (!isRunning){
            bot.setUpdatesListener(new UpdatesListener() {
                public int process(List<Update> list) {
                    // Depending on the update in the list a different plugin type will have to be called

                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }
            });

            isRunning = true;
            return true;
        }
        return false;
    }

    public boolean stopUpdateListener(){
        if (isRunning){
            bot.removeGetUpdatesListener();
            isRunning = false;
            return true;
        }
        return false;
    }
}
