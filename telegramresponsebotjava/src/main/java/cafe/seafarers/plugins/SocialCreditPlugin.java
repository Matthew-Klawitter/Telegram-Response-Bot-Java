package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public class SocialCreditPlugin implements BotPlugin {
	@Override
	public BaseRequest onCommand(Update update) {
		return null;
	}

	@Override
	public BaseRequest onMessage(Update update) {
		return null;
	}

	@Override
	public String[] getCommands() {
		return new String[0];
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
		return null;
	}

	@Override
	public String getAuthor() {
		return null;
	}

	@Override
	public String getVersion() {
		return null;
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
}
