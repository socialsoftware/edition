package pt.ist.socialsoftware.edition.domain;

public abstract class Source extends Source_Base {

	public Source() {
		super();
	}

	public abstract String getName();

	public abstract String getMetaTextual();

	public void remove() {
		removeFragment();

		for (SourceInter inter : getSourceInters()) {
			removeSourceInters(inter);
		}
	}
}
