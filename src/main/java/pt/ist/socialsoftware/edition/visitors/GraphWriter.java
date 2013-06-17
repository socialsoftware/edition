/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

/**
 * Produces a textual representation of the graph representing a fragment text
 * 
 * @author ars
 * 
 */
public class GraphWriter implements GraphVisitor {

	Set<VariationPoint> visited = new HashSet<VariationPoint>();

	private String result = "";

	private final Map<VariationPoint, Integer> pointsNumber = new HashMap<VariationPoint, Integer>();

	private Integer pointCounter = 0;
	private int readingCounter = 0;

	public String getResult() {
		return result;
	}

	/**
	 * Produces a textual representation starting from the given VariationPoint
	 * a line is generated for each path between two adjacent nodes
	 * 
	 * @param variationPoint
	 *            where the generation is done
	 */
	@Override
	public void visit(VariationPoint variationPoint) {

		if (!visited.contains(variationPoint)) {

			visited.add(variationPoint);

			Integer pointNumber = getVariationPointNumber(variationPoint);

			Set<VariationPoint> nextVariationPoints = new HashSet<VariationPoint>();

			for (Reading rdg : variationPoint.getOutReadingsSet()) {
				assert rdg.getNextVariationPoint() != null : "READING DOES NOT HAVE NEXT VARIATION POINT:"
						+ rdg;

				VariationPoint nextPoint = rdg.getNextVariationPoint();
				nextVariationPoints.add(nextPoint);
				Integer nextPointNumber = getVariationPointNumber(nextPoint);

				result = result + "[P" + pointNumber + "]";

				rdg.accept(this);

				result = result + "[/P" + nextPointNumber + "]\n";

			}

			for (VariationPoint nextPoint : nextVariationPoints) {
				nextPoint.accept(this);

			}
		}

	}

	/**
	 * generates a number for each variation point as needed
	 * 
	 * @param variationPoint
	 * @return point number for given variation point
	 */
	private Integer getVariationPointNumber(VariationPoint variationPoint) {

		Integer pointNumber = pointsNumber.get(variationPoint);
		if (pointNumber == null) {
			pointCounter = pointCounter + 1;
			pointNumber = pointCounter;
			pointsNumber.put(variationPoint, pointCounter);
		}

		return pointNumber;
	}

	@Override
	public void visit(Reading reading) {
		readingCounter = readingCounter + 1;

		result = result + "[R" + readingCounter + "]";

		reading.getFirstText().accept(this);

		result = result + "[/R" + readingCounter + "]";
	}

	@Override
	public void visit(SimpleText text) {
		result = result + "[ST]" + text.getValue() + "[/ST]";

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		result = result + "[LBT](BW:" + lbText.getBreakWord() + ",H:"
				+ lbText.getHyphenated() + ")[/LBT]";

		if (lbText.getNextText() != null) {
			lbText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(PbText pbText) {
		result = result + "[PBT]" + "[/PBT]";

		if (pbText.getNextText() != null) {
			pbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(FormatText formatText) {
		result = result + "[FT," + formatText.getRend().toString() + ","
				+ formatText.getOpenClose().toString() + "]";

		if (formatText.getNextText() != null) {
			formatText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(SpaceText spaceText) {
		result = result + "[SP](QT=" + spaceText.getQuantity() + ",DIM="
				+ spaceText.getDim() + ",UNIT=" + spaceText.getUnit()
				+ ")[/SP]";

		if (spaceText.getNextText() != null) {
			spaceText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(EmptyText emptyText) {
		result = result + "[ET][/ET]";

		if (emptyText.getNextText() != null) {
			emptyText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		result = result + "[ADD," + addText.getPlace() + ","
				+ addText.getOpenClose() + "]";

		if (addText.getNextText() != null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		result = result + "[DEL," + delText.getOpenClose() + "]";
		if (delText.getNextText() != null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {
		result = result + "[SUBST," + substText.getOpenClose() + "]";
		if (substText.getNextText() != null) {
			substText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		result = result + "[PAR," + paragraphText.getOpenClose() + "]";
		if (paragraphText.getNextText() != null) {
			paragraphText.getNextText().accept(this);
		}
	}

}
