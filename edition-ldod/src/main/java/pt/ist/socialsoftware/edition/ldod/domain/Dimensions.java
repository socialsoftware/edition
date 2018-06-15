package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.Dimensions_Base;

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
