package cafe.seafarers.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import cafe.seafarers.currencies.BankManager;
import org.apache.commons.text.similarity.LevenshteinDistance;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.config.Resources;

public class TriviaPlugin implements BotPlugin {

	private class TriviaRound {
		String question;
		ArrayList<String> answers;

		public TriviaRound(String question) {
			this.answers = new ArrayList<String>();
			this.question = question;
		}

		public void addAnswer(String answer) {
			this.answers.add(answer.toLowerCase().trim());
		}

		public boolean checkAnswer(String guess) {
			String cleanedGuess = guess.toLowerCase().trim();
			for (String possibleMatch : answers) {
				int diff = new LevenshteinDistance().apply(possibleMatch, cleanedGuess);
				if (diff <= 2) { // Exact match or 2 characters wrong/removed
					return true;
				}
			}
			return false;
		}

		public String getAnswer() {
			StringBuffer sb = new StringBuffer();
			for (String ans : answers) {
				if (sb.length() > 0) {
					sb.append(" or ");
				}
				sb.append(ans);
			}
			return sb.toString();
		}

		public String getQuestion() {
			return question;
		}
	}

	private class TriviaGame {
		private ArrayList<TriviaRound> rounds;
		private int currentIndex;
		private HashMap<String, Integer> score;

		public TriviaGame() {
			this.score = new HashMap<String, Integer>();
			this.rounds = new ArrayList<TriviaRound>();
			this.currentIndex = 0;
		}

		public void reset() {
			this.score = new HashMap<String, Integer>();
			this.currentIndex = 0;
		}

		public void addRound(TriviaRound round) {
			this.rounds.add(round);
		}

		private TriviaRound getRound() {
			if (currentIndex < rounds.size()) {
				return rounds.get(currentIndex);
			} else {
				return null;
			}
		}

		public void nextRound() {
			currentIndex++;
		}

		public String getQuestion() {
			if (getRound() == null) {
				return "";
			}
			return getRound().getQuestion();
		}

		public boolean checkAnswer(String guess) {
			return getRound().checkAnswer(guess);
		}

		public String getAnswer() {
			return getRound().getAnswer();
		}

		/**
		 * + 1 because currentRound starts at 0
		 */
		public int getCurrentRound() {
			return currentIndex + 1;
		}

		public boolean isGameOver() {
			return currentIndex >= rounds.size();
		}

		public void givePoints(String user) {
			if (score.containsKey(user)) {
				score.put(user, score.get(user) + 1);
			} else {
				score.put(user, 1);
			}
		}

		public String getWinnerUser() {
			String winner = null;
			StringBuffer sb = new StringBuffer("\nScores:\n");
			// Get one winner
			for (Entry<String, Integer> e : score.entrySet()) {
				if (winner == null) {
					winner = e.getKey();
				} else if (e.getValue() > score.get(winner)) { // If new user is greater, update winner
					winner = e.getKey();
				}
				sb.append(e.getKey() + ":\t" + e.getValue() + "\n");
				// Pay out points equal to the number of questions this player got correct (jeopardy style)
				if (e.getValue() > 0){
					BankManager.deposit(e.getKey(), e.getValue());
				}
			}
			// Check if others tied
			for (Entry<String, Integer> e : score.entrySet()) {
				if (!winner.equals(e.getKey()) && score.get(winner) == e.getValue()) {
					winner += ", " + e.getKey();
				}
			}
			// Return final scoreboard
			if (winner != null) {
				return winner + " has won! Points have been awarded!\n" + sb.toString();
			} else {
				return "Nobody won!\n" + sb.toString();
			}
		}
	}

	private static final String[] COMMANDS = { "trivia", "trivialist", "trivianew", "triviastop", "trivianext" };
	private static final String[] DESCRIPTIONS = { "[name] starts a trivia game", "lists current trivia games",
			"creates a new trivia game", "stops a trivia game", "skips a trivia question" };

	// Trivia name to TrivaGame
	private HashMap<String, TriviaGame> trivia;

	// Chat Id to Current Game
	private HashMap<Long, TriviaGame> currentGames;
	// Chat Id to Current guesses
	private HashMap<Long, Integer> currentGuesses;
	// Chat Id to original question time
	private HashMap<Long, Long> currentTimeLimits;

