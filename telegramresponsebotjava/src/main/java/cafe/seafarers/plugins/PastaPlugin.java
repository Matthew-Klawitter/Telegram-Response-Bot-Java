package cafe.seafarers.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cafe.seafarers.config.Resources;
import cafe.seafarers.currencies.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class PastaPlugin implements BotPlugin {
	private static final String[] COMMANDS = { "pasta", "pastalist", "pastanew" };
	private static final String[] DESCRIPTIONS = { "[name] gets an (optionally named) pasta", "lists current pasta",
			"Create a new pasta - '/pastanew <name> [type the pasta on a new line]'" };
	private HashMap<String, String> pasta;
	private Random random;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1).toLowerCase();
		String command = message.split("[ @]")[0];
		String args = message.substring(command.length()).trim();
		switch (command) {
		case "pastalist":
			if (pasta.size() == 0) {
				return new SendMessage(update.message().chat().id(), "No pasta set");
			}
			StringBuffer sb = new StringBuffer();
			for (String pasta : pasta.keySet()) {
				sb.append(pasta);
				sb.append("\n");
			}
			return new SendMessage(update.message().chat().id(), sb.toString());
		case "pastanew":
			String[] lines = args.split("\n", 2);
			pasta.put(lines[0], lines[1]);
			save();
			return new SendMessage(update.message().chat().id(), "Created pasta: " + lines[0]);
		case "pasta":
			if (args.length() > 0) {
				if (pasta.containsKey(args)) {
					return new SendMessage(update.message().chat().id(), pasta.get(args));
				} else {
					return new SendMessage(update.message().chat().id(), "That pasta doesn't exist");
				}

			}
			else if (pasta.isEmpty()){
				return new SendMessage(update.message().chat().id(), "No pasta's have been created!");
			}
			else {
				int randomIndex = random.nextInt(pasta.size());
				ArrayList<String> pastas = new ArrayList<String>(pasta.keySet());
				String key = pastas.get(randomIndex);
				return new SendMessage(update.message().chat().id(), pasta.get(key));
			}
		}
		return null;
	}

	@Override
	public BaseRequest onMessage(Update update) {
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
		return "Pasta";
	}

	@Override
	public String getAuthor() {
		return "Mark";
	}

	@Override
	public String getVersion() {
		return "1.1";
	}

	@Override
	public boolean enable() {
		try {
			File f = Resources.LoadFile(this, "pastas.json");

			if (f != null) {
				TypeToken<HashMap<String, String>> token = new TypeToken<HashMap<String, String>>() {
				};
				Gson gson = new Gson();
				BufferedReader br = new BufferedReader(new FileReader(f));
				pasta = gson.fromJson(br, token.getType());
			} else {
				pasta = new HashMap<String, String>();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			pasta = new HashMap<String, String>();
		}

		random = new Random();
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
		return "Sends a pasta\n/pasta <name?>\n/pastalist\n/pastanew <pasta name>...\nPasta";
	}

	@Override
	public BaseRequest periodicUpdate() {
		return null;
	}

	private boolean save() {
		Gson gson = new Gson();
		String json = gson.toJson(pasta);
		if (Resources.SaveFile(this, "pastas.json", json))
			return true;
		return false;
	}
}
