package pt.ist.socialsoftware.edition.domain;

public class EditionInter extends EditionInter_Base {

	public EditionInter() {
		super();
	}

	@Override
	public void print() {
		System.out.print(getEdition().getEditor() + ":");

	}

	@Override
	public String getName() {
		return getEdition().getEditor();
	}

}
