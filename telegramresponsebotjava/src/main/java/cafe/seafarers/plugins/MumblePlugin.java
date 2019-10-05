package cafe.seafarers.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import cafe.seafarers.config.Resources;

public class MumblePlugin implements BotPlugin {
	private static final String[] COMMANDS = { "mstatus", "menable", "mdisable" };
	private String dnsName;
	private Set<Long> enabledChannels;
	private int users;

	@Override
	public BaseRequest onCommand(Update update) {
		String message = update.message().text().substring(1).toLowerCase();
		String command = message.split("[ @]")[0];
		switch (command) {
		case "mstatus":
			return new SendMessage(update.message().chat().id(), "There are " + getUsers() + " users connected.");
		case "menable":
			enabledChannels.add(update.message().chat().id());
			return new SendMessage(update.message().chat().id(), "Enabled mumble status.");
		case "mdisable":
			enabledChannels.remove(update.message().chat().id());
			return new SendMessage(update.message().chat().id(), "Enabled mumble status.");
		}
		return null;
	}

	/**
	 * @return the number of connected users
	 */
	private int getUsers() {
		try {
			int PORT = 64738;
			InetAddress ADDRESS = InetAddress.getByName(dnsName);
			byte[] buffer = new byte[12];
			// Place time starting at index 4
			longToByteArray(System.currentTimeMillis(), buffer, 4);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, ADDRESS, PORT);
			DatagramSocket datagramSocket = new DatagramSocket();
			datagramSocket.send(packet);
			DatagramSocket serverSocket = datagramSocket;
			byte[] receiveData = new byte[24];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			byte[] response = receivePacket.getData();
			// Format ping data
			int users = (response[15] & 0xFF) | ((response[14] & 0xFF) << 8) | ((response[13] & 0xFF) << 16)
					| ((response[12] & 0xFF) << 24);
			datagramSocket.close();
			return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private String getConnectedUserMessage(int newUserCount) {
		StringBuffer sb = new StringBuffer();
		if (newUserCount < users) {
			sb.append("A user has disconnected from the server. There are now ");
		} else {
			sb.append("A user has connected to the server. There are now ");
		}
		sb.append(newUserCount);
		sb.append(" users connected.");
		return sb.toString();
	}

	/**
	 * Translate the given long into the array b starting at index offset
	 */
	private static void longToByteArray(long l, byte[] b, int offset) {
		b[7 + offset] = (byte) (l);
		l >>>= 8;
		b[6 + offset] = (byte) (l);
		l >>>= 8;
		b[5 + offset] = (byte) (l);
		l >>>= 8;
		b[4 + offset] = (byte) (l);
		l >>>= 8;
		b[3 + offset] = (byte) (l);
		l >>>= 8;
		b[2 + offset] = (byte) (l);
		l >>>= 8;
		b[1 + offset] = (byte) (l);
		l >>>= 8;
		b[0 + offset] = (byte) (l);
	}

	@Override
	public BaseRequest onMessage(Update update) {
		return null;
	}

	@Override
	public String[] getCommands() {
		return COMMANDS;
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
		File f = Resources.LoadFile(this, "config.txt");
		Scanner fileIn;
		try {
			fileIn = new Scanner(f);
			dnsName = fileIn.nextLine().trim();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		enabledChannels = new HashSet<Long>();
		users = 0;
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
		return "/mstatus\n/menable\n/mdisable";
	}

	@Override
	public BaseRequest periodicUpdate() {
		int newUsers = getUsers();
		if (users != newUsers) {
			String connectedString = getConnectedUserMessage(newUsers);
			users = newUsers;
			for (Long id : enabledChannels) {
				return new SendMessage(id, connectedString);
			}
		}
		return null;
	}
}
