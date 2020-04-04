package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.MessagesResponse;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

public class VapePlugin implements BotPlugin {
	private HashMap<String, Integer> penFuel;

	@Override
	public BaseRequest onCommand(Update update) {
		Long channelID = update.message().chat().id();
		String message = update.message().text().substring(1);
		String command = message.split("[ @]")[0];
		String user = update.message().from().username();
		Random r = new Random();
		int vapeLevel = r.nextInt(20);

		if (!penFuel.containsKey(user)) {
			penFuel.put(user, 50);
		}

		if (command.equals("vrefill")) {
			penFuel.replace(user, 50);
			return new SendMessage(channelID, user + " refill's their vape pen!");
		}

		if (penFuel.get(user) <= 0) {
			return new SendMessage(channelID, user
					+ " tries to vape, but their pen was out of juice! You need to use /vrefill before vaping again");
		}

		switch (vapeLevel) {
		case 0:
			penFuel.replace(user, penFuel.get(user));
			return new SendMessage(channelID, user + " forgot to click the button.");
		case 1:
			penFuel.replace(user, penFuel.get(user) - 1);
			return new SendMessage(channelID,
					user + " tries to breath in the vape, but their pen is empty! How foolish.");
		case 2:
			penFuel.replace(user, penFuel.get(user) - 2);
			return new SendMessage(channelID, user + " takes a swig of vape, and breaths out a small cloud.");
		case 3:
			penFuel.replace(user, penFuel.get(user) - 3);
			return new SendMessage(channelID,
					user + " takes a swig of vape, and breaths out a respectable cloud. Nice!");
		case 4:
			penFuel.replace(user, penFuel.get(user) - 4);
			return new SendMessage(channelID,
					user + " takes a swig of vape, and breaths out one heck of a cloud! Siiick!");
		case 5:
			penFuel.replace(user, penFuel.get(user) - 5);
			return new SendMessage(channelID, user + " lets out the sickest cloud anyone has ever seen!");
		case 6:
			penFuel.replace(user, penFuel.get(user) - 6);
			return new SendMessage(channelID, user + " downs themselves in their own vape. Honorable sudoku.");
		case 7:
			penFuel.replace(user, penFuel.get(user) - 7);
			return new SendMessage(channelID,
					user + " has followed the ways of the vapnatman. They hide themselves within their vape.");
		case 8:
			penFuel.replace(user, penFuel.get(user) - 8);
			return new SendMessage(channelID, user
					+ " has become vape itself. With every breath they let out the sickest vape cloud. No one can match them.");
		case 9:
			penFuel.replace(user, penFuel.get(user) - 9);
			return new SendMessage(channelID, user + " tries to take a swig of vape but chokes on it! How lame.");
		case 10:
			penFuel.replace(user, penFuel.get(user) - 10);
			return new SendMessage(channelID,
					user + " can't decide which flavor they like. They waste a lot of their vape.");
		case 11:
			penFuel.replace(user, penFuel.get(user) - 11);
			return new SendMessage(channelID, user + " forgot to stop pressing the pen.");
		case 12:
			penFuel.replace(user, penFuel.get(user) - 12);
			return new SendMessage(channelID, user + " no longer breaths oxygen. They breath pure vape.");
		case 13:
			penFuel.replace(user, penFuel.get(user) - 13);
			return new SendMessage(channelID, user + " wants vape banned... or they say as they vape.");
		case 14:
			penFuel.replace(user, penFuel.get(user) - 14);
			return new SendMessage(channelID, user + " vapes one out to the Colts. Praise be Andrew Luck.");
		case 15:
			penFuel.replace(user, penFuel.get(user) - 15);
			return new SendMessage(channelID, user + " thinks vaping is so cool and puffs out three rings.");
		case 16:
			penFuel.replace(user, penFuel.get(user) - 16);
			return new SendMessage(channelID, user + " does a cool table trick with a mouthful of vape.");
		case 17:
			penFuel.replace(user, penFuel.get(user) - 17);
			return new SendMessage(channelID,
					user + " cloaked themselves in their vape of invisibility. There is vape everywhere.");
		case 18:
			penFuel.replace(user, penFuel.get(user) - 18);
			return new SendMessage(channelID, user + " is a living fog machine.");
		case 19:
			penFuel.replace(user, penFuel.get(user) - 19);
			return new SendMessage(channelID, user + " forgot to cap their vape pen and wasted their juice.");
		}
		return new SendMessage(channelID, user + " tries to take a swig of vape but chokes on it! How lame.");
	}

	@Override
	public String[] getCommands() {
		return new String[] { "vape", "vrefill" };
	}

	@Override
	public BaseRequest periodicUpdate() {
		return null;
	}

	@Override
	public String getName() {
		return "Vape";
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
		return "/vape to exhale a sick cloud.";
	}

	@Override
	public boolean enable() {
		penFuel = new HashMap<String, Integer>();

		return false;
	}

	@Override
	public boolean disable() {
		return false;
	}

	@Override
	public boolean hasMessageAccess() {
		return false;
	}

	@Override
	public BaseRequest onMessage(Update update) {
		return null;
	}
}
