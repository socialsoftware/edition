package pt.ist.socialsoftware.edition.text.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.text.deleters.ExpertEditionInterDeleter;

import java.util.ArrayList;
import java.util.List;

public class ExpertEditionInter extends ExpertEditionInter_Base {
	public ExpertEditionInter() {
		setNotes("");
		setVolume("");
		setSubNumber("");
	}

	@Override
	public void remove() {
		Remover.remove(this);
		super.remove();
	}

	@Component
	public static class Remover {
		public static ExpertEditionInterDeleter expertEditionInterDeleter;

		@Autowired
		public Remover(ExpertEditionInterDeleter expertEditionInterDeleter) {
			this.expertEditionInterDeleter = expertEditionInterDeleter;
		}

		public static void remove(ExpertEditionInter expertEditionInter) {
			expertEditionInterDeleter.remove(expertEditionInter);
		}

	}

		@Override
	public String getShortName() {
		return getExpertEdition().getEditorShortName();
	}

	@Override
	public String getTitle() {
		String fragTitle = super.getTitle();
		if (fragTitle == null || fragTitle.trim().equals("")) {
			return getFragment().getTitle();
		} else {
			return fragTitle;
		}
	}

	@Override
	public Edition.EditionType getSourceType() {
		return Edition.EditionType.EDITORIAL;
	}

	public int compareExpertEditionInter(ExpertEditionInter other) {
		String myEditor = getExpertEdition().getEditor();
		String otherEditor = other.getExpertEdition().getEditor();

		if (myEditor.equals(otherEditor)) {
			return compareSameEditor(other);
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

	public int compareSameEditor(ExpertEditionInter other) {
		int result = comparePage(other);
		if (result == 0) {
			return compareNumber(other);
		} else {
			return result;
		}
	}

	private int comparePage(ExpertEditionInter other) {
		int result = getVolume().compareTo(other.getVolume());
		if (result == 0) {
			return getStartPage() - other.getStartPage();
		} else {
			return result;
		}
	}

	private int compareNumber(ExpertEditionInter other) {
		int result = getNumber() - other.getNumber();
		if (result == 0) {
			return compareSubNumber(other);
		}
		return result;
	}

	private int compareSubNumber(ExpertEditionInter other) {
		return getSubNumber().compareTo(other.getSubNumber());
	}

	@Override
	public boolean belongs2Edition(Edition edition) {
		return this.getExpertEdition() == edition;
	}

	@Override
	public Edition getEdition() {
		return getExpertEdition();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = new ArrayList<>();
		return listUses;
	}

	@Override
	public String getReference() {
		return Integer.toString(getNumber());
	}

	public String getCompleteNumber() {
		return Integer.toString(getNumber()) + (!getSubNumber().equals("") ? "-" + getSubNumber() : getSubNumber());
	}

}
