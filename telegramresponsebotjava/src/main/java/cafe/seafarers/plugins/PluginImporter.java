package cafe.seafarers.plugins;

import com.sun.jdi.ClassNotLoadedException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PluginImporter {
    private List<Class> importedPluginClasses;
    private boolean imported;

    public PluginImporter() {
        importedPluginClasses = new ArrayList<Class>();
        imported = false;
    }

    /**
     * Imports Plugin paths into Java classpath and appends them to importedPluginClasses
     * @param pluginClassPaths List<String> of paths to packages to import
     * @return boolean true if successfully imported
     */
    public boolean importPlugins(List<String> pluginClassPaths) {
        if (!imported){
            try {
                for (String s: pluginClassPaths){
                    Class c = Class.forName(s);
                    importedPluginClasses.add(c);
                    //BotPlugin plugin = (BotPlugin) c.getDeclaredConstructor().newInstance()
                }
                imported = true;
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Returns an ArrayList of loaded PluginClasses
     * @return List<Class>
     */
    public List<Class> getImportedPluginClasses(){
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
