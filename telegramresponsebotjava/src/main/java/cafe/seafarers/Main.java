package cafe.seafarers;

import cafe.seafarers.plugins.BotPlugin;
import cafe.seafarers.plugins.PluginImporter;

import java.lang.reflect.InvocationTargetException;

public class Main {
    /**
     * Main method that launches the bot
     * @param args
     */
    public static void main(String[] args){
       /* PluginImporter importer = new PluginImporter();
        importer.importPlugin("D:\\Documents\\GitHub\\Telegram-Response-Bot-Java\\telegramresponsebotjava\\src\\main\\test\\DummyPlugin.java");

        try {
            for (Class c :  importer.getImportedPluginClasses()){
                // Instantiate and load Plugin
                BotPlugin plugin = (BotPlugin)c.getDeclaredConstructor().newInstance();
                System.out.println(plugin.getName());

            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/
    }
}
