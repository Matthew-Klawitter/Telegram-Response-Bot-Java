package cafe.seafarers.plugins;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.imageio.ImageIO;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendPhoto;

import cafe.seafarers.config.Resources;

public class DogePlugin implements BotPlugin {
	private static final String[] COMMANDS = { "doge" };
	private static final String[] WORDS = { "many", "very", "such", "much", "so", "wow" };
	private static final Color[] COLORS = { new Color(52, 255, 0), // Green
			new Color(255, 0, 0), // Red
			new Color(0, 0, 255), // Blue
			new Color(255, 255, 0), // Yellow
			new Color(255, 128, 0), // Orange
			new Color(200, 0, 255) // Purple
	};
	private static final int[] COORDS = { 70, 100, 750, 200, 650, 460, 785, 700, 150, 600 };

	private HashSet<String> collectedWords;
	private Random random;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1);
		String command = message.split("[ @]")[0].toLowerCase();
		switch (command) {
		case "doge":
			int count = 5;
			if(collectedWords.size() < count) {
				return null;
			}
			shuffleArray(WORDS);
			shuffleArray(COLORS);
			ArrayList<String> wordsArray = new ArrayList<String>(collectedWords);
			String[] selectedWords = new String[count];
			for (int i = 0; i < count; i++) {
				if (WORDS[i].equals("wow")) {
					selectedWords[i] = WORDS[i];
				} else {
					selectedWords[i] = WORDS[i] + " " + wordsArray.get(random.nextInt(wordsArray.size()));
				}
			}
			File file = Resources.LoadFile(this, "doge.jpg");
			try {
				BufferedImage bufferedImage = ImageIO.read(file);

				Graphics graphics = bufferedImage.getGraphics();

				for (int i = 0; i < count; i++) {
					graphics.setColor(COLORS[i]);
					graphics.setFont(new Font("Comic Sans", Font.PLAIN, 72));
					int x = COORDS[2 * i];
					int y = COORDS[2 * i + 1];
					graphics.drawString(selectedWords[i], x, y);
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "jpg", out);
				System.out.println("Image Created");
				return new SendPhoto(update.message().chat().id(), out.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void shuffleArray(Object[] ar) {
		for (int i = ar.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			// Simple swap
			Object a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	@Override
	public BaseRequest onMessage(Update update) {
		String[] words = update.message().text().toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");
		for (String word : words) {
			collectedWords.add(word);
		}
		return null;
	}

	@Override
	public String[] getCommands() {
		return COMMANDS;
	}

	@Override
	public String getName() {
		return "Doge";
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
		collectedWords = new HashSet<String>();
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
		return "/doge\n";
	}
	
	@Override
	public BaseRequest periodicUpdate() {
		return null;
	}
}
