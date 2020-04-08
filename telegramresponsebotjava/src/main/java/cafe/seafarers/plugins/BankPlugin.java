package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.currencies.BankManager;

public class BankPlugin implements BotPlugin {
	private final String[] COMMANDS = { "bcreate", "btransfer", "bview" };
	private final String[] DESCRIPTIONS = { "creates an account", "<to> <amount> sends points",
			"views available funds" };

	private String getCanonicalName(User user) {
		if (user.username() == null) {
			return user.firstName();
		} else {
			return user.username();
		}
	}

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1);
		String command = message.split("[ @]")[0];
		String user = getCanonicalName(update.message().from());

		switch (command) {
		case "bcreate":
			if (BankManager.createAccount(user)) {
				return new SendMessage(update.message().chat().id(), "Bank: Successfully created an account");
			}
			return new SendMessage(update.message().chat().id(), "Bank: You already have an account.");
		case "btransfer":
			String[] args = message.substring(command.length() + 1).split(" ");

			if (args.length == 2) {
				try {
					String toUser = args[0];
					//toUser = toUser.substring(0, 1).toUpperCase() + toUser.substring(1);
					int amount = Integer.parseInt(args[1]);

					if (BankManager.transferFunds(user, toUser, amount))
						return new SendMessage(update.message().chat().id(), "Bank: Successfully transferred funds.");
					return new SendMessage(update.message().chat().id(),"Bank: Could not transfer funds. Your balance is too low.");
				} catch (NumberFormatException e) {
					return new SendMessage(update.message().chat().id(),"Bank: Could not transfer funds. Must input a valid amount.");
				}
			}
			return new SendMessage(update.message().chat().id(),"Bank: Could not transfer funds. Invalid arguments specified!");
		case "bview":
			int funds = BankManager.getFunds(user);
			return new SendMessage(update.message().chat().id(), "Bank: You have " + funds + " points");
		}
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
	public String getName() {
		return "Bank";
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
		return "Available commands for Bank:\n'bcreate' to create an account\n'btransfer <to> [amount]' to send points\n'bview' to view available funds";
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
}
