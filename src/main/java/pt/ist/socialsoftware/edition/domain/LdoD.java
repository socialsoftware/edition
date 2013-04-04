package pt.ist.socialsoftware.edition.domain;

import pt.ist.fenixframework.FenixFramework;

public class LdoD extends LdoD_Base {

	public static LdoD getInstance() {
		return FenixFramework.getRoot();
	}

	public LdoD() {
		super();
	}

	public Edition getEdition(String acronym) {
		for (Edition edition : getEditions()) {
			if (edition.hasAcronym(acronym)) {
				return edition;
			}
		}
		return null;
	}

}
