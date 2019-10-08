package cafe.seafarers.bot;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendLocation;
import com.pengrad.telegrambot.response.BaseResponse;
import com.sun.source.util.Plugin;

import cafe.seafarers.plugins.PluginManager;

public class ResponseBot {
	private TelegramBot bot;
	private boolean isRunning;

	public ResponseBot(String token) {
		bot = new TelegramBot(token);
		isRunning = false;
	}

	public boolean startUpdateListener(PluginManager manager) {
		if (!isRunning) {
			bot.setUpdatesListener(new UpdatesListener() {
				public int process(List<Update> list) {
					for (Update update : list) {
						try {
							if (update.message().text() == null) {
								System.out.println(update.message().toString());
								continue;
							}
							if (update.message().text().startsWith("/")) {
								BaseRequest request = manager.handleCommand(update);
								if (request != null) {
									bot.execute(request);
								}
							} else {
								List<BaseRequest> requests = manager.handleMessage(update);
								for (BaseRequest request : requests) {
									bot.execute(request);
								}
							}
						} catch (Exception e) {
							System.out.println("Error!");
							e.printStackTrace();
							System.out.println("Cased by update:");
							System.out.println(update.toString());
						}
					}

					return UpdatesListener.CONFIRMED_UPDATES_ALL;
				}
			});

			isRunning = true;
			return true;
		}
		return false;

	}

	public boolean stopUpdateListener() {
		if (isRunning) {
			bot.removeGetUpdatesListener();
			isRunning = false;
			return true;
		}
		return false;
	}

	public void startPeriodicUpdateListener(PluginManager manager, int delay) {
		PeriodicUpdateThread thread = new PeriodicUpdateThread(manager, delay);
		thread.start();
	}

	private class PeriodicUpdateThread extends Thread {
		PluginManager manager;
		int delay;

		public PeriodicUpdateThread(PluginManager manager, int secondDelay) {
			this.manager = manager;
			this.delay = secondDelay;
		}

		public void run() {
			try {
				while (true) {
					Thread.sleep(delay * 1000);
					List<BaseRequest> requests = manager.updatePeriodically();
					for (BaseRequest request : requests) {
						bot.execute(request);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
