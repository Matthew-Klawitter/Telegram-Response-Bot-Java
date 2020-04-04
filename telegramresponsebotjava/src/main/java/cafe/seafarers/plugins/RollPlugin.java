package cafe.seafarers.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class RollPlugin implements BotPlugin {
	private static final String[] COMMANDS = { "r" };
	private static final String[] DESCRIPTIONS = { "[quantity]d[dice_size] rolls some dice" };

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(2).toLowerCase().trim();
		String[] parts = message.split("d");
		int num, size;

		try {
			if (parts.length > 0) {
				num = Math.max(Integer.parseInt(parts[0]), 1);
			} else {
				num = 1;
			}

			if (parts.length > 1) {
				size = Integer.parseInt(parts[1]);

				if (size < 1) {
					size = 1;
				}
			} else {
				size = 1;
			}

			List<Integer> rolls = performRolls(num, size);
			int sum = sum(rolls);

			StringBuffer response = new StringBuffer("Roll: Summary:\n");

			for (int r : rolls) {
				response.append("Rolled: " + r + "\n");
			}

			response.append("\nSum: " + sum);

			return new SendMessage(update.message().chat().id(), response.toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new SendMessage(update.message().chat().id(),
					"Roll: Sorry, something went wrong when parsing your syntax! Please use /help Rolls to learn how to use this plugin.");
		}

	}

	private List<Integer> performRolls(int num, int size) {
		List<Integer> rolls = new ArrayList<Integer>();
		Random rand = new Random();

		for (int i = 0; i < num; i++) {
			rolls.add(rand.nextInt(size) + 1);
		}
		return rolls;
	}

	private int sum(List<Integer> rolls) {
		int sum = 0;
		for (int i : rolls) {
			sum += i;
		}
		return sum;
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
	public BaseRequest periodicUpdate() {
		return null;
	}

	@Override
	public String getName() {
		return "Roll";
	}

	@Override
	public String getAuthor() {
		return "Matthew Klawitter\nCode based upon Mark Powers' Python implementation";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getHelp() {
		return "Roll: '/r [quantity]d[dice_size]' to roll a dice.";
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
}
