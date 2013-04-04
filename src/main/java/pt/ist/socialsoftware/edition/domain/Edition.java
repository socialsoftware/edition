package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Edition extends Edition_Base {

	public boolean hasAcronym(String acronym) {
		if (acronym.equals("JPC") && getEditor().equals("Jacinto Prado Coelho")) {
			return true;
		} else if (acronym.equals("TSC")
				&& getEditor().equals("Teresa Sobral Cunha")) {
			return true;
		} else if (acronym.equals("RZ") && getEditor().equals("Richard Zenith")) {
			return true;
		} else if (acronym.equals("JP")
				&& getEditor().equals("Jerónimo Pizarro")) {
			return true;
		} else {
			return false;
		}
	}

	public List<EditionInter> getSortedInterps() {
		List<EditionInter> interps = new ArrayList<EditionInter>(
				getEditionInters());

		Collections.sort(interps);

		return interps;
	}

}
