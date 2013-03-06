package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class Reading extends Reading_Base implements GraphElement {

	public enum ReadingType {
		NO_TYPE, ORTHOGRAPHIC
	};

	public Reading() {
		super();

		setType(ReadingType.NO_TYPE);
	}

	public void print() {
		getText().printAll();
		getNextVariationPoint().print();
	}

	public void print(FragInter fragInter) {
		if (getFragIntersSet().contains(fragInter)) {
			getText().printAll();
		}
		getNextVariationPoint().print(fragInter);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public void addText(LdoDText text) {
		LdoDText ldoDText = getText();

		while (ldoDText.getNextText() != null) {
			ldoDText = ldoDText.getNextText();
		}

		ldoDText.setNextText(text);
	}

}
