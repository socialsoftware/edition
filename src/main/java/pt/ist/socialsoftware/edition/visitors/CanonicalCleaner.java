package pt.ist.socialsoftware.edition.visitors;

import java.util.HashSet;
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

public class CanonicalCleaner implements GraphVisitor {

	Set<VariationPoint> visited = new HashSet<VariationPoint>();

	@Override
	public void visit(VariationPoint variationPoint) {
		if (!visited.contains(variationPoint)) {
			visited.add(variationPoint);

			for (Reading rdg : variationPoint.getOutReadings()) {
				rdg.accept(this);
			}
		}
	}

	@Override
	public void visit(Reading reading) {
		VariationPoint previousPoint = reading.getPreviousVariationPoint();
		VariationPoint nextPoint = reading.getNextVariationPoint();
		if (previousPoint != null
				&& previousPoint.getInReadingsCount() == 1
				&& previousPoint.getOutReadingsCount() == 1
				&& previousPoint.getInReadingsInters().equals(
						previousPoint.getOutReadingsInters())) {

			Reading previousRdg = previousPoint.getInReadings().get(0);

			previousRdg.addEndText(reading.getText());
			previousRdg.setNextVariationPoint(nextPoint);

			reading.remove();
			previousPoint.remove();

		}
		nextPoint.accept(this);
	}

	@Override
	public void visit(SimpleText text) {
		assert false;
	}

	@Override
	public void visit(LbText lbText) {
		assert false;

	}

	@Override
	public void visit(FormatText formatText) {
		assert false;

	}

	@Override
	public void visit(PbText pbText) {
		assert false;

	}

	@Override
	public void visit(SpaceText spaceText) {
		assert false;

	}

	@Override
	public void visit(EmptyText emptyText) {
		assert false;

	}

	@Override
	public void visit(AddText addText) {
		assert false;
	}

	@Override
	public void visit(DelText delText) {
		assert false;

	}

	@Override
	public void visit(SubstText substText) {
		assert false;

	}

	@Override
	public void visit(ParagraphText paragraphText) {
		assert false;

	}

}
