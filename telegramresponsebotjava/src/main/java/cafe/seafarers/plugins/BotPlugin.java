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
     * Returns a list of commands that when received by Telegram run this plugin's onCommand method
     * @return Array String
     */
    public String[] getCommands();

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
