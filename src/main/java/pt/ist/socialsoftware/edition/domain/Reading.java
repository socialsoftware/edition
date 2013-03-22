package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class Reading extends Reading_Base implements GraphElement {

	public enum ReadingType {
		NO_TYPE, ORTHOGRAPHIC
	};

	public Reading() {
		super();

		setType(ReadingType.NO_TYPE);
	}

	public static void addReading(VariationPoint startPoint,
			VariationPoint endPoint, List<FragInter> pendingFragInterps,
			LdoDText text) {

		Reading rdg = new Reading();
		text.setReading(rdg);
		text.setNextText(null);

		for (FragInter fragInter : pendingFragInterps) {
			rdg.addFragInters(fragInter);
		}

		rdg.setPreviousVariationPoint(startPoint);
		rdg.setNextVariationPoint(endPoint);
	}

	public void remove() {
		removePreviousVariationPoint();
		removeNextVariationPoint();
		for (FragInter fragInter : getFragInters()) {
			removeFragInters(fragInter);
		}
		removeText();

		deleteDomainObject();
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public void addBeginText(LdoDText text) {
		LdoDText ldoDText = getText();

		text.setNextText(ldoDText);
		this.setText(text);
	}

	public void addEndText(LdoDText text) {
		LdoDText ldoDText = getText();

		while (ldoDText.getNextText() != null) {
			ldoDText = ldoDText.getNextText();
		}

		ldoDText.setNextText(text);
	}

}
