package pt.ist.socialsoftware.edition.domain;

public class EditionInter extends EditionInter_Base {

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

}
