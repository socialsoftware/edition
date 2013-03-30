package pt.ist.socialsoftware.edition.domain;

public abstract class FragInter extends FragInter_Base {

	public enum SourceType {
		AUTHORIAL, EDITORIAL;
	};

	public FragInter() {
		super();
	}

	public abstract String getName();

	public abstract SourceType getSourceType();

	public String getTranscription() {
		return getFragment().getTranscription(this);
	}

}
