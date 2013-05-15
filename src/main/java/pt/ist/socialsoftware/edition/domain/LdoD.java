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
			if (edition.hasAcronym(acronym)) {
				return edition;
			}
		}
		return null;
	}

}
