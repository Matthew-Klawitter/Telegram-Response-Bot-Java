package cafe.seafarers.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

public class PluginManager {
	// Map of command strings to BotPlugins, used to call Plugins when a valid
	// command is received
	private HashMap<String, BotPlugin> commands;
	// Map of plugins to Booleans, determines if a plugin is enabled
	private HashMap<BotPlugin, Boolean> plugins;
	// Set of plugins that receive each message updates
	private Set<BotPlugin> messagePlugins;
	//
	private String pluginDirPath;

	public PluginManager(String pluginPath) {
		this.plugins = new HashMap<BotPlugin, Boolean>();
		this.commands = new HashMap<String, BotPlugin>();
		this.messagePlugins = new HashSet<BotPlugin>();
		// importer = new PluginImporter(pluginPath);
		this.pluginDirPath = pluginPath;
	}

	/**
	 * Attempts to instantiate loaded plugin classes into objects for use with the
	 * Bot
	 * 
	 * @param bot         instance of TelegramBot
	 * @param pluginPaths List<String> of paths to each plugin that should be
	 *                    imported
	 * @return boolean true if plugins are successfully imported and instantiated
	 */
	public boolean loadPlugins(List<String> pluginPaths) {
		// Clear out any existing data to facilitate reloading in the future
		plugins.clear();
		commands.clear();
		messagePlugins.clear();

		Set<Class<BotPlugin>> pluginClasses = importPlugins(pluginPaths);
		if (pluginClasses != null) {
			return instantiateImported(pluginClasses);
		}
		return false;
	}

	/**
	 * Imports plugins into the Java classpath into importedPluginClasses
	 * 
	 * @param classNames full names of classes to load
	 * @return true if the class is successfully imported
	 */
	private Set<Class<BotPlugin>> importPlugins(List<String> classNames) {
		try (URLClassLoader cl = new URLClassLoader(new URL[] { new File(pluginDirPath).toURI().toURL() })) {
			Set<Class<BotPlugin>> importedPluginClasses = new HashSet<Class<BotPlugin>>();
			for (String name : classNames) {
				Class<BotPlugin> c = (Class<BotPlugin>) cl.loadClass(name);
				importedPluginClasses.add(c);
			}
			return importedPluginClasses;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Instantiates all imported plugins in importer.getImportedPluginClasses() and
	 * adds object instances to plugins Set
	 * 
	 * @return true if plugins are successfully instantiated and added to plugins
	 *         Set
	 */
	private boolean instantiateImported(Set<Class<BotPlugin>> pluginClasses) {
		try {
			for (Class<BotPlugin> c : pluginClasses) {
				// Instantiate and load Plugin
				BotPlugin plugin = c.getConstructor().newInstance();

				// Map commands to Plugin
				for (String command : plugin.getCommands()) {
					commands.put(command, plugin);
				}
				if (plugin.hasMessageAccess()) {
					messagePlugins.add(plugin);
				}
				// Enable the plugin for operation
				plugins.put(plugin, true);
			}
			return true;
		} catch (InstantiationException | InvocationTargetException | NoSuchMethodException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	public BaseRequest handleCommand(Update update) {
		Long chatId = update.message().chat().id();
		String message = update.message().text().substring(1);
		// Split at either @ or ' '
		String command = message.split("[ @]")[0];
		String args = message.substring(command.length()).trim();
		// Run a PluginManager command or a plugin command
		switch (command) {
		case "plugins":
			StringBuffer response = new StringBuffer();
			for (Entry<BotPlugin, Boolean> plugin : plugins.entrySet()) {
				response.append(plugin.getKey().getName());
				// Notify if plugin has message access
				if (plugin.getKey().hasMessageAccess()) {
					response.append("(M)");
				}
				// Notify if plugin disabled
				if (!plugin.getValue()) {
					response.append("(D)");
				}
				response.append("\n");
			}
			return new SendMessage(chatId, response.toString());
		case "help":
			if (getPluginByName(args) == null) {
				return new SendMessage(chatId,
						"/help <plugin name> - Display help for a plugin\n" 
				+ "/plugins - List plugins\n"
				+ "/disable <plugin name> - Disables plugin\n"
				+ "/enable <plugin name> - Enables plugin\n");
			} else {
				BotPlugin plugin = getPluginByName(args);
				StringBuffer sb = new StringBuffer();
				sb.append(plugin.getName());
				sb.append("  ");
				sb.append(plugin.getVersion());
				sb.append("\n");
				sb.append(plugin.getAuthor());
				sb.append("\n");
				sb.append(getPluginHelp(args));
				return new SendMessage(chatId, sb.toString());
			}
		case "disable":
			if (getPluginByName(args) == null) {
				return new SendMessage(chatId, "That plugin doesn't exist");
			} else {
				if (disablePlugin(args)) {
					return new SendMessage(chatId, "Disabled " + args);
				} else {
					return new SendMessage(chatId, "Could not disable " + args);
				}
			}
		case "enable":
			if (getPluginByName(args) == null) {
				return new SendMessage(chatId, "That plugin doesn't exist");
			} else {
				if (enablePlugin(args)) {
					return new SendMessage(chatId, "Enabled " + args);
				} else {
					return new SendMessage(chatId, "Could not enable" + args);
				}
			}
		default:
			BotPlugin plugin = commands.get(command);
			if(!plugins.get(plugin)) {
				return new SendMessage(chatId, plugin.getName() + " is disabled.");
			}
			BaseRequest request = plugin.onCommand(update);
			if (request == null) {
				return new SendMessage(chatId, "That command does not exist");
			} else {
				return request;
			}
		}
	}

	public BaseRequest handleMessage(Update update) {
		BaseRequest request = null;
		for (BotPlugin plugin : messagePlugins) {
			// Skip if plugin isn't enabled
			if(!plugins.get(plugin)) {
				continue;
			}
			// Make sure to store a non null request if we ever find one
			BaseRequest newRequest = plugin.onMessage(update);
			if (newRequest != null) {
				request = newRequest;
			}
		}
		return request;
	}

	private String getPluginHelp(String pluginName) {
		BotPlugin plugin = getPluginByName(pluginName);
		if (plugin != null) {
			return plugin.getHelp();
		}
		return "No plugin with the name " + pluginName + " found";
	}

	private BotPlugin getPluginByName(String pluginName) {
		for (BotPlugin plugin : plugins.keySet()) {
			if (plugin.getName().equals(pluginName)) {
				return plugin;
			}
		}
		return null;
	}

	private boolean enablePlugin(String pluginName) {
		BotPlugin plugin = getPluginByName(pluginName);
		if (plugin != null) {
			plugin.enable();
			plugins.put(plugin, true);
			return true;
		}
		return false;
	}

	private boolean disablePlugin(String pluginName) {
		BotPlugin plugin = getPluginByName(pluginName);
		if (plugin != null) {
			plugin.disable();
			plugins.put(plugin, false);
			return true;
		}
		return false;
	}

	private Set<String> getCommands() {
		return commands.keySet();
	}

	// TODO
	public boolean reloadPlugins() {
		return true;
	}

	// TODO
	public void processPlugin(BotPlugin plugin) {

	}

}
