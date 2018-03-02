package pt.ist.socialsoftware.edition.core.services;

import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.Fragment;
import pt.ist.socialsoftware.edition.core.shared.exception.LdoDException;

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
