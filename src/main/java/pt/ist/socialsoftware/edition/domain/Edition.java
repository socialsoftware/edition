package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateValueException;

public abstract class Edition extends Edition_Base {

	public Edition() {
		super();
	}

	@Override
	public void setAcronym(String acronym) {
		if ((getAcronym() != null && !getAcronym().equals(acronym))
				|| getAcronym() == null) {
			for (ExpertEdition edition : LdoD.getInstance()
					.getExpertEditionsSet()) {
				if (acronym.equals(edition.getAcronym())) {
					throw new LdoDDuplicateValueException();
				}
			}

			for (VirtualEdition edition : LdoD.getInstance()
					.getVirtualEditionsSet()) {
				if (acronym.equals(edition.getAcronym())) {
					throw new LdoDDuplicateValueException();
				}
			}
		}

		super.setAcronym(acronym);
	}

}
