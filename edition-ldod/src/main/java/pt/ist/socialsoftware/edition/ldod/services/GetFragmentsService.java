package pt.ist.socialsoftware.edition.ldod.services;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualManager;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class GetFragmentsService extends LdoDService {

	VirtualManager virtualManager = null;

	@Override
	void execution() throws LdoDException {
		virtualManager = VirtualManager.getInstance();

		for (Fragment fragment : virtualManager.getFragmentsSet()) {
			fragment.getTitle();
			fragment.getExternalId();
		}

	}

}
