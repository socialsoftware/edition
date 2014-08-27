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

		// the bcrypt generator
		// https://www.dailycred.com/blog/12/bcrypt-calculator
		LdoDUser ars = new LdoDUser(ldod, "ars",
				"$2a$11$Y0PQlyE20CXaI9RGhtjZJeTM/0.RUyp2kO/YAJI2P2FeINDEUxd2m",
				"António", "Rito Silva", "rito.silva@tecnico.ulisboa.pt");
		LdoDUser diego = new LdoDUser(ldod, "diego",
				"$2a$11$b3rI6cl/GOzVqOKUOWSQQ.nTJFn.s8a/oALV.YOWoUZu6HZGvyCXu",
				"Diego", "Giménez", "dgimenezdm@gmail.com");
		LdoDUser mp = new LdoDUser(ldod, "mp",
				"$2a$11$Nd6tuFTBZV3ej02xJcJhUOZtHKsc888UOBXFz9jDYDBs/EHQIIP26",
				"Manuel", "Portela", "mportela@fl.uc.pt");
		LdoDUser tiago = new LdoDUser(ldod, "tiago",
				"$2a$11$GEa2gLrEweOV5b.fzTi5ueg.s9h2wP/SmRUt2mCvU.Ra7BxgkPVci",
				"Tiago", "Santos", "tiago@tiagosantos.me");
		LdoDUser nuno = new LdoDUser(ldod, "nuno",
				"$2a$11$ICywhcOlcgbkWmi2zxYRi./AjLrz4Vieb25TBUeK3FsMwYmSPTcMu",
				"Nuno", "Pinto", "nuno.mribeiro.pinto@gmail.com");
		LdoDUser luis = new LdoDUser(ldod, "luis",
				"$2a$11$c0Xrwz/gw0tBoMo3o1AG3.boCszoGOXyDWZ5z2vSY259/RDLK4ZDi",
				"Luís", "Lucas Pereira", "lmlbpereira@gmail.com");
		LdoDUser andre = new LdoDUser(ldod, "afs",
				"$2a$11$na24dttCBjjT5uVT0mBCb.MlDdCGHwu3w6tRTqf5OD9QAsIPYJzfu",
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
				"LdoD Edition of Class X", new LocalDate(), false, null);
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
				"LdoD Edition of Class W", new LocalDate(), false, null);
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
