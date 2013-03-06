/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

/**
 * @author ars
 * 
 */
public class WritePlainText4Inter implements GraphVisitor {

	FragInter fragInter = null;
	String result = "";

	public String getResult() {
		return result;
	}

	public WritePlainText4Inter(FragInter fragInter) {
		this.fragInter = fragInter;
		result = fragInter.getName() + ":";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.VariationPoint)
	 */
	@Override
	public void visit(VariationPoint variationPoint) {
		if (variationPoint.getOutReadingsSet().isEmpty()) {
			result = result + "\n";
		} else {

			for (Reading rdg : variationPoint.getOutReadings()) {
				if (rdg.getFragIntersSet().contains(fragInter)) {
					rdg.accept(this);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.Reading)
	 */
	@Override
	public void visit(Reading reading) {
		reading.getText().accept(this);
		reading.getNextVariationPoint().accept(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.SimpleText)
	 */
	@Override
	public void visit(SimpleText text) {
		String separators = ".,?!:;";
		String separator = null;
		String toAdd = text.getValue();

		String firstChar = toAdd.substring(0, 1);

		if (separators.contains(firstChar)) {
			separator = "";
		} else {
			separator = " ";
		}

		result = result + separator + toAdd;

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.LbText)
	 */
	@Override
	public void visit(LbText lbText) {
		result = result + "\n";

		if (lbText.getNextText() != null) {
			lbText.getNextText().accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.FormatText)
	 */
	@Override
	public void visit(FormatText formatText) {
		// TODO Auto-generated method stub
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.PbText)
	 */
	@Override
	public void visit(PbText pbText) {
		// TODO Auto-generated method stub
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.SpaceText)
	 */
	@Override
	public void visit(SpaceText spaceText) {
		result = result + "[SPACE]";

		if (spaceText.getNextText() != null) {
			spaceText.getNextText().accept(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.EmptyText)
	 */
	@Override
	public void visit(EmptyText emptyText) {

		if (emptyText.getNextText() != null) {
			emptyText.getNextText().accept(this);
		}

	}

}
