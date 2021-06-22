package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class DadPlugin implements BotPlugin {
	private static final String[] COMMANDS = {};

	@Override
	public BaseRequest onCommand(Update update) {
		return null;
	}

	@Override
	public BaseRequest onMessage(Update update) {
		String message = update.message().text();

		if (message.toLowerCase().startsWith("i'm ") || message.toLowerCase().startsWith("im ") || message.toLowerCase().startsWith("i’m ")) {
			if (message.contains("tler")){
				return new SendMessage(update.message().chat().id(), "What are you trying to say son?");
			}

			String newMessage = "Hi " + message.substring(3).trim() + ", I'm dad!";
			return new SendMessage(update.message().chat().id(), newMessage);
		}
		return null;
	}

	@Override
	public BotCommand[] getCommands() {
		return new BotCommand[0];
	}

	@Override
	public String getName() {
		return "Dad";
	}

	@Override
	public String getAuthor() {
		return "Mark";
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
		return "I'm dad!\n";
	}

	@Override
	public BaseRequest periodicUpdate() {
		return null;
	}
}
