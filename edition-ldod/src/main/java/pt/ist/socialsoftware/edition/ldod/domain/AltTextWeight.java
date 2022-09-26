package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.AltTextWeight_Base;

public class AltTextWeight extends AltTextWeight_Base {

	public AltTextWeight() {
		super();
	}

	public AltTextWeight(SegText segText, double weight) {
		setSegText(segText);
		setWeight(weight);
	}


	public void remove() {
		setSegText(null);
		setAltText(null);

		deleteDomainObject();
	}

}
