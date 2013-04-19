package pt.ist.socialsoftware.edition.domain;

public class EditionInter extends EditionInter_Base {

	public EditionInter() {
		super();
	}

	@Override
	public String getName() {
		return getEdition().getEditor();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.EDITORIAL;
	}

	public int compareEditionInter(EditionInter other) {
		String myEditor = getEdition().getEditor();
		String otherEditor = other.getEdition().getEditor();

		if (myEditor.equals(otherEditor)) {
			return compareNumber(other);
		} else if (myEditor.equals(Edition.COELHO)) {
			return -1;
		} else if (otherEditor.equals(Edition.COELHO)) {
			return 1;
		} else if (myEditor.equals(Edition.CUNHA)) {
			return -1;
		} else if (otherEditor.equals(Edition.CUNHA)) {
			return 1;
		} else if (myEditor.equals(Edition.ZENITH)) {
			return -1;
		} else if (otherEditor.equals(Edition.ZENITH)) {
			return 1;
		} else {
			assert false : "To include more code when virtual editions are suported";
			return 0;
		}
	}

	public int compareNumber(EditionInter other) {
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
}
