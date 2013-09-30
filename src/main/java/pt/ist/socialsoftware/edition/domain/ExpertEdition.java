package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpertEdition extends ExpertEdition_Base implements
		Comparable<ExpertEdition> {
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

		switch (editor) {
		case COELHO:
			setAcronym("JPC");
			break;
		case CUNHA:
			setAcronym("TSC");
			break;
		case ZENITH:
			setAcronym("RZ");
			break;
		case PIZARRO:
			setAcronym("JP");
			break;
		default:
			assert false : "Nome de editor com erros: " + editor;
		}
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.EDITORIAL;
	}

	@Override
	public int compareTo(ExpertEdition other) {
		String myEditor = getEditor();
		String otherEditor = other.getEditor();

		if (myEditor.equals(otherEditor)) {
			return 0;
		} else if (myEditor.equals(ExpertEdition.COELHO)) {
			return -1;
		} else if (otherEditor.equals(ExpertEdition.COELHO)) {
			return 1;
		} else if (myEditor.equals(ExpertEdition.CUNHA)) {
			return -1;
		} else if (otherEditor.equals(ExpertEdition.CUNHA)) {
			return 1;
		} else if (myEditor.equals(ExpertEdition.ZENITH)) {
			return -1;
		} else if (otherEditor.equals(ExpertEdition.ZENITH)) {
			return 1;
		} else {
			assert false : "To extend when new expert editions are include";
			return 0;
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

	public List<ExpertEditionInter> getSortedInter4Frag(Fragment fragment) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>();

		for (FragInter inter : fragment.getFragmentInterSet()) {
			if ((inter.getSourceType() == SourceType.EDITORIAL)
					&& ((ExpertEditionInter) inter).getExpertEdition() == this) {
				interps.add((ExpertEditionInter) inter);
			}
		}

		Collections.sort(interps);

		return interps;

	}

	public ExpertEditionInter getNextHeteronymInter(ExpertEditionInter inter,
			Heteronym heteronym) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionIntersSet());

		Collections.sort(interps);

		return findNextElementByHeteronym(inter, heteronym, interps);
	}

	public ExpertEditionInter getPrevHeteronymInter(ExpertEditionInter inter,
			Heteronym heteronym) {
		List<ExpertEditionInter> interps = new ArrayList<ExpertEditionInter>(
				getExpertEditionIntersSet());

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

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>(getExpertEditionIntersSet());
	}

	@Override
	public String getReference() {
		return getEditor();
	}

}
