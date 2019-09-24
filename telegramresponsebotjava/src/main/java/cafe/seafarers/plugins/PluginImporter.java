package cafe.seafarers.plugins;

import com.sun.jdi.ClassNotLoadedException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PluginImporter {
    private Set<Class> importedPluginClasses;
    private boolean imported;

    public PluginImporter() {
        importedPluginClasses = new HashSet<Class>();
        imported = false;
    }

    /**
     * Imports plugin paths into Java classpath and appends them to importedPluginClasses
     * This should only be run once unless an exception occurs
     * @param pluginClassPaths Set<String> of paths to packages to import
     * @return boolean true if successfully imported, false if already imported
     */
    public boolean importPlugins(List<String> pluginClassPaths) {
        if (!imported){
            for (String s: pluginClassPaths){
                try {
                    Class c = Class.forName(s);
                    importedPluginClasses.add(c);
                }
                catch (ClassNotFoundException e) {
                    // We received an invalid path...
                    e.printStackTrace();
                    importedPluginClasses.clear();
                    return false;
                }
            }
            imported = true;
        }
        return false;
    }

    /**
     * Imports a single plugin into the Java classpath and appends it to importedPluginClasses
     * @param pluginClassPath full path to the plugin class
     * @return true if the class is successfully imported
     */
    public boolean importPlugin(String pluginClassPath) {
        try {
            Class c = Class.forName(pluginClassPath);
            if (!importedPluginClasses.contains(c)){
                importedPluginClasses.add(c);
                return true;
            }
            return false;
        }
        catch (ClassNotFoundException e) {
            // We received an invalid path...
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns a Set of loaded PluginClasses
     * @return List<Class>
     */
    public Set<Class> getImportedPluginClasses(){
        return importedPluginClasses;
    }

    /**
     * Returns true if plugins have been imported into classpath
     * @return boolean
     */
    public boolean isImported(){
        return imported;
    }
}
