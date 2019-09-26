package cafe.seafarers.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PluginImporter {
	private Set<Class<BotPlugin>> importedPluginClasses;
	private boolean imported;
	private String pluginDirPath;

	public PluginImporter(String pluginDirPath) {
		importedPluginClasses = new HashSet<Class<BotPlugin>>();
		imported = false;
		this.pluginDirPath = pluginDirPath;
	}

	/**
	 * Imports plugins into the Java classpath into importedPluginClasses
	 * 
	 * @param classNames full names of classes to load
	 * @return true if the class is successfully imported
	 */
	public boolean importPlugins(List<String> classNames) {
		try (URLClassLoader cl = new URLClassLoader(new URL[] { new File(pluginDirPath).toURI().toURL() })){
			for(String name : classNames) {
				Class<BotPlugin> c = (Class<BotPlugin>) cl.loadClass(name);
				importedPluginClasses.add(c);
			}
			return true;
		} catch (ClassNotFoundException e) {
			// We received an invalid path...
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns a Set of loaded PluginClasses
	 * 
	 * @return List<Class>
	 */
	public Set<Class<BotPlugin>> getImportedPluginClasses() {
		return importedPluginClasses;
	}

	/**
	 * Returns true if plugins have been imported into classpath
	 * 
	 * @return boolean
	 */
	public boolean isImported() {
		return imported;
	}

	public void setInstantiated() {
		imported = true;		
	}
}
