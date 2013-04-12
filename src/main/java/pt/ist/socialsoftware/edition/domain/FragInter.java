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

	public String getTranscription() {
		return getFragment().getTranscription(this).trim();
	}

	@Override
	public int compareTo(FragInter other) {
		if ((this instanceof EditionInter) && (other instanceof SourceInter)) {
			return -1;
		} else if ((this instanceof EditionInter)
				&& (other instanceof EditionInter)) {
			return ((EditionInter) this)
					.compareEditionInter((EditionInter) other);
		} else {
			return ((SourceInter) this).compareSourceInter((SourceInter) other);
		}
	}

}
