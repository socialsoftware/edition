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

	public EditionInter getNextNumberInter(EditionInter inter, int number) {
		List<EditionInter> interps = new ArrayList<EditionInter>(
				getEditionInters());

		Collections.sort(interps);

		return findNextElementByNumber(inter, number, interps);
	}

	public EditionInter getPrevNumberInter(EditionInter inter, int number) {
		List<EditionInter> interps = new ArrayList<EditionInter>(
				getEditionInters());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByNumber(inter, number, interps);
	}

	private EditionInter findNextElementByNumber(EditionInter inter,
			int number, List<EditionInter> interps) {
		Boolean stopNext = false;
		for (EditionInter tmpInter : interps) {
			if (stopNext) {
				return tmpInter;
			}
			if ((tmpInter.getNumber() == number) && tmpInter == inter) {
				stopNext = true;
			}
		}
		return interps.get(0);
	}

	public EditionInter getNextHeteronymInter(EditionInter inter,
			Heteronym heteronym) {
		List<EditionInter> interps = new ArrayList<EditionInter>(
				getEditionInters());

		Collections.sort(interps);

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	public EditionInter getPrevHeteronymInter(EditionInter inter,
			Heteronym heteronym) {
		List<EditionInter> interps = new ArrayList<EditionInter>(
				getEditionInters());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	private EditionInter findNextElementByHeteronym(EditionInter inter,
			Heteronym heteronym, List<EditionInter> interps) {
		Boolean stopNext = false;
		for (EditionInter tmpInter : interps) {
			if (stopNext) {
				return tmpInter;
			}
			if ((tmpInter.getHeteronym() == heteronym) && tmpInter == inter) {
				stopNext = true;
			}
		}
		return interps.get(0);
	}

}
