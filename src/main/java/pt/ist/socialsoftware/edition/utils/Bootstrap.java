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
		LdoD ldod = LdoD.getInstance();

		Role user = new Role(ldod, "USER");
		Role admin = new Role(ldod, "ADMIN");

		LdoDUser ars = new LdoDUser(ldod, "ars",
				"afec24a5413f633e764835ad9651329bae74fdf04096442d11e585ce14a3985f");
		LdoDUser diego = new LdoDUser(ldod, "diego",
				"9306e985ad9ba5d90948190bf3a11e5dff0859092f91015f272106fabd51defd");
		LdoDUser mp = new LdoDUser(ldod, "mp",
				"57861f3d84c18bdf6cdc7d14d74ea7db958713189db33992f71207a06c3c5a03");
		LdoDUser tim = new LdoDUser(ldod, "tim",
				"d64785a0a97001a90cbe50bd01d9767fd4a3cb0e76b98bd3c92939ff7feaa2ce");
		LdoDUser carlos = new LdoDUser(ldod, "carlos",
				"cf7fa6738933ffe9f792359fba2cabcd1c36874c5af0994aee01fde5cc19015c");
		LdoDUser llc = new LdoDUser(ldod, "llc",
				"2429c113c25f33be41309c940d0b3dfebd7104f92aede07c47d95754800cd597");
		LdoDUser tiago = new LdoDUser(ldod, "tiago",
				"de968c78d0e50dbfd5083e1994492548baf4159f7112242acb02f773dc308ac9");

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

		llc.addRoles(user);
		llc.addRoles(admin);

		tiago.addRoles(user);
		tiago.addRoles(admin);

		VirtualEdition classX = new VirtualEdition(ldod, ars, "ClassX",
				"LdoD Edition of Class X", "12/12/2012", true);
		classX.addParticipant(llc);
		llc.addSelectedVirtualEditions(classX);

		VirtualEdition classY = new VirtualEdition(ldod, ars, "ClassY",
				"LdoD Edition of Class Y", "01/12/2012", false);
		classY.addParticipant(llc);
		llc.addSelectedVirtualEditions(classY);

		VirtualEdition classW = new VirtualEdition(ldod, ars, "ClassW",
				"LdoD Edition of Class W", "01/01/2013", true);
		classW.addParticipant(diego);
		classW.addParticipant(mp);
		classW.addParticipant(tim);
		classW.addParticipant(carlos);
		classW.addParticipant(tiago);
		mp.addSelectedVirtualEditions(classW);
		diego.addSelectedVirtualEditions(classW);
		tiago.addSelectedVirtualEditions(classW);
	}
}
