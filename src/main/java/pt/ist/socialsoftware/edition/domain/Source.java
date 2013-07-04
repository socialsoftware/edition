package pt.ist.socialsoftware.edition.domain;

public abstract class Source extends Source_Base {

	public abstract String getName();

	public abstract String getMetaTextual();

	public void remove() {
		setFragment(null);

		for (SourceInter inter : getSourceIntersSet()) {
			removeSourceInters(inter);
		}

		deleteDomainObject();
	}
}
