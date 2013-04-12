package pt.ist.socialsoftware.edition.domain;

public class SourceInter extends SourceInter_Base {

	public SourceInter() {
		super();
	}

	@Override
	public String getName() {
		return getSource().getName();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.AUTHORIAL;
	}

	public int compareSourceInter(SourceInter other) {
		return getName().compareTo(other.getName());
	}
}