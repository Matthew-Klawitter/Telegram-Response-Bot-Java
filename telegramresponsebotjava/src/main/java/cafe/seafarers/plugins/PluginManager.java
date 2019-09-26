package cafe.seafarers.plugins;

import com.pengrad.telegrambot.TelegramBot;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PluginManager {
    // Set of all BotPlugin objects
    private Set<BotPlugin> plugins;
    // Map of command strings to BotPlugins, used to call Plugins when a valid command is received
    private HashMap<String, BotPlugin> commands;
    // Map of plugins to Booleans, determines if a plugin is currently processable
    private HashMap<BotPlugin, Boolean> enabledPlugins;
    private PluginImporter importer;

    public PluginManager(String pluginPath){
        plugins = new HashSet<BotPlugin>();
        commands = new HashMap<String, BotPlugin>();
        enabledPlugins = new HashMap<BotPlugin, Boolean>();
        importer = new PluginImporter(pluginPath);
    }

    /**
     * Attempts to instantiate loaded plugin classes into objects for use with the Bot
     * @param bot instance of TelegramBot
     * @param pluginPaths List<String> of paths to each plugin that should  be imported
     * @return boolean true if plugins are successfully imported and instantiated
     */
    public boolean loadPlugins(TelegramBot bot, List<String> pluginPaths){
        // Clear out any existing data to facilitate reloading in the future
        plugins.clear();
        commands.clear();
        enabledPlugins.clear();

        if (importer.importPlugins(pluginPaths)){
        	importer.setInstantiated();
            return instantiateImported();
        }
        return false;
    }

    /**
     * Instantiates all imported plugins in importer.getImportedPluginClasses() and adds object instances to plugins Set
     * @return true if plugins are successfully instantiated and added to plugins Set
     */
    private boolean instantiateImported(){
        try {
            for (Class<BotPlugin> c :  importer.getImportedPluginClasses()){
                // Instantiate and load Plugin
                BotPlugin plugin = c.getConstructor().newInstance();
                System.out.println(plugin.getName());
                plugins.add(plugin);

                // Map commands to Plugin
                for (String command : plugin.getCommands()){
                    commands.put(command, plugin);
                }

                // Enable the plugin for operation
                enabledPlugins.put(plugin, true);
            }
            return true;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reloadPlugins(TelegramBot bot){
        return true;
    }

    public void processPlugin(String pluginName){

    }

    public String getPluginHelp(String pluginName){
        return "";
    }

    public boolean enablePlugin(String pluginName){
        return true;
    }

    public boolean disablePlugin(String pluginName){
        return true;
    }

    public List<String> getCommands(){
        return null;
    }
}
