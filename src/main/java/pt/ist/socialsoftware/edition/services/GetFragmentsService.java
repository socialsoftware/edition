package pt.ist.socialsoftware.edition.services;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class GetFragmentsService extends LdoDService {

	LdoD ldoD = null;

	@Override
	void execution() throws LdoDException {
		ldoD = LdoD.getInstance();

		for (Fragment fragment : ldoD.getFragments()) {
			fragment.getTitle();
			fragment.getExternalId();
		}

	}

}
