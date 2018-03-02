package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.domain.Dimensions_Base;

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
