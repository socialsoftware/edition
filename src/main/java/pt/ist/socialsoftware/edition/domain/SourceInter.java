package pt.ist.socialsoftware.edition.domain;

public class SourceInter extends SourceInter_Base {

	public SourceInter() {
		super();
	}

	@Override
	public String getShortName() {
		return getSource().getName();
	}

	@Override
	public String getTitle() {
		return getFragment().getTitle();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.AUTHORIAL;
	}

	public int compareSourceInter(SourceInter other) {
		return getSource().getName().compareTo(other.getSource().getName());
	}

	@Override
	public String getMetaTextual() {
		return getSource().getMetaTextual();
	}

	@Override
	public void remove() {
		super.remove();

		setSource(null);

		deleteDomainObject();
	}

}