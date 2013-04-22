package pt.ist.socialsoftware.edition.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.LdoDText;
import pt.ist.socialsoftware.edition.domain.LdoDText.OpenClose;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

public class HtmlWriter4OneInter extends HtmlWriter {

	private final List<LdoDText> pendingWrite = new ArrayList<LdoDText>();

	private Boolean displayDel = false;
	private Boolean highlightIns = true;
	private Boolean highlightSubst = false;
	private Boolean showNotes = true;

	private final Map<FragInter, Integer> interpsChar = new HashMap<FragInter, Integer>();
	private int totalChar = 0;
	private Boolean isBreakWord = true;
	private boolean isDel = false;

	private String notes = "";
	private int refsCounter = 1;

	@Override
	public String getTranscription() {
		return showNotes ? transcription + "<br>" + notes : transcription;
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

	public void write() {
		visit(fragInter.getFragment().getVariationPoint());
	}

	public void write(Boolean displayDel, Boolean highlightIns,
			Boolean highlightSubst, Boolean showNotes) {
		this.displayDel = displayDel;
		this.highlightIns = highlightIns;
		this.highlightSubst = highlightSubst;
		this.showNotes = showNotes;
		visit(fragInter.getFragment().getVariationPoint());
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

		if (separators.contains(firstChar) || !isBreakWord) {
			separator = "";
			isBreakWord = true;
		} else {
			separator = " ";
			System.out.println("SEPARADOR");
		}

		// deleted parts are not included in the statistics
		if (!isDel) {
			totalChar = totalChar + toAdd.length();
			for (FragInter inter : text.getReading().getFragInters()) {
				Integer value = interpsChar.get(inter);
				value = value + toAdd.length();
				interpsChar.put(inter, value);
			}
		}

		OpenClose state = OpenClose.NO;
		for (LdoDText pText : pendingWrite) {
			// the separator should not be affected by formatting and it is
			// written before open
			OpenClose pState = pText.getOpenClose();
			if (pState == OpenClose.CLOSE) {
				state = OpenClose.CLOSE;
			} else if ((pState == OpenClose.OPEN) && (state == OpenClose.CLOSE)) {
				transcription = transcription + separator;
				state = OpenClose.OPEN;
			}

			// writing notes needs to increment counter
			String reference = pText.writeReference(refsCounter);
			if (showNotes && (reference != null)) {
				if (pState == OpenClose.OPEN) {
					// <del><a href= ....>
					transcription = transcription + pText.writeHtml()
							+ reference;
				} else {
					// </a></del>
					transcription = transcription + reference
							+ pText.writeHtml();
				}
				notes = notes + pText.writeNote(refsCounter);
				refsCounter = refsCounter + 1;
			} else {
				transcription = transcription + pText.writeHtml();
			}
		}
		pendingWrite.clear();

		if (state == OpenClose.OPEN) {
			transcription = transcription + toAdd;
		} else {
			transcription = transcription + separator + toAdd;
		}

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText text) {
		transcription = transcription + text.writeHtml();

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText text) {
		transcription = transcription + text.writeHtml();
	}

	@Override
	public void visit(FormatText text) {
		pendingWrite.add(text);

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText text) {
		transcription = transcription + text.writeHtml();

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText text) {
		if (highlightIns) {
			pendingWrite.add(text);
		}

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}

	}

	@Override
	public void visit(DelText text) {
		if (displayDel) {
			pendingWrite.add(text);

			switch (text.getOpenClose()) {
			case CLOSE:
				isDel = false;
				break;
			case OPEN:
				isDel = true;
				break;
			case NO:
				assert false;
				break;
			default:
				break;
			}

		}

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText text) {
		if (displayDel && highlightSubst) {
			pendingWrite.add(text);
		}

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(EmptyText text) {
		isBreakWord = text.getIsBreak();

		System.out.println(isBreakWord);

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText text) {
		assert false;
	}

}
