package cafe.seafarers.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import cafe.seafarers.Main;
import cafe.seafarers.plugins.BotPlugin;

public class Resources {
	/**
	 * Saves a file with specified content to pluginDir/pluginName/fileName
	 * 
	 * @param plugin   the BotPlugin from which a file is being saved
	 * @param fileName the name of the file to be wrote
	 * @param content  The string to be wrote to a file (could be json, etc.)
	 * @return true if the file is successfully saved
	 */
	public static boolean SaveFile(BotPlugin plugin, String fileName, String content) {
		try {
			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + plugin.getName());

			if (!Files.exists(path)) {
				path.toFile().mkdirs();
			}

			FileWriter fw = new FileWriter(path.toString() + "/" + fileName);
			fw.write(content);
			fw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Saves a file with specified content to pluginDir/directory/fileName
	 * 
	 * @param directory the directory to save to
	 * @param fileName  the name of the file to be wrote
	 * @param content   The string to be wrote to a file (could be json, etc.)
	 * @return true if the file is successfully saved
	 */
	public static boolean SaveFile(String directory, String fileName, String content) {
		try {
			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + directory);

			if (!Files.exists(path)) {
				path.toFile().mkdirs();
			}

			FileWriter fw = new FileWriter(path.toString() + "/" + fileName);
			fw.write(content);
			fw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Loads a file from pluginDir/pluginName/fileName
	 * 
	 * @param plugin   the plugin attempting to load a file
	 * @param fileName the name of the file to be loaded
	 * @return File object to be read by the plugin. May be null
	 */
	public static File LoadFile(BotPlugin plugin, String fileName) {
		try {
			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + plugin.getName());

			if (!Files.exists(path)) {
				return null;
			}

			return new File(path.toString() + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads a file from pluginDir/directory/fileName
	 * 
	 * @param directory the directory to load from
	 * @param fileName  the name of the file to be loaded
	 * @return File object to be read by the plugin. May be null
	 */
	public static File LoadFile(String directory, String fileName) {
		try {
			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + directory);

			if (!Files.exists(path)) {
				return null;
			}

			return new File(path.toString() + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Serializes an object to a file located in pluginDir/plugin_name/filename In
	 * order to use this method the BotPlugin object passed as a parameter must be
	 * implementing Serializable
	 * 
	 * @param plugin   the plugin from which a file must be saved
	 * @param fileName the name of the file to be saved, must include an extension
	 * @param toSave   the Object to be saved
	 * @return true if the file is successfully wrote
	 */
	public static boolean SerializeFile(BotPlugin plugin, String fileName, Object toSave) {
		try {
			if (!(toSave instanceof java.io.Serializable)) {
				return false;
			}

			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + plugin.getName());

			if (!Files.exists(path)) {
				path.toFile().mkdirs();
			}

			FileOutputStream fos = new FileOutputStream(path.toString() + "/" + fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(toSave);
			oos.close();
			return true;
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Serializes an object to a file located in pluginDir/plugin_name/filename In
	 * order to use this method the BotPlugin object passed as a parameter must be
	 * implementing Serializable
	 * 
	 * @param directory the directory to save to
	 * @param fileName  the name of the file to be saved, must include an extension
	 * @param toSave    the Object to be saved
	 * @return true if the file is successfully wrote
	 */
	public static boolean SerializeFile(String directory, String fileName, Object toSave) {
		try {
			if (!(toSave instanceof java.io.Serializable)) {
				return false;
			}

			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + directory);

			if (!Files.exists(path)) {
				path.toFile().mkdirs();
			}

			FileOutputStream fos = new FileOutputStream(path.toString() + "/" + fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(toSave);
			oos.close();
			return true;
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Loads and returns a ObjectInputStream for the given filename After the plugin
	 * is done loading data it should close the file with .close()
	 * 
	 * @param plugin   The plugin object attempting to load a file
	 * @param fileName The filename to be loaded including extension
	 * @return ObjectInputStream for the given file that must be closed after done.
	 *         May return null
	 */
	public static ObjectInputStream LoadSerializedFile(BotPlugin plugin, String fileName) {
		try {
			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + plugin.getName());

			if (!Files.exists(path)) {
				return null;
			}

			FileInputStream fis = new FileInputStream(path.toString() + "/" + fileName);
			return new ObjectInputStream(fis);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads and returns a ObjectInputStream for the given filename After the plugin
	 * is done loading data it should close the file with .close()
	 * 
	 * @param directory The directory to load the file from
	 * @param fileName  The filename to be loaded including extension
	 * @return ObjectInputStream for the given file that must be closed after done.
	 *         May return null
	 */
	public static ObjectInputStream LoadSerializedFile(String directory, String fileName) {
		try {
			String dir = Main.PluginConfigurationDirectory;
			Path path = Paths.get(dir + "/config/" + directory);

			if (!Files.exists(path)) {
				return null;
			}

			FileInputStream fis = new FileInputStream(path.toString() + "/" + fileName);
			return new ObjectInputStream(fis);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads a File contained within the Resources folder Resource must be located
	 * at pluginName/filename
	 * 
	 * @param plugin   The plugin attempting to load a resource
	 * @param fileName The filename of the resource to be loaded
	 * @return File object, null if it doesn't exist
	 */
	public static File LoadResourceFile(BotPlugin plugin, String fileName) {
		try {
			ClassLoader cl = plugin.getClass().getClassLoader();
			return new File(Objects.requireNonNull(cl.getResource(plugin.getName() + "/" + fileName)).getFile());
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Streams a file contained within the Resources folder Resource must be located
	 * at pluginName/Filename
	 * 
	 * @param plugin   The plugin attempting to load a resource
	 * @param fileName The filename of the resource to be loaded
	 * @return BufferedReader containing the resource stream, null if it doesn't
	 *         exist
	 */
	public static BufferedReader LoadFileResourceReader(BotPlugin plugin, String fileName) {
		try {
			ClassLoader cl = plugin.getClass().getClassLoader();
			InputStreamReader isr = new InputStreamReader(
					Objects.requireNonNull(cl.getResourceAsStream(plugin.getName() + "/" + fileName)));
			return new BufferedReader(isr);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
