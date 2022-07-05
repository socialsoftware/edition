package pt.ist.socialsoftware.edition.ldod.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

	private static final Properties properties = new Properties();

	static {
		try {
			properties.load(PropertiesManager.class.getResourceAsStream("/application.properties"));
			properties.load(PropertiesManager.class.getResourceAsStream("/specific.properties"));
			properties.load(PropertiesManager.class.getResourceAsStream("/secrete.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load properties files.", e);
		}
	}

	public static Properties getProperties() {
		return properties;
	}
}