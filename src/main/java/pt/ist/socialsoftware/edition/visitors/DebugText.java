/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

/**
 * Returns the plain text associated with the visited nodes
 * 
 * @author ars
 * 
 */
public class DebugText implements GraphVisitor {

	Set<VariationPoint> visited = new HashSet<VariationPoint>();

	private String result = "";

	private int pointCounter = 0;
	private int readingCounter = 0;

	public String getResult() {
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.VariationPoint)
	 */
	@Override
	public void visit(VariationPoint variationPoint) {

		if (!visited.contains(variationPoint)) {

			visited.add(variationPoint);
			pointCounter++;

			for (Reading rdg : variationPoint.getOutReadings()) {
				result = result + "[P" + pointCounter + "]";

				rdg.accept(this);

				result = result + "\n";

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
		readingCounter++;

		result = result + "[R" + readingCounter + "]";

		reading.getText().accept(this);

		result = result + "[/R" + readingCounter + "]";

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
		result = result + "[ST]" + text.getValue() + "[/ST]";

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
		result = result + "[LBT](BW:" + lbText.getBreakWord() + ",H:"
				+ lbText.getHyphenated() + ")[/LBT]";

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
		result = result + "[FT]" + "[/FT]";
		if (formatText.getPrevText() != null) {
			formatText.getNextText().accept(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.PbText)
	 */
	@Override
	public void visit(PbText pbText) {
		result = result + "[PBT]" + "[/PBT]";
		if (pbText.getPrevText() != null) {
			pbText.getNextText().accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.
	 * socialsoftware.edition.domain.SpaceText)
	 */
	@Override
	public void visit(SpaceText spaceText) {

		result = result + "[SP](QT=" + spaceText.getQuantity() + ",DIM="
				+ spaceText.getDim() + ",UNIT=" + spaceText.getUnit()
				+ ")[/SP]";

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
		result = result + "[ET][/ET]";

		if (emptyText.getPrevText() != null) {
			emptyText.getNextText().accept(this);
		}

	}

}
