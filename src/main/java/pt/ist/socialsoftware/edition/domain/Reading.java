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
		text.setReadingOfFirst(rdg);
		text.setReading(rdg);
		text.setNextText(null);

		for (FragInter fragInter : pendingFragInterps) {
			rdg.addFragInters(fragInter);
		}

		rdg.setPreviousVariationPoint(startPoint);
		rdg.setNextVariationPoint(endPoint);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public void addBeginText(LdoDText text) {
		LdoDText ldoDText = getFirstText();

		text.setNextText(ldoDText);

		this.addText(text);
		this.setFirstText(text);
	}

	public void addEndText(LdoDText text) {
		LdoDText ldoDText = getFirstText();

		while (ldoDText.getNextText() != null) {
			ldoDText = ldoDText.getNextText();
		}

		for (LdoDText tmpText = text; tmpText != null; tmpText = tmpText
				.getNextText()) {
			this.addText(tmpText);
		}

		ldoDText.setNextText(text);
	}

	public void removeOnlyThis() {
		setPreviousVariationPoint(null);
		setNextVariationPoint(null);

		for (FragInter fragInter : getFragIntersSet()) {
			removeFragInters(fragInter);
		}

		setFirstText(null);

		for (LdoDText text : getTextSet()) {
			removeText(text);
		}

		deleteDomainObject();
	}

	public void remove() {
		setPreviousVariationPoint(null);
		setNextVariationPoint(null);

		for (FragInter fragInter : getFragIntersSet()) {
			removeFragInters(fragInter);
		}

		for (LdoDText text : getTextSet()) {
			text.remove();
		}

		deleteDomainObject();
	}

}
