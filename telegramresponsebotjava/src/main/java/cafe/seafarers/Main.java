package cafe.seafarers;

import java.util.ArrayList;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;

import cafe.seafarers.plugins.PluginManager;

public class Main {
    /**
     * Main method that launches the bot
     * @param args
     */
    public static void main(String[] args){
    	if(args.length < 2) {
    		System.out.println("Usage: java Main <bot token> <plugin directory> <plugin names...>");
    		return;
    	}
    	String botToken = args[0];
    	String pluginDir = args[1];
    	List<String> pluginNames = new ArrayList<String>();
    	for(int i = 2; i < args.length; i++) {
    		pluginNames.add(args[i]);
    	}
    	
    	TelegramBot bot = new TelegramBot(botToken);
    	PluginManager manager = new PluginManager(pluginDir);
    	manager.loadPlugins(bot, pluginNames);
    }
}
