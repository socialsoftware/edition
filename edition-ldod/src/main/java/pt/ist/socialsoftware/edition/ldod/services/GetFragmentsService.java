package pt.ist.socialsoftware.edition.ldod.services;

import pt.ist.socialsoftware.edition.text.domain.CollectionManager;
import pt.ist.socialsoftware.edition.text.domain.Fragment;
import pt.ist.socialsoftware.edition.text.exception.LdoDException;

public class GetFragmentsService extends LdoDService {

	CollectionManager collectionManager = null;

	@Override
	void execution() throws LdoDException {
		collectionManager = CollectionManager.getInstance();

		for (Fragment fragment : collectionManager.getFragmentsSet()) {
			fragment.getTitle();
			fragment.getExternalId();
		}

	}

}
