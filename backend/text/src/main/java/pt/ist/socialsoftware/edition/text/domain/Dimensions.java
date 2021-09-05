package pt.ist.socialsoftware.edition.text.domain;



public class Dimensions extends Dimensions_Base {

	public Dimensions(Float height, Float width, int position) {
		setHeight(height);
		setWidth(width);
		setPosition(position);
	}

	public void remove() {
		setManuscriptSource(null);
		deleteDomainObject();
	}

}