	private Random random;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1);
		String command = message.split("[ @]")[0].toLowerCase();
		String args = message.substring(command.length()).trim();
		switch (command) {
		case "trivia":
			if (currentGames.containsKey(update.message().chat().id())) {
				return new SendMessage(update.message().chat().id(), "A game is already in progress.");
			} else if (args.length() == 0) {
				List<String> names = new ArrayList<String>(trivia.keySet());
				// random trivia game to play
				int randomIndex = random.nextInt(names.size());
				// save it and use it later
				args = names.get(randomIndex);
			}
			if (trivia.containsKey(args)) {
				TriviaGame game = trivia.get(args);
				currentGames.put(update.message().chat().id(), game);

				StringBuffer sb = new StringBuffer();
				sb.append("Welcome to trivia night!\n'");
				sb.append(args);
				sb.append("' is the game.\n");
				sb.append("Starting round " + game.getCurrentRound() + "\n\n");
				sb.append(game.getQuestion());
				currentGuesses.put(update.message().chat().id(), 0);
				currentTimeLimits.put(update.message().chat().id(), System.currentTimeMillis());
				return new SendMessage(update.message().chat().id(), sb.toString());
			} else {
				return new SendMessage(update.message().chat().id(), "That trivia game doesn't exist");
			}
		case "trivianext":
			TriviaGame game = currentGames.get(update.message().chat().id());
			if (game != null) {
				return nextQuestion(game, update.message().chat().id(), update.message().from(), true);
			}
			return new SendMessage(update.message().chat().id(), "There is no active game");
		case "trivialist":
			StringBuffer sb = new StringBuffer("Avaliable trivia games:\n");
			for (String name : trivia.keySet()) {
				if (sb.length() > 4000) {
					continue;
				}
				sb.append(name);
				sb.append("\n");
			}
			if (sb.length() > 4000) {
				sb.append("and more...");
			}
			return new SendMessage(update.message().chat().id(), sb.toString());
		case "triviastop":
			currentGames.remove(update.message().chat().id());
			return new SendMessage(update.message().chat().id(), "Trivia game ended");
		case "trivianew":
			return new SendMessage(update.message().chat().id(), "This feature is not implemented yet");
		}
		return null;
	}

	@Override
	public BaseRequest onMessage(Update update) {
		long id = update.message().chat().id();
		TriviaGame game = currentGames.get(id);
		if (game != null) {
			currentGuesses.put(id, currentGuesses.get(id) + 1);
			if (game.checkAnswer(update.message().text())) {
				return nextQuestion(game, update.message().chat().id(), update.message().from(), false);
			} else if (currentGuesses.get(id) > 10) {
				return nextQuestion(game, update.message().chat().id(), update.message().from(), true);
			}
		}
		return null;
	}

	private BaseRequest nextQuestion(TriviaGame game, long id, User from, boolean skipped) {
		currentGuesses.put(id, 0);
		currentTimeLimits.put(id, System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		if (skipped) {
			sb.append("Skipped question, no points.\nThe answer was '");
			sb.append(game.getAnswer());
			sb.append("'\n");
		} else {
			game.givePoints(getCanonicalName(from));
			sb.append("Correct ");
			sb.append(getCanonicalName(from));
			sb.append("!\n");
		}
		game.nextRound();
		if (game.isGameOver()) {
			sb.append("Game Over!\n\n");
			sb.append(game.getWinnerUser());

			game.reset();
			currentGames.remove(id);
			currentGuesses.remove(id);
			currentTimeLimits.remove(id);
		} else {
			sb.append("Round ");
			sb.append(game.getCurrentRound());
			sb.append("\n\n");
			sb.append(game.getQuestion());
		}
		return new SendMessage(id, sb.toString());
	}

	private String getCanonicalName(User user) {
		if (user.username() == null) {
			return user.firstName();
		} else {
			return user.username();
		}
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
		return "Trivia";
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
		random = new Random();
		trivia = new HashMap<String, TriviaGame>();
		currentGames = new HashMap<Long, TriviaGame>();
		currentGuesses = new HashMap<Long, Integer>();
		currentTimeLimits = new HashMap<Long, Long>();

		try {
			File file = Resources.LoadFile(this, "trivia.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			TriviaGame newGame = null;
			TriviaRound round = null;
			while (line != null) {
				line = line.trim();
				if (newGame == null) { // New Game
					newGame = new TriviaGame();
					trivia.put(line, newGame);
				} else if (round == null && line.isEmpty()) { // Game is over
					newGame = null;
				} else if (round == null) { // New round
					round = new TriviaRound(line);
				} else if (line.isEmpty()) { // Round is over
					newGame.addRound(round);
					round = null;
				} else { // Answer
					round.addAnswer(line);
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		System.out.println(trivia.size() + " trivia games");
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
		return "/trivia <name> - Starts trivia game with name\n/trivialist - list all trivia games\n/trivianew <name>- create a new trivia game\n/triviastop - end current trivia game";
	}

	@Override
	public BaseRequest periodicUpdate() {
		// If any game is
		for (long id : currentTimeLimits.keySet()) {
			TriviaGame game = currentGames.get(id);
			long diff = System.currentTimeMillis() - currentTimeLimits.get(id);
			// Skip if question has been going on for 5 minutes
			if (game != null && (diff > 1000 * 60 * 5)) {
				return nextQuestion(game, id, null, true);
			}
		}
		return null;
	}
}
