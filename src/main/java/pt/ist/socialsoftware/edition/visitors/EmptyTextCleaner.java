package pt.ist.socialsoftware.edition.visitors;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

public class EmptyTextCleaner implements GraphVisitor {

	Set<VariationPoint> visited = new HashSet<VariationPoint>();

	@Override
	public void visit(VariationPoint variationPoint) {
		if (!visited.contains(variationPoint)) {
			visited.add(variationPoint);

			for (Reading rdg : variationPoint.getOutReadings()) {
				rdg.accept(this);
			}

			for (Reading rdg : variationPoint.getOutReadings()) {
				rdg.getNextVariationPoint().accept(this);
			}

		}

	}

	@Override
	public void visit(Reading reading) {

		if (reading.getFirstText() instanceof EmptyText) {
			EmptyText text = (EmptyText) reading.getFirstText();
			if (!text.getIsBreak()) {
				assert text.getNextText() == null : "CLEANING EMPTY-TEXT FAILED";

				Reading nextReading = reading.getNextVariationPoint()
						.getOutReadings().get(0);

				for (Reading prevReading : reading.getPreviousVariationPoint()
						.getInReadings()) {

					SimpleText prevText = (SimpleText) prevReading
							.getFirstText();
					SimpleText nextText = (SimpleText) nextReading
							.getFirstText();
					SimpleText composedText = new SimpleText(
							prevText.getValue() + nextText.getValue());

					Reading composedReading = new Reading();
					composedReading.addBeginText(composedText);

					VariationPoint prevPoint = prevReading
							.getPreviousVariationPoint();
					VariationPoint nextPoint = nextReading
							.getNextVariationPoint();

					for (FragInter fragInter : reading.getFragInters()) {
						if (prevReading.getFragIntersSet().contains(fragInter)) {
							composedReading.addFragInters(fragInter);
							prevReading.removeFragInters(fragInter);
							nextReading.removeFragInters(fragInter);
							reading.removeFragInters(fragInter);
						}
					}

					composedReading.setPreviousVariationPoint(prevPoint);
					composedReading.setNextVariationPoint(nextPoint);
				}
				text.remove();
				reading.remove();
			}
		}

	}

	@Override
	public void visit(SimpleText text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
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
