package pt.ist.socialsoftware.edition.domain;

public class Dimensions extends Dimensions_Base {

	public Dimensions(Float height, Float width) {
		setHeight(height);
		setWidth(width);
	}

	public void remove() {
		setManuscriptSource(null);
		deleteDomainObject();
	}

}
