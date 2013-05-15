package pt.ist.socialsoftware.edition.domain;

public class ExpertEditionInter extends ExpertEditionInter_Base {

	public ExpertEditionInter() {
		super();
	}

	@Override
	public String getShortName() {
		return getExpertEdition().getEditorShortName();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.EDITORIAL;
	}

	public int compareExpertEditionInter(ExpertEditionInter other) {
		String myEditor = getExpertEdition().getEditor();
		String otherEditor = other.getExpertEdition().getEditor();

		if (myEditor.equals(otherEditor)) {
			return compareNumber(other);
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

	public int compareNumber(ExpertEditionInter other) {
		if (getNumber() == other.getNumber()) {
			return comparePage(getPage(), other.getPage());
		} else if (getNumber() < other.getNumber()) {
			return -1;
		} else
			return 1;
	}

	private int comparePage(int page1, int page2) {
		if (page1 < page2)
			return -1;
		else if (page1 == page2)
			return 0;
		else
			return 1;

	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Título: " + getTitle() + "<br>";

		result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";

		String number = getNumber() == 0 ? "" : Integer.toString(getNumber());
		result = result + "Número: " + number + "<br>";

		result = result + "Página: " + getPage() + "<br>";

		result = result + "Data: " + getDate() + "<br>";

		result = result + "Notas: " + getNotes();

		return result;
	}

	@Override
	public void remove() {
		super.remove();

		removeExpertEdition();

		deleteDomainObject();
	}
}
