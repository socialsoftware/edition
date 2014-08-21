package pt.ist.socialsoftware.edition.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.springframework.web.WebApplicationInitializer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
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
	@Atomic(mode = TxMode.WRITE)
	public static void initDatabase() {
		if (LdoD.getInstance() == null) {
			new LdoD();
			populateDatabaseUsersAndRoles();
		}
	}

	public static void populateDatabaseUsersAndRoles() {
		// delete directory and all its files if it exists
		String path = PropertiesManager.getProperties().getProperty(
				"corpus.editions.dir");
		File directory = new File(path);
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				// Unable to delete directory
				e.printStackTrace();
			}
		}
		directory.mkdirs();

		LdoD ldod = LdoD.getInstance();

		Role user = new Role(ldod, "USER");
		Role admin = new Role(ldod, "ADMIN");

		// the sha-256 generator http://hash.online-convert.com/sha256-generator
		LdoDUser ars = new LdoDUser(
				ldod,
				"ars",
				"afec24a5413f633e764835ad9651329bae74fdf04096442d11e585ce14a3985f",
				"António", "Rito Silva", "rito.silva@tecnico.ulisboa.pt");
		LdoDUser diego = new LdoDUser(
				ldod,
				"diego",
				"9306e985ad9ba5d90948190bf3a11e5dff0859092f91015f272106fabd51defd",
				"Diego", "Giménez", "dgimenezdm@gmail.com");
		LdoDUser mp = new LdoDUser(
				ldod,
				"mp",
				"57861f3d84c18bdf6cdc7d14d74ea7db958713189db33992f71207a06c3c5a03",
				"Manuel", "Portela", "mportela@fl.uc.pt");
		LdoDUser tiago = new LdoDUser(
				ldod,
				"tiago",
				"de968c78d0e50dbfd5083e1994492548baf4159f7112242acb02f773dc308ac9",
				"Tiago", "Santos", "tiago@tiagosantos.me");
		LdoDUser nuno = new LdoDUser(
				ldod,
				"nuno",
				"057cc85da65d23b0b2971833ec27aab712b84aa6e47c034ab8143e48398205a1",
				"Nuno", "Pinto", "nuno.mribeiro.pinto@gmail.com");
		LdoDUser luis = new LdoDUser(
				ldod,
				"luis",
				"067404fcada78c3f94ef0e9caefc1fc0d47d26568b4ea85f7246f350b311a27d",
				"Luís", "Lucas Pereira", "lmlbpereira@gmail.com");
		LdoDUser andre = new LdoDUser(
				ldod,
				"afs",
				"afec24a5413f633e764835ad9651329bae74fdf04096442d11e585ce14a3985f",
				"André", "Santos", "andrefilipebrazsantos@gmail.com");

		ars.addRoles(user);
		ars.addRoles(admin);

		diego.addRoles(user);
		diego.addRoles(admin);

		mp.addRoles(user);
		mp.addRoles(admin);

		tiago.addRoles(user);
		tiago.addRoles(admin);

		nuno.addRoles(user);
		nuno.addRoles(admin);

		luis.addRoles(user);
		luis.addRoles(admin);

		andre.addRoles(user);
		andre.addRoles(admin);

		VirtualEdition classX = new VirtualEdition(ldod, ars, "ClassX",
				"LdoD Edition of Class X", new LocalDate(), true, null);
		classX.addParticipant(luis);
		classX.addParticipant(mp);
		classX.addParticipant(diego);
		classX.addParticipant(tiago);
		classX.addParticipant(ars);
		classX.addParticipant(andre);
		luis.addSelectedVirtualEditions(classX);
		mp.addSelectedVirtualEditions(classX);
		ars.addSelectedVirtualEditions(classX);
		diego.addSelectedVirtualEditions(classX);
		tiago.addSelectedVirtualEditions(classX);
		nuno.addSelectedVirtualEditions(classX);
		andre.addSelectedVirtualEditions(classX);

		VirtualEdition classY = new VirtualEdition(ldod, ars, "ClassY",
				"LdoD Edition of Class Y", new LocalDate(), false, null);
		classY.addParticipant(luis);
		classY.addParticipant(mp);
		classY.addParticipant(diego);
		classY.addParticipant(tiago);
		classY.addParticipant(ars);
		luis.addSelectedVirtualEditions(classY);
		mp.addSelectedVirtualEditions(classY);
		ars.addSelectedVirtualEditions(classY);
		diego.addSelectedVirtualEditions(classY);
		tiago.addSelectedVirtualEditions(classY);
		nuno.addSelectedVirtualEditions(classY);

		VirtualEdition classW = new VirtualEdition(ldod, ars, "ClassW",
				"LdoD Edition of Class W", new LocalDate(), true, null);
		classW.addParticipant(diego);
		classW.addParticipant(mp);
		classW.addParticipant(luis);
		classW.addParticipant(andre);
		classW.addParticipant(tiago);
		classW.addParticipant(nuno);
		mp.addSelectedVirtualEditions(classW);
		ars.addSelectedVirtualEditions(classW);
		diego.addSelectedVirtualEditions(classW);
		tiago.addSelectedVirtualEditions(classW);
		nuno.addSelectedVirtualEditions(classW);
	}
}
