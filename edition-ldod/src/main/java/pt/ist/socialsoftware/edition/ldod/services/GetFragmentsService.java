package pt.ist.socialsoftware.edition.ldod.services;

import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class GetFragmentsService extends LdoDService {

	LdoD ldoD = null;

	@Override
	void execution() throws LdoDException {
		ldoD = LdoD.getInstance();

		for (Fragment fragment : ldoD.getFragmentsSet()) {
			fragment.getTitle();
			fragment.getExternalId();
		}

	}

}
