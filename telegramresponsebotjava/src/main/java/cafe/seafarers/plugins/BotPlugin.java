package cafe.seafarers.plugins;

public interface BotPlugin {
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
