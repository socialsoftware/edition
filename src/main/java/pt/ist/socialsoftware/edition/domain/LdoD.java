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
import pt.ist.socialsoftware.edition.mallet.CorpusGenerator;
import pt.ist.socialsoftware.edition.security.LdoDSession;

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

	@Atomic(mode = TxMode.WRITE)
	public void createUsers() throws FileNotFoundException, IOException {
		String[] usernames = { "xpto", "xyz" };
		String[] passwords = { "xpto", "xyz" };
		String[] firstNames = { "António", "Manuel" };
		String[] lastNames = { "Silva", "Ferreira da Silva" };
		String[] emails = { "ars@gmail.com", "xpto@hotmail.com" };
		List<LdoDUser> users = new ArrayList<LdoDUser>();

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		for (int i = 0; i < usernames.length; i++) {
			if (getUser(usernames[i]) == null) {
				users.add(new LdoDUser(this, usernames[i], passwordEncoder
						.encode(passwords[i]), firstNames[i], lastNames[i],
						emails[i]));
			}
		}

		Role userRole = getRole("USER");

		ExpertEdition[] editions = getExpertEditionsSet().toArray(
				new ExpertEdition[4]);

		Random rand = new Random();
		for (LdoDUser user : users) {
			user.addRoles(userRole);
			VirtualEdition virtualEdition = new VirtualEdition(this, user,
					"Ed-" + user.getUsername(), "Edição de "
							+ user.getFirstName() + " " + user.getLastName(),
					new LocalDate(), true, editions[rand.nextInt(4)]);
			user.addSelectedVirtualEditions(virtualEdition);
			CorpusGenerator generator = new CorpusGenerator();
			generator.generate(virtualEdition);
		}

	}
}
