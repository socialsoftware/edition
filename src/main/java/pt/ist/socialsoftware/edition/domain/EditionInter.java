package pt.ist.socialsoftware.edition.domain;

public class EditionInter extends EditionInter_Base implements
		Comparable<EditionInter> {

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

	@Override
	public int compareTo(EditionInter other) {
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
}
