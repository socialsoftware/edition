package pt.ist.socialsoftware.edition.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.socialsoftware.edition.core.domain.ExpertEdition_Base;

public class ExpertEdition extends ExpertEdition_Base implements Comparable<ExpertEdition> {
	public ExpertEdition(LdoD ldoD, String title, String author, String editor, LocalDate date) {
		setTitle(title);
		setAuthor(author);
		setEditor(editor);
		setDate(date);
		setPub(true);

		switch (editor) {
		case Edition.COELHO_EDITION_NAME:
			setAcronym(Edition.COELHO_EDITION_ACRONYM);
			break;
		case Edition.CUNHA_EDITION_NAME:
			setAcronym(Edition.CUNHA_EDITION_ACRONYM);
			break;
		case Edition.ZENITH_EDITION_NAME:
			setAcronym(Edition.ZENITH_EDITION_ACRONYM);
			break;
		case Edition.PIZARRO_EDITION_NAME:
			setAcronym(Edition.PIZARRO_EDITION_ACRONYM);
			break;
		default:
			assert false : "Nome de editor com erros: " + editor;
		}

		setLdoD4Expert(ldoD);
	}

	@Override
	public Edition.EditionType getSourceType() {
		return Edition.EditionType.EDITORIAL;
	}

	@Override
	public int compareTo(ExpertEdition other) {
		String myEditor = getEditor();
		String otherEditor = other.getEditor();

		if (myEditor.equals(otherEditor)) {
			return 0;
		} else if (myEditor.equals(Edition.COELHO_EDITION_NAME)) {
			return -1;
		} else if (otherEditor.equals(Edition.COELHO_EDITION_NAME)) {
			return 1;
		} else if (myEditor.equals(Edition.CUNHA_EDITION_NAME)) {
			return -1;
		} else if (otherEditor.equals(Edition.CUNHA_EDITION_NAME)) {
			return 1;
		} else if (myEditor.equals(Edition.ZENITH_EDITION_NAME)) {
			return -1;
		} else if (otherEditor.equals(Edition.ZENITH_EDITION_NAME)) {
			return 1;
		} else {
			assert false : "To extend when new expert editions are include";
			return 0;
		}
	}

	public String getEditorShortName() {
		if (getEditor().equals(Edition.COELHO_EDITION_NAME)) {
			return "Coelho";
		} else if (getEditor().equals(Edition.CUNHA_EDITION_NAME)) {
			return "Cunha";
		} else if (getEditor().equals(Edition.ZENITH_EDITION_NAME)) {
			return "Zenith";
		} else if (getEditor().equals(Edition.PIZARRO_EDITION_NAME)) {
			return "Pizarro";
		} else {
			assert false;
			return null;
		}
	}

	public List<ExpertEditionInter> getSortedInter4Frag(Fragment fragment) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>();

		for (FragInter inter : fragment.getFragmentInterSet()) {
			if ((inter.getSourceType() == Edition.EditionType.EDITORIAL)
					&& ((ExpertEditionInter) inter).getExpertEdition() == this) {
				interps.add((ExpertEditionInter) inter);
			}
		}

		Collections.sort(interps);

		return interps;

	}

	public ExpertEditionInter getNextHeteronymInter(ExpertEditionInter inter, Heteronym heteronym) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(getExpertEditionIntersSet());

		Collections.sort(interps);

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	public ExpertEditionInter getPrevHeteronymInter(ExpertEditionInter inter, Heteronym heteronym) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(getExpertEditionIntersSet());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	private ExpertEditionInter findNextElementByHeteronym(ExpertEditionInter inter, Heteronym heteronym,
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

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>(getExpertEditionIntersSet());
	}

	@Override
	public String getReference() {
		return getEditor();
	}

	public ExpertEditionInter getFirstInterpretation() {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(getExpertEditionIntersSet());

		Collections.sort(interps);

		return interps.get(0);
	}
}
