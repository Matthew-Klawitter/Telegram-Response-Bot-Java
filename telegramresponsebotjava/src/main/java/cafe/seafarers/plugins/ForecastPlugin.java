package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class ForecastPlugin implements BotPlugin {
	private static final String[] COMMANDS = { "forecast" };

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1);
		String command = message.split("[ @]")[0].toLowerCase();
		String args = message.substring(command.length()).trim();
		switch (command) {
		case "forecast":
			KeyboardButton[] kbs = {new KeyboardButton("Send location")};
			kbs[0].requestLocation(true);
			//AnswerInlineQuery i = new AnswerInlineQuery(args, null);
			SendMessage m = new SendMessage(update.message().chat().id(), "Please send location");
			m.replyMarkup(new ReplyKeyboardMarkup(kbs));
			return m;
		}
		return null;
	}

	@Override
	public BaseRequest onMessage(Update update) {
		return null;
	}

	@Override
	public String[] getCommands() {
		return COMMANDS;
	}

	@Override
	public String getName() {
		return "Forecast";
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
		return false;
	}

	@Override
	public String getHelp() {
		return "/forecast to get the weather\n";
	}
}
