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
//			for (String answer : answers) {
//				this.answers.add(answer.toLowerCase().trim());
//			}
			this.question = question;
		}

		public void addAnswer(String answer) {
			this.answers.add(answer.toLowerCase().trim());
		}

		public boolean checkAnswer(String guess) {
			String cleanedGuess = guess.toLowerCase().trim();
			for (String possibleMatch : answers) {
				// If the answer is contained in the guess, the guess is write
				if (cleanedGuess.contains(possibleMatch)) {
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
			// Get one winner
			for (Entry<String, Integer> e : score.entrySet()) {
				if (winner == null) {
					winner = e.getKey();
				} else if (score.get(winner) > e.getValue()) {
					winner = e.getKey();
				}
			}
			// Check if others tied
			for (Entry<String, Integer> e : score.entrySet()) {
				if (!winner.equals(e.getKey()) && score.get(winner) == e.getValue()) {
					winner += ", " + e.getKey();
				}
			}
			return winner;
		}
	}

	private static final String[] COMMANDS = { "trivia", "trivialist", "trivianew", "triviastop", "trivianext" };
	// Trivia name to TrivaGame
	private HashMap<String, TriviaGame> trivia;

	// Chat Id to Current Game
	private HashMap<Long, TriviaGame> currentGames;

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

				return new SendMessage(update.message().chat().id(), sb.toString());
			} else {
				return new SendMessage(update.message().chat().id(), "That trivia game doesn't exist");
			}
		case "trivianext":
			TriviaGame game = currentGames.get(update.message().chat().id());
			if (game != null) {
				return nextQuestion(game, update, true);
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
		TriviaGame game = currentGames.get(update.message().chat().id());
		if (game != null && game.checkAnswer(update.message().text())) {
			return nextQuestion(game, update, false);
		}
		return null;
	}

	private BaseRequest nextQuestion(TriviaGame game, Update update, boolean skipped) {
		StringBuffer sb = new StringBuffer();
		if (skipped) {
			sb.append("Skipped question, no points.\nThe answer was '");
			sb.append(game.getAnswer());
			sb.append("'\n");
		} else {
			game.givePoints(getCanonicalName(update.message().from()));
			sb.append("Correct ");
			sb.append(getCanonicalName(update.message().from()));
			sb.append("!\n");
		}
		game.nextRound();
		if (game.isGameOver()) {
			sb.append("Game Over!\n\n");
			if (game.getWinnerUser() != null) {
				sb.append(game.getWinnerUser());
				sb.append(" has won!");
			} else {
				sb.append("Nobody won!");
			}

			game.reset();
			currentGames.remove(update.message().chat().id());
		} else {
			sb.append("Round ");
			sb.append(game.getCurrentRound());
			sb.append("\n\n");
			sb.append(game.getQuestion());
		}
		return new SendMessage(update.message().chat().id(), sb.toString());
	}

	private String getCanonicalName(User user) {
		if (user.username() == null) {
			return user.firstName();
		} else {
			return user.username();
		}
	}

	@Override
	public String[] getCommands() {
		return COMMANDS;
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
		} catch (NullPointerException e){
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
		return null;
	}
}
