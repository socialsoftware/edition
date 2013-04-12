package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Edition extends Edition_Base {
	public static final String COELHO = "Jacinto Prado Coelho";
	public static final String CUNHA = "Teresa Sobral Cunha";
	public static final String ZENITH = "Richard Zenith";
	public static final String PIZARRO = "Jerónimo Pizarro";

	public boolean hasAcronym(String acronym) {
		if (acronym.equals("JPC") && getEditor().equals(COELHO)) {
			return true;
		} else if (acronym.equals("TSC") && getEditor().equals(CUNHA)) {
			return true;
		} else if (acronym.equals("RZ") && getEditor().equals(ZENITH)) {
			return true;
		} else if (acronym.equals("JP") && getEditor().equals(PIZARRO)) {
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
