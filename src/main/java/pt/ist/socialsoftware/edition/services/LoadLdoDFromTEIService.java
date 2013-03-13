package pt.ist.socialsoftware.edition.services;

import pt.ist.socialsoftware.edition.loaders.LoadLdoDFromTEI;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class LoadLdoDFromTEIService extends LdoDService {

	@Override
	void execution() throws LdoDException {
		LoadLdoDFromTEI importLdoD = new LoadLdoDFromTEI();
		importLdoD.loadLdoDTEI();
	}

}
