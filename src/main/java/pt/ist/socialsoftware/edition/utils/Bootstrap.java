package pt.ist.socialsoftware.edition.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import jvstm.Transaction;

import org.springframework.web.WebApplicationInitializer;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Role;

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

		populateDatabaseUsersAndRoles();
	}

	private static boolean notInitialized = true;

	/**
	 * It is invoked for JUnit test
	 */
	public static void initDatabase() {
		if (notInitialized) {
			FenixFramework.initialize(PropertiesManager
					.getFenixFrameworkConfig());

			populateDatabaseUsersAndRoles();
		}
		notInitialized = false;
	}

	public static void populateDatabaseUsersAndRoles() {
		Transaction.begin();
		LdoD ldoD = FenixFramework.getRoot();

		if (ldoD.getUsersCount() == 0) {

			Role user = new Role(ldoD, "USER");
			Role admin = new Role(ldoD, "ADMIN");

			LdoDUser ars = new LdoDUser(ldoD, "ars",
					"afec24a5413f633e764835ad9651329bae74fdf04096442d11e585ce14a3985f");
			LdoDUser diego = new LdoDUser(ldoD, "diego",
					"9306e985ad9ba5d90948190bf3a11e5dff0859092f91015f272106fabd51defd");
			LdoDUser mp = new LdoDUser(ldoD, "mp",
					"57861f3d84c18bdf6cdc7d14d74ea7db958713189db33992f71207a06c3c5a03");
			LdoDUser tim = new LdoDUser(ldoD, "tim",
					"d64785a0a97001a90cbe50bd01d9767fd4a3cb0e76b98bd3c92939ff7feaa2ce");
			LdoDUser carlos = new LdoDUser(ldoD, "carlos",
					"cf7fa6738933ffe9f792359fba2cabcd1c36874c5af0994aee01fde5cc19015c");
			LdoDUser ecscw = new LdoDUser(ldoD, "ecscw",
					"8859415df64d58bab8c12dd8d8c20cf710cab143a37561d6802a551555ffd377");

			ars.addRoles(user);
			ars.addRoles(admin);

			diego.addRoles(user);
			diego.addRoles(admin);

			mp.addRoles(user);
			mp.addRoles(admin);

			tim.addRoles(user);
			tim.addRoles(admin);

			carlos.addRoles(user);
			carlos.addRoles(admin);

			ecscw.addRoles(user);
			ecscw.addRoles(admin);
		}

		Transaction.commit();
	}

}
