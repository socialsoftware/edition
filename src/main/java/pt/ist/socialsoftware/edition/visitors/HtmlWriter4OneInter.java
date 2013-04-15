/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import java.util.HashMap;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FormatText.Rendition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.LdoDText.OpenClose;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

/**
 * @author ars
 * 
 */
public class HtmlWriter4OneInter extends HtmlWriter {

	private final Map<FragInter, Integer> interpsChar = new HashMap<FragInter, Integer>();
	private int totalChar = 0;
	private Boolean isDel = false;

	private FragInter fragInter = null;
	private String transcription = "";
	private String notes = "";
	private int refsCounter = 1;

	public String getTranscription() {
		return transcription + "<br>" + notes;
	}

	public Integer getInterPercentage(FragInter inter) {
		return (interpsChar.get(inter) * 100) / totalChar;
	}

	public HtmlWriter4OneInter(FragInter fragInter) {
		this.fragInter = fragInter;
		transcription = "";

		for (FragInter inter : fragInter.getFragment().getFragmentInter()) {
			interpsChar.put(inter, 0);
		}

	}

	@Override
	public void visit(VariationPoint variationPoint) {
		if (!variationPoint.getOutReadingsSet().isEmpty()) {
			for (Reading rdg : variationPoint.getOutReadings()) {
				if (rdg.getFragIntersSet().contains(fragInter)) {
					rdg.accept(this);
				}
			}
		}
	}

	@Override
	public void visit(Reading reading) {
		reading.getFirstText().accept(this);
		reading.getNextVariationPoint().accept(this);
	}

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

		if (!isDel) {
			totalChar = totalChar + toAdd.length();
			for (FragInter inter : text.getReading().getFragInters()) {
				Integer value = interpsChar.get(inter);
				value = value + toAdd.length();
				interpsChar.put(inter, value);
			}
		}

		transcription = transcription + separator + toAdd;

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		if (lbText.getHyphenated()) {
			transcription = transcription + "-";
		}
		transcription = transcription + "<br>";

		if (lbText.getNextText() != null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(FormatText formatText) {
		if (formatText.getRend() == Rendition.RIGHT) {
			if (formatText.getOpenClose() == OpenClose.OPEN) {
				transcription = transcription + "<div class=\"text-right\">";
			} else {
				transcription = transcription + "</div>";
			}

		} else if (formatText.getRend() == Rendition.LEFT) {
			if (formatText.getOpenClose() == OpenClose.OPEN) {
				transcription = transcription + "<div class=\"text-left\">";
			} else {
				transcription = transcription + "</div>";
			}

		} else if (formatText.getRend() == Rendition.CENTER) {
			if (formatText.getOpenClose() == OpenClose.OPEN) {
				transcription = transcription + "<div class=\"text-center\">";
			} else {
				transcription = transcription + "</div>";
			}
		} else if (formatText.getRend() == Rendition.BOLD) {
			if (formatText.getOpenClose() == OpenClose.OPEN) {
				transcription = transcription + "<strong>";
			} else {
				transcription = transcription + "</strong>";
			}

		} else if (formatText.getRend() == Rendition.RED) {
			if (formatText.getOpenClose() == OpenClose.OPEN) {
				transcription = transcription
						+ "<span style=\"color: rgb(255,0,0);\"";
			} else {
				transcription = transcription + "</span>";
			}
		} else if (formatText.getRend() == Rendition.UNDERLINED) {
			if (formatText.getOpenClose() == OpenClose.OPEN) {
				transcription = transcription
						+ "<div style=\"text-decoration: underline;\">";
			} else {
				transcription = transcription + "</span>";
			}
		}

		if (formatText.getNextText() != null) {
			formatText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {
		// TODO Auto-generated method stub
		assert false;
	}

	@Override
	public void visit(SpaceText spaceText) {
		String separator = "";
		if (spaceText.getDim() == SpaceDim.VERTICAL) {
			separator = "<br>";
		} else if (spaceText.getDim() == SpaceDim.HORIZONTAL) {
			separator = " ";
		}

		for (int i = 0; i < spaceText.getQuantity(); i++) {
			transcription = transcription + separator;
		}

		if (spaceText.getNextText() != null) {
			spaceText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(EmptyText emptyText) {

		if (emptyText.getNextText() != null) {
			emptyText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(AddText addText) {

		switch (addText.getOpenClose()) {
		case CLOSE:
			transcription = transcription + "</a></ins>";
			break;
		case OPEN:
			transcription = transcription + "<ins><a href=\"#"
					+ Integer.toString(refsCounter) + "\">";
			notes = notes + "<a id =\"" + Integer.toString(refsCounter)
					+ "\"></a>" + "[" + Integer.toString(refsCounter) + "] "
					+ "Adicionado - " + addText.getPlace().toString() + "<br>";
			refsCounter++;
			break;
		}

		if (addText.getNextText() != null) {
			addText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(DelText delText) {
		switch (delText.getOpenClose()) {
		case CLOSE:
			isDel = false;
			transcription = transcription + "</a></del>";
			break;
		case OPEN:
			isDel = true;
			transcription = transcription + "<del><a href=\"#"
					+ Integer.toString(refsCounter) + "\">";
			notes = notes + "<a id =\"" + Integer.toString(refsCounter)
					+ "\"></a>" + "[" + Integer.toString(refsCounter) + "] "
					+ "Retirado - " + delText.getHow().toString() + "<br>";
			refsCounter++;
			break;
		}

		if (delText.getNextText() != null) {
			delText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(SubstText substText) {
		switch (substText.getOpenClose()) {
		case CLOSE:
			transcription = transcription + "</span>";
			break;
		case OPEN:
			transcription = transcription
					+ "<span style=\"background-color: rgb(220,220,220);\">";
			break;
		}

		if (substText.getNextText() != null) {
			substText.getNextText().accept(this);
		}

	}

	@Override
	public void visit(ParagraphText paragraphText) {
		switch (paragraphText.getOpenClose()) {
		case CLOSE:
			transcription = transcription + "</p>";
			break;
		case OPEN:
			transcription = transcription + "<p>";
			break;
		}

		if (paragraphText.getNextText() != null) {
			paragraphText.getNextText().accept(this);
		}
	}
}
