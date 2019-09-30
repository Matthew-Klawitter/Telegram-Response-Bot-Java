package cafe.seafarers.config;

import java.io.File;

public class Config {
	private static final String baseURL = "config/";
	
	public static File getPluginConfig(String pluginName) {
		return new File(baseURL + pluginName);
	}
}
