package pt.ist.socialsoftware.edition.domain;

public abstract class FragInter extends FragInter_Base {

	public FragInter() {
		super();
	}

	public abstract void print();

	public abstract String getName();

	public String getTranscription() {
		return getFragment().getTranscription(this);
	}

}
