package pt.ist.socialsoftware.edition.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Role;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

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
		initDatabase();
	}

	/**
	 * It is invoked for JUnit test
	 */
	@Atomic
	public static void initDatabase() {
		if (LdoD.getInstance() == null) {
			new LdoD();
			populateDatabaseUsersAndRoles();
		}
	}

	public static void populateDatabaseUsersAndRoles() {
		LdoD ldoD = LdoD.getInstance();

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
		LdoDUser llc = new LdoDUser(ldoD, "llc",
				"2429c113c25f33be41309c940d0b3dfebd7104f92aede07c47d95754800cd597");

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

		llc.addRoles(user);
		llc.addRoles(admin);

		VirtualEdition classX = new VirtualEdition(ldoD, ars, "ClassX",
				"LdoD Edition of Class X", "12/12/2012", true);
		classX.addParticipant(ecscw);
		classX.addParticipant(llc);
		llc.addSelectedVirtualEditions(classX);

		VirtualEdition classY = new VirtualEdition(ldoD, ars, "ClassY",
				"LdoD Edition of Class Y", "01/12/2012", false);
		classY.addParticipant(ecscw);
		classY.addParticipant(llc);
		llc.addSelectedVirtualEditions(classY);

		VirtualEdition classW = new VirtualEdition(ldoD, ars, "ClassW",
				"LdoD Edition of Class W", "01/01/2013", true);
		classW.addParticipant(diego);
		classW.addParticipant(mp);
		classW.addParticipant(tim);
		classW.addParticipant(carlos);
		mp.addSelectedVirtualEditions(classW);
		diego.addSelectedVirtualEditions(classW);
	}
}
