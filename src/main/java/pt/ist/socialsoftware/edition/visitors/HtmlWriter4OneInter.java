package pt.ist.socialsoftware.edition.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.Rend;
import pt.ist.socialsoftware.edition.domain.Rend.Rendition;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.TextPortion;

public class HtmlWriter4OneInter extends HtmlWriter {
	protected FragInter fragInter = null;
	private String transcription = "";

	private Boolean highlightDiff = false;
	private Boolean displayDel = false;
	private Boolean highlightIns = true;
	private Boolean highlightSubst = false;
	private Boolean showNotes = true;

	private final Map<FragInter, Integer> interpsChar = new HashMap<FragInter, Integer>();
	private int totalChar = 0;

	public String getTranscription(FragInter inter) {
		assert inter == fragInter;
		return transcription;
	}

	public Integer getInterPercentage(FragInter inter) {
		return (interpsChar.get(inter) * 100) / totalChar;
	}

	public HtmlWriter4OneInter(FragInter fragInter) {
		this.fragInter = fragInter;
		transcription = "";

		for (FragInter inter : fragInter.getFragment().getFragmentInterSet()) {
			interpsChar.put(inter, 0);
		}
	}

	public void write(Boolean highlightDiff) {
		this.highlightDiff = highlightDiff;
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}
		visit((AppText) fragInter.getFragment().getTextPortion());
	}

	public void write(Boolean highlightDiff, Boolean displayDel,
			Boolean highlightIns, Boolean highlightSubst, Boolean showNotes) {
		this.highlightDiff = highlightDiff;
		this.displayDel = displayDel;
		this.highlightIns = highlightIns;
		this.highlightSubst = highlightSubst;
		this.showNotes = showNotes;
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}
		visit((AppText) fragInter.getFragment().getTextPortion());
	}

	@Override
	public void visit(AppText appText) {
		TextPortion firstChild = appText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (appText.getParentOfLastText() == null) {
			if (appText.getNextText() != null) {
				appText.getNextText().accept(this);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		if (rdgGrpText.getInterps().contains(this.fragInter)) {
			TextPortion firstChild = rdgGrpText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}
		}

		if (rdgGrpText.getParentOfLastText() == null) {
			rdgGrpText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(RdgText rdgText) {
		if (rdgText.getInterps().contains(this.fragInter)) {

			Boolean color = false;
			if (highlightDiff) {
				int size = fragInter.getFragment().getFragmentInterSet().size();
				if (rdgText.getInterps().size() < size) {
					color = true;
					int colorValue = 255 - (255 / size)
							* (size - rdgText.getInterps().size() - 1);
					String colorCode = "<span style=\"background-color: rgb(0,"
							+ colorValue + ",255);\">";

					transcription = transcription
							+ rdgText.writeSeparator(displayDel,
									highlightSubst, fragInter) + colorCode;
				}
			}

			if (!color) {
				transcription = transcription
						+ rdgText.writeSeparator(displayDel, highlightSubst,
								fragInter);
			}

			TextPortion firstChild = rdgText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}

			if (color) {
				transcription = transcription + "</span>";
			}
		}

		if (rdgText.getParentOfLastText() == null) {
			rdgText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		transcription = transcription + "<p>";

		TextPortion firstChild = paragraphText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		transcription = transcription + "</p>";

		if (paragraphText.getParentOfLastText() == null) {
			paragraphText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SegText segText) {
		List<Rend> rends = new ArrayList<Rend>(segText.getRendSet());
		String preFormat = "";
		for (Rend rend : rends) {
			if (rend.getRend() == Rendition.RIGHT) {
				preFormat = preFormat + "<div class=\"text-right\">";
			} else if (rend.getRend() == Rendition.LEFT) {
				preFormat = preFormat + "<div class=\"text-left\">";
			} else if (rend.getRend() == Rendition.CENTER) {
				preFormat = preFormat + "<div class=\"text-center\">";
			} else if (rend.getRend() == Rendition.BOLD) {
				preFormat = preFormat + "<strong>";
			} else if (rend.getRend() == Rendition.ITALIC) {
				preFormat = preFormat + "<em>";
			} else if (rend.getRend() == Rendition.RED) {
				preFormat = preFormat + "<span style=\"color: rgb(255,0,0);\">";
			} else if (rend.getRend() == Rendition.UNDERLINED) {
				preFormat = preFormat + "<u>";
			}
		}

		transcription = transcription
				+ segText.writeSeparator(displayDel, highlightSubst, fragInter)
				+ preFormat;

		TextPortion firstChild = segText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		Collections.reverse(rends);
		String postFormat = "";
		for (Rend rend : rends) {
			if (rend.getRend() == Rendition.RIGHT) {
				postFormat = postFormat + "</div>";
			} else if (rend.getRend() == Rendition.LEFT) {
				postFormat = postFormat + "</div>";
			} else if (rend.getRend() == Rendition.CENTER) {
				postFormat = postFormat + "</div>";
			} else if (rend.getRend() == Rendition.BOLD) {
				postFormat = postFormat + "</strong>";
			} else if (rend.getRend() == Rendition.ITALIC) {
				postFormat = postFormat + "</em>";
			} else if (rend.getRend() == Rendition.RED) {
				postFormat = postFormat + "</span>";
			} else if (rend.getRend() == Rendition.UNDERLINED) {
				postFormat = postFormat + "</u>";
			}
		}

		transcription = transcription + postFormat;

		if (segText.getParentOfLastText() == null) {
			segText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SimpleText simpleText) {
		String value = simpleText.getValue();

		totalChar = totalChar + value.length();
		for (FragInter inter : simpleText.getInterps()) {
			Integer number = interpsChar.get(inter);
			number = number + value.length();
			interpsChar.put(inter, number);
		}

		transcription = transcription
				+ simpleText.writeSeparator(displayDel, highlightSubst,
						fragInter) + value;

		if (simpleText.getParentOfLastText() == null) {
			simpleText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		if (lbText.getInterps().contains(fragInter)) {
			String hyphen = "";
			if (lbText.getHyphenated()) {
				hyphen = "-";
			}

			transcription = transcription + hyphen + "<br>";
		}

		if (lbText.getParentOfLastText() == null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {
		if (pbText.getInterps().contains(fragInter)) {
			transcription = transcription + "<hr size=\"3\" color=\"black\">";
		}

		if (pbText.getParentOfLastText() == null) {
			pbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText spaceText) {
		String separator = "";
		if (spaceText.getDim() == SpaceDim.VERTICAL) {
			separator = "<br>";
			// the initial line break is for a new line
			transcription = transcription + separator;
		} else if (spaceText.getDim() == SpaceDim.HORIZONTAL) {
			separator = "&nbsp; ";
		}

		for (int i = 0; i < spaceText.getQuantity(); i++) {
			transcription = transcription + separator;
		}

		if (spaceText.getParentOfLastText() == null) {
			spaceText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		String openFormat = "";
		String closeFormat = "";

		switch (addText.getPlace()) {
		case INLINE:
		case SUPERIMPOSED:
		case UNSPECIFIED:
			openFormat = "<small>";
			closeFormat = "</small>";
			break;
		case MARGIN:
		case OPPOSITE:
		case ABOVE:
		case TOP:
			openFormat = "<sup>";
			closeFormat = "</sup>";
			break;
		case BELOW:
		case BOTTOM:
		case END:
			openFormat = "<sub>";
			closeFormat = "</sub>";
			break;
		}

		if (highlightIns) {
			String insertSymbol = "<span style=\"color: rgb(128,128,128);\"><small>&and;</small></span>";
			if (showNotes) {
				insertSymbol = "<abbr title=\"" + addText.getNote() + "\">"
						+ insertSymbol + "</abbr>";
			}

			transcription = transcription
					+ addText.writeSeparator(displayDel, highlightSubst,
							fragInter) + openFormat + insertSymbol;
		} else {
			transcription = transcription
					+ addText.writeSeparator(displayDel, highlightSubst,
							fragInter);
		}

		TextPortion firstChild = addText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (highlightIns) {
			transcription = transcription + closeFormat;
		}

		if (addText.getParentOfLastText() == null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		if (displayDel) {
			transcription = transcription
					+ delText.writeSeparator(displayDel, highlightSubst,
							fragInter)
					+ "<del><span style=\"color: rgb(128,128,128);\">";
			if (showNotes) {
				transcription = transcription + "<abbr title=\""
						+ delText.getNote() + "\">";
			}

			TextPortion firstChild = delText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}

			if (showNotes) {
				transcription = transcription + "</abbr>";
			}

			transcription = transcription + "</span></del>";
		}

		if (delText.getParentOfLastText() == null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {
		if (displayDel && highlightSubst) {
			transcription = transcription
					+ substText.writeSeparator(displayDel, highlightSubst,
							fragInter)
					+ "<span style=\"background-color: rgb(255,255,0);\">";
		}

		TextPortion firstChild = substText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (displayDel && highlightSubst) {
			transcription = transcription + "</span>";
		}

		if (substText.getParentOfLastText() == null) {
			substText.getNextText().accept(this);
		}
	}

}