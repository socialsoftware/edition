package pt.ist.socialsoftware.edition.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author ars
 * 
 */
public class Bootstrap implements WebApplicationInitializer {

	/**
	 * It is invoked from spring mvc user interface
	 * 
	 */
	@Override
	public void onStartup(ServletContext arg0) throws ServletException {
		FenixFramework.initialize(PropertiesManager.getFenixFrameworkConfig());
	}

	private static boolean notInitialized = true;

	/**
	 * It is invoked for JUnit test
	 */
	public static void initDatabase() {
		if (notInitialized) {
			FenixFramework.initialize(PropertiesManager
					.getFenixFrameworkConfig());
		}
		notInitialized = false;
	}

}
