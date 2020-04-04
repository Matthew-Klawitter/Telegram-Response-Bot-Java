package cafe.seafarers.plugins;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.config.Resources;

public class ForecastPlugin implements BotPlugin {
	private static final String[] COMMANDS = { "weather" };
	private static final String[] DESCRIPTIONS = { "<city,country> sends the weather at a place" };

	private static String apiKey;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1);
		String command = message.split("[ @]")[0].toLowerCase();
		String args = message.substring(command.length()).trim();
		switch (command) {
		case "forecast":
			if (args.isEmpty()) {
				return new SendMessage(update.message().chat().id(), "Please enter where (City,country)");
			}
			// TODO request location if none supplied?
//			KeyboardButton[] kbs = {new KeyboardButton("Send location")};
//			kbs[0].requestLocation(true);
//			//AnswerInlineQuery i = new AnswerInlineQuery(args, null);
//			SendMessage m = new SendMessage(update.message().chat().id(), "Please send location");
//			m.replyMarkup(new ReplyKeyboardMarkup(kbs));
			return new SendMessage(update.message().chat().id(), weather(args));
		}
		return null;
	}

	// Modeling http response
	private class Response {
		Coord coord;
		Weather[] weather;
		String base;
		Main main;
		int visibility;
		Wind wind;
		Clouds clouds;
		Rain rain;
		Snow snow;
		int dt;
		Sys sys;
		int timezone;
		int id;
		String name;
		int cod;

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("In ");
			sb.append(name);
			sb.append(" it is ");
			sb.append(main.temp);
			sb.append(" degrees with ");
			sb.append(weather[0].description);
			return sb.toString();
		}
	}

	private class Coord {
		double lat;
		double lon;
	}

	private class Weather {
		int id;
		String main;
		String description;
		String icon;
	}

	private class Main {
		double temp;
		double pressure;
		double humidity;
		double temp_min;
		double temp_max;
	}

	private class Wind {
		double speed;
		int deg;
	}

	private class Clouds {
		double all;
	}

	private class Rain {
		// int 1h;
		// int 3h;
	}

	private class Snow {
		// int 1h;
		// int 3h;
	}

	private class Sys {
		int type;
		int id;
		double message;
		String country;
		int sunrise;
		int sunset;
	}

	public String weather(String place) {
		String baseUrl = "https://api.openweathermap.org/data/2.5/";
		String method = "weather?q=" + place;
		String key = "&APPID=" + apiKey;
		String params = "&units=imperial";
		String requestUrl = baseUrl + method + key + params;

		URL myurl;
		try {
			myurl = new URL(requestUrl);
			HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
			con.setRequestMethod("GET");

			StringBuffer sb = new StringBuffer();
			DataInputStream input = new DataInputStream(con.getInputStream());
			for (int c = input.read(); c != -1; c = input.read()) {
				sb.append((char) c);
			}
			input.close();
			Response r = new Gson().fromJson(sb.toString(), Response.class);
			return r.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "Invalid location";
		}
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
		File f = Resources.LoadFile(this, "config.txt");
		Scanner fileIn;
		try {
			fileIn = new Scanner(f);
			apiKey = fileIn.nextLine().trim();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
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

	@Override
	public BaseRequest periodicUpdate() {
		return null;
	}
}
