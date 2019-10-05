package cafe.seafarers.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class TriggerPlugin implements BotPlugin {
	private static final String[] COMMANDS = { "triggerlist", "triggernew" };
	private HashMap<String, ArrayList<String>> triggers;
	private Random random;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1).toLowerCase();
		String command = message.split("[ @]")[0];
		switch (command) {
		case "triggerlist":
			if (triggers.size() == 0) {
				return new SendMessage(update.message().chat().id(), "No triggers set");
			}
			StringBuffer sb = new StringBuffer();
			for (String trigger : triggers.keySet()) {
				sb.append(trigger);
				sb.append("\n");
			}
			return new SendMessage(update.message().chat().id(), sb.toString());
		case "triggernew":
			String args = message.substring(command.length()).trim();
			String[] lines = args.split("\n");
			ArrayList<String> responses = new ArrayList<String>();
			for (String response : lines) {
				responses.add(response);
			}
			String trigger = responses.remove(0);
			triggers.put(trigger, responses);
			return new SendMessage(update.message().chat().id(), "Created trigger: " + trigger);
		}
		return null;
	}

	@Override
	public BaseRequest onMessage(Update update) {
		for (String trigger : triggers.keySet()) {
			if (update.message().text().toLowerCase().contains(trigger)) {
				ArrayList<String> responses = triggers.get(trigger);
				int randomIndex = random.nextInt(responses.size());
				return new SendMessage(update.message().chat().id(), responses.get(randomIndex));
			}
		}
		return null;
	}

	@Override
	public String[] getCommands() {
		return COMMANDS;
	}

	@Override
	public String getName() {
		return "Trigger";
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
		triggers = new HashMap<String, ArrayList<String>>();
		random = new Random();
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
		return "Automatically responses to a trigger word\n/triggerlist\n/triggernew <trigger>...\nresponse1\nresponse2...";
	}

	@Override
	public BaseRequest periodicUpdate() {
		return null;
	}
}
