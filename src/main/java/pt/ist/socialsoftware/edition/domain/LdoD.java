package pt.ist.socialsoftware.edition.domain;

import pt.ist.fenixframework.FenixFramework;

public class LdoD extends LdoD_Base {

	public static LdoD getInstance() {
		return FenixFramework.getRoot();
	}

	public LdoD() {
		super();
	}

	public ExpertEdition getExpertEdition(String acronym) {
		for (ExpertEdition edition : getExpertEditions()) {
			if (edition.getAcronym().equals(acronym)) {
				return edition;
			}
		}
		return null;
	}

	public LdoDUser getUser(String username) {
		for (LdoDUser user : getUsers()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
}
