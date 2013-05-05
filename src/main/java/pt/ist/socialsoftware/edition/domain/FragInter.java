package pt.ist.socialsoftware.edition.domain;

public abstract class FragInter extends FragInter_Base implements
		Comparable<FragInter> {

	public enum SourceType {
		AUTHORIAL, EDITORIAL;
	};

	public FragInter() {
		super();
	}

	public abstract String getName();

	public abstract SourceType getSourceType();

	@Override
	public int compareTo(FragInter other) {
		if ((getSourceType() == SourceType.EDITORIAL)
				&& (other.getSourceType() == SourceType.AUTHORIAL)) {
			return -1;
		} else if ((getSourceType() == SourceType.AUTHORIAL)
				&& (other.getSourceType() == SourceType.EDITORIAL)) {
			return 1;
		} else if ((getSourceType() == SourceType.EDITORIAL)
				&& (other.getSourceType() == SourceType.EDITORIAL)) {
			return ((EditionInter) this)
					.compareEditionInter((EditionInter) other);
		} else {
			return ((SourceInter) this).compareSourceInter((SourceInter) other);
		}

	}

	public abstract String getMetaTextual();

	public void remove() {

		removeFragment();
		removeHeteronym();
		for (Category cat : getCategories()) {
			removeCategories(cat);
		}

		for (Reading rdg : getReadings()) {
			removeReadings(rdg);
		}

	}

}
