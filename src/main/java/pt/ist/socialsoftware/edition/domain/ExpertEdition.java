package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpertEdition extends ExpertEdition_Base {
	public static final String COELHO = "Jacinto Prado Coelho";
	public static final String CUNHA = "Teresa Sobral Cunha";
	public static final String ZENITH = "Richard Zenith";
	public static final String PIZARRO = "Jerónimo Pizarro";

	public ExpertEdition(LdoD ldoD, String title, String author, String editor,
			String date) {
		setLdoD4Expert(ldoD);
		setTitle(title);
		setAuthor(author);
		setEditor(editor);
		setDate(date);
	}

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

	public String getEditorShortName() {
		if (getEditor().equals(COELHO)) {
			return "Coelho";
		} else if (getEditor().equals(CUNHA)) {
			return "Cunha";
		} else if (getEditor().equals(ZENITH)) {
			return "Zenith";
		} else if (getEditor().equals(PIZARRO)) {
			return "Pizarro";
		} else {
			assert false;
			return null;
		}
	}

	public List<ExpertEditionInter> getSortedInterps() {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionInters());

		Collections.sort(interps);

		return interps;
	}

	public ExpertEditionInter getNextNumberInter(ExpertEditionInter inter,
			int number) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionInters());

		Collections.sort(interps);

		return findNextElementByNumber(inter, number, interps);
	}

	public ExpertEditionInter getPrevNumberInter(ExpertEditionInter inter,
			int number) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionInters());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByNumber(inter, number, interps);
	}

	private ExpertEditionInter findNextElementByNumber(
			ExpertEditionInter inter, int number,
			List<ExpertEditionInter> interps) {
		Boolean stopNext = false;
		for (ExpertEditionInter tmpInter : interps) {
			if (stopNext) {
				return tmpInter;
			}
			if ((tmpInter.getNumber() == number) && tmpInter == inter) {
				stopNext = true;
			}
		}
		return interps.get(0);
	}

	public ExpertEditionInter getNextHeteronymInter(ExpertEditionInter inter,
			Heteronym heteronym) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionInters());

		Collections.sort(interps);

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	public ExpertEditionInter getPrevHeteronymInter(ExpertEditionInter inter,
			Heteronym heteronym) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionInters());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	private ExpertEditionInter findNextElementByHeteronym(
			ExpertEditionInter inter, Heteronym heteronym,
			List<ExpertEditionInter> interps) {
		Boolean stopNext = false;
		for (ExpertEditionInter tmpInter : interps) {
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
