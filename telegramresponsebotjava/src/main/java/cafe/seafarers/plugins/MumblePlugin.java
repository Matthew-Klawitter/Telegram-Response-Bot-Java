package cafe.seafarers.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.config.Resources;

public class MumblePlugin implements BotPlugin {
	private static final String[] COMMANDS = { "menable", "mdisable" };
	private static final String[] DESCRIPTIONS = { "enables mumble alerts", "disable mumble alerts" };
    private String lastLineProcessed;
    private Set<Long> enabledChannels;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1).toLowerCase();
		String command = message.split("[ @]")[0];
		switch (command) {
		case "menable":
			enabledChannels.add(update.message().chat().id());
			return new SendMessage(update.message().chat().id(), "Enabled mumble status.");
		case "mdisable":
			enabledChannels.remove(update.message().chat().id());
			return new SendMessage(update.message().chat().id(), "Disabled mumble status.");
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
		return "Mumble";
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
        enabledChannels = new HashSet<Long>();
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
		return "/menable\n/mdisable";
	}

	@Override
	public BaseRequest periodicUpdate() {
        try {
            Scanner logFile = new Scanner(new File("/var/log/mumble-server/mumble-server.log"));
            String lastLine = null;
            while(logFile.hasNextLine()){
                lastLine = logFile.nextLine();
                if(lastLine.equals(lastLineProcessed)){
                    break;
                }
            }
            // If we are just starting, use the last line in the file next time
            if(lastLineProcessed == null){
                lastLineProcessed = lastLine;
                return null;
            }
            // If log file was cleared, and last line wasn't found, use the first line
            if(!logFile.hasNextLine()){
                logFile.reset();
            }
            while(logFile.hasNextLine()){
                lastLineProcessed = logFile.nextLine();
                if(lastLineProcessed.contains("Connection closed")){
                    String m = getName(lastLineProcessed);
                    String connectedString = String.format("%s disconnected from mumble.", m);
                    logFile.close();
                    for (Long id : enabledChannels) {
                        return new SendMessage(id, connectedString);
                    }
                    return null; // if no channels
                } else if(lastLineProcessed.contains("Authenticated")){
                    String m = getName(lastLineProcessed);
                    String connectedString = String.format("%s connected to mumble.", m);
                    logFile.close();
                    for (Long id : enabledChannels) {
                        return new SendMessage(id, connectedString);
                    }
                    return null; // if no channels
                }
            }
            System.out.println(lastLineProcessed);
            logFile.close();
        } catch(java.io.FileNotFoundException e){
			System.out.println(e);
            return null;
        }
        return null;
	}

    // These are very specific properties of the mumble log
    private String getName(String line){
        return line.substring(line.indexOf(":", 34)+1, line.indexOf("(", 34));
    }
}
