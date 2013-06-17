package pt.ist.socialsoftware.edition.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

	private static final Properties properties = new Properties();

	static {
		try {
			properties.load(PropertiesManager.class
					.getResourceAsStream("/configuration.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load properties files.", e);
		}
	}
}