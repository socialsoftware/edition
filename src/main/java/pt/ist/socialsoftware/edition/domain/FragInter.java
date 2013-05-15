package pt.ist.socialsoftware.edition.domain;

public abstract class FragInter extends FragInter_Base implements
		Comparable<FragInter> {

	public enum SourceType {
		AUTHORIAL, EDITORIAL, VIRTUAL;
	};

	public FragInter() {
		super();
	}

	public abstract String getShortName();

	public abstract SourceType getSourceType();

	@Override
	public int compareTo(FragInter other) {
		if (getSourceType() != other.getSourceType()) {
			if (getSourceType() == SourceType.EDITORIAL) {
				return -1;
			} else if (getSourceType() == SourceType.AUTHORIAL) {
				return 1;
			} else if ((getSourceType() == SourceType.VIRTUAL)
					&& (other.getSourceType() == SourceType.EDITORIAL)) {
				return 1;
			} else if ((getSourceType() == SourceType.VIRTUAL)
					&& (other.getSourceType() == SourceType.AUTHORIAL)) {
				return 1;
			}
		} else if (getSourceType() == other.getSourceType()) {
			if (getSourceType() == SourceType.EDITORIAL) {
				return ((ExpertEditionInter) this)
						.compareExpertEditionInter((ExpertEditionInter) other);
			} else if (getSourceType() == SourceType.VIRTUAL) {
				return ((VirtualEditionInter) this)
						.compareVirtualEditionInter((VirtualEditionInter) other);
			} else if (getSourceType() == SourceType.AUTHORIAL) {
				return ((SourceInter) this)
						.compareSourceInter((SourceInter) other);
			}
		}
		return 0;
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
