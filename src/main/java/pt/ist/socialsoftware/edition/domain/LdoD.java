package pt.ist.socialsoftware.edition.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joda.time.LocalDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

public class LdoD extends LdoD_Base {

	public static LdoD getInstance() {
		return FenixFramework.getDomainRoot().getLdoD();
	}

	public LdoD() {
		FenixFramework.getDomainRoot().setLdoD(this);
		setNullEdition(new NullEdition());
	}

	public List<ExpertEdition> getSortedExpertEdition() {
		List<ExpertEdition> editions = new ArrayList<ExpertEdition>(
				getExpertEditionsSet());
		Collections.sort(editions);
		return editions;
	}

	public Edition getEdition(String acronym) {
		for (Edition edition : getExpertEditionsSet()) {
			if (edition.getAcronym().equals(acronym)) {
				return edition;
			}
		}

		for (Edition edition : getVirtualEditionsSet()) {
			if (edition.getAcronym().equals(acronym)) {
				return edition;
			}
		}

		return null;
	}

	public LdoDUser getUser(String username) {
		for (LdoDUser user : getUsersSet()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public Role getRole(String rolename) {
		for (Role role : getRolesSet()) {
			if (role.getRolename().equals(rolename))
				return role;
		}
		return null;
	}

	public List<VirtualEdition> getVirtualEditions4User(LdoDUser user,
			LdoDSession session) {
		List<VirtualEdition> manageVE = new ArrayList<VirtualEdition>();
		List<VirtualEdition> selectedVE = new ArrayList<VirtualEdition>();
		List<VirtualEdition> mineVE = new ArrayList<VirtualEdition>();
		List<VirtualEdition> publicVE = new ArrayList<VirtualEdition>();

		// synchronize session
		Set<VirtualEdition> sessionVE = new HashSet<VirtualEdition>(
				session.getSelectedVEs());
		for (VirtualEdition edition : sessionVE) {
			if ((user != null)
					&& !user.getSelectedVirtualEditionsSet().contains(edition)) {
				session.removeSelectedVE(edition.getExternalId(),
						edition.getAcronym());
			} else if ((user == null) && (!edition.getPub())) {
				session.removeSelectedVE(edition.getExternalId(),
						edition.getAcronym());
			}
		}

		if (user == null) {
			selectedVE.addAll(session.getSelectedVEs());
		}

		for (VirtualEdition virtualEdition : getVirtualEditionsSet()) {
			if ((user != null)
					&& (virtualEdition.getSelectedBySet().contains(user))) {
				selectedVE.add(virtualEdition);
			} else if (virtualEdition.getParticipantSet().contains(user)) {
				mineVE.add(virtualEdition);
			} else if (virtualEdition.getPub()
					&& !selectedVE.contains(virtualEdition)) {
				publicVE.add(virtualEdition);
			}
		}

		manageVE.addAll(selectedVE);
		manageVE.addAll(mineVE);
		manageVE.addAll(publicVE);

		return manageVE;
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEdition createVirtualEdition(LdoDUser user, String acronym,
			String title, LocalDate date, boolean pub, Edition usedEdition) {
		return new VirtualEdition(this, user, acronym, title, date, pub,
				usedEdition);
	}

	public void createUsers() throws FileNotFoundException, IOException {
		String[] usernames = { "edna", "balbina", "evandro", "carla",
				"daniela", "samuel", "john", "fernando", "ines", "amelia",
				"vera", "eduarda", "antoniot", "antonioa" };
		String[] firstNames = { "Edna ", "Balbina", "Evandro", "Carla",
				"Daniela", "Samuel", "John", "Fernando", "Inês", "Amélia",
				"Silvéria", "Eduarda", "António", "António" };
		String[] lastNames = { "Boliqueime ", "Oliveira", "Santos", "Araújo",
				"Côrtes Maduro", "Teixeira", "D. Mock", "Mendes", "Mendes",
				"Ribeira", "Ramos", "Mota", "Tavares Lopes",
				"Apolinário Lourenço" };
		String[] emails = { "ednaboliqueime@gmail.com",
				"binaa_oliveira_57@hotmail.com", "esantos.art@gmail.com",
				"cmlintu@hotmail.com", "cortesmaduro@hotmail.com",
				"samfilmt@gmail.com", "jdm11july@gmail.com",
				"faomendes@sapo.pt", "m.inesmariz@gmail.com",
				"ameliarribeira@gmail.com", "ramos.silveria@gmail.com",
				"eduarda.ribeiromota@gmail.com", "atlopes@fl.uc.pt",
				"ant.apolinario@gmail.com" };

		String listPasswords = PropertiesManager.getProperties().getProperty(
				"users.passwords");

		String[] passwords = listPasswords.split(",");

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Random rand = new Random();

		for (int i = 0; i < usernames.length; i++) {
			createUser(usernames[i], passwordEncoder.encode(passwords[i]),
					firstNames[i], lastNames[i], emails[i], rand);
		}

	}

	@Atomic(mode = TxMode.WRITE)
	private void createUser(String username, String password, String firstName,
			String lastName, String email, Random rand)
			throws FileNotFoundException, IOException {
		if (getUser(username) == null) {
			LdoDUser user = new LdoDUser(this, username, password, firstName,
					lastName, email);

			Role userRole = getRole("USER");
			user.addRoles(userRole);

			ExpertEdition[] editions = getExpertEditionsSet().toArray(
					new ExpertEdition[4]);
			VirtualEdition virtualEdition = new VirtualEdition(this, user,
					"Ed-" + user.getUsername(), "Edição de "
							+ user.getFirstName() + " " + user.getLastName(),
					new LocalDate(), true, editions[rand.nextInt(4)]);
			user.addSelectedVirtualEditions(virtualEdition);

			// CorpusGenerator generator = new CorpusGenerator();
			// generator.generate(virtualEdition);
		}
	}
}
