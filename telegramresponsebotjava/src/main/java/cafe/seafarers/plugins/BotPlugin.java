package cafe.seafarers.plugins;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public interface BotPlugin {
    /**
     * Run every time a user types a command in Telegram mapped to this plugin
     * @return BaseRequest a Telegram object to be sent to a chat room
     */
    public BaseRequest onCommand(Update update);
    
    /**
     * Run every time the bot recieves a message, if this plugin has message
     * access
     * @param update
     * @return
     */
    public BaseRequest onMessage(Update update);

    /**
     * Returns a list of commands that when received by Telegram run this plugin's onCommand method
     * @return Array String
     */
    public String[] getCommands();
    
    /**
     * @return true if this plugin should receive all messages
     */
    public boolean hasMessageAccess();
    
    /**
     * implement if you need an update on a period
     */
    public BaseRequest periodicUpdate();

    /**
     * The name of the plugin
     * @return String plugin name
     */
    public String getName();

    /**
     * The author of the plugin
     * @return String plugin author
     */
    public String getAuthor();

    /**
     * The current version of the plugin
     * @return String plugin version
     */
    public String getVersion();
    
    /**
     * @return String plugin help
     */
    public String getHelp();

    /**
     * Enables the plugin's operation if it is currently disabled
     * @return True if the plugin is successfully enabled
     */
    public boolean enable();

    /**
     * Disables the plugin's operation if it is currently enabled
     * @return True if the plugin is successfully disabled
     */
    public boolean disable();
}
