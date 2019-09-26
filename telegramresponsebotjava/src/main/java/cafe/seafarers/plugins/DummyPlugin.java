package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public class DummyPlugin implements BotPlugin {
    public BaseRequest onCommand(Update update) {
        return null;
    }

    public String[] getCommands() {
        return new String[0];
    }

    public String getName() {
        return "Test Plugin Please Ignore";
    }

    public String getAuthor() {
        return null;
    }

    public String getVersion() {
        return null;
    }

    public boolean enable() {
        return false;
    }

    public boolean disable() {
        return false;
    }
}
