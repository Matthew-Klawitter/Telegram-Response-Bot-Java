package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class DummyPlugin implements BotPlugin {
	private static final String[] COMMANDS = {"test"};
	
	@Override
    public BaseRequest onCommand(Update update) {
    	return new SendMessage(update.message().chat().id(), "Hello!");
    }
	
	@Override
	public BaseRequest onMessage(Update update) {
		return new SendMessage(update.message().chat().id(), update.message().text()); 
	}

	@Override
    public String[] getCommands() {
        return COMMANDS;
    }

	@Override
    public String getName() {
        return "Dummy Plugin";
    }

	@Override
    public String getAuthor() {
        return "";
    }

	@Override
    public String getVersion() {
        return "1.0";
    }

	@Override
    public boolean enable() {
        return true;
    }

	@Override
    public boolean disable() {
        return true;
    }	

	@Override
	public boolean hasMessageAccess() {
		return true;
	}

	@Override
	public String getHelp() {
		return "/test to greet me\n";
	}
}
