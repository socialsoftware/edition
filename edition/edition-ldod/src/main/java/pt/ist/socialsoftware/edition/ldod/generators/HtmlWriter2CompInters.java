package pt.ist.socialsoftware.edition.ldod.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.AddText;
import pt.ist.socialsoftware.edition.ldod.domain.AltText;
import pt.ist.socialsoftware.edition.ldod.domain.AppText;
import pt.ist.socialsoftware.edition.ldod.domain.DelText;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.GapText;
import pt.ist.socialsoftware.edition.ldod.domain.LbText;
import pt.ist.socialsoftware.edition.ldod.domain.NoteText;
import pt.ist.socialsoftware.edition.ldod.domain.ParagraphText;
import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.ldod.domain.RdgText;
import pt.ist.socialsoftware.edition.ldod.domain.RefText;
import pt.ist.socialsoftware.edition.ldod.domain.SegText;
import pt.ist.socialsoftware.edition.ldod.domain.SimpleText;
import pt.ist.socialsoftware.edition.ldod.domain.SpaceText;
import pt.ist.socialsoftware.edition.ldod.domain.SubstText;
import pt.ist.socialsoftware.edition.ldod.domain.UnclearText;

public class HtmlWriter2CompInters implements TextPortionVisitor {
	private final Map<FragInter, String> transcriptionsMap = new HashMap<FragInter, String>();
	private final Map<FragInter, Integer> transcriptionsLengthMap = new HashMap<FragInter, Integer>();
	private List<FragInter> interps = null;



	private Boolean lineByLine = false;
	private Boolean showSpaces = false;


	private String lineByLineTranscription = "";

	public HtmlWriter2CompInters(List<FragInter> interps) {
		this.interps = new ArrayList<FragInter>(interps);
	}

	public void write(Boolean lineByLine, Boolean showSpaces) {
		this.lineByLine = lineByLine;
		this.showSpaces = showSpaces;

		for (FragInter inter : interps) {
			transcriptionsMap.put(inter, "");
			transcriptionsLengthMap.put(inter, 0);
		}

		visit((AppText) interps.iterator().next().getFragment().getTextPortion());
	}

	public String getTranscription(FragInter inter) {
		return transcriptionsMap.get(inter);
	}

	public String getTranscriptionLineByLine() {
		// add the last line
		for (FragInter inter : interps) {
			lineByLineTranscription = lineByLineTranscription + transcriptionsMap.get(inter) + "<br>";
			transcriptionsMap.put(inter, "");
			transcriptionsLengthMap.put(inter, 0);
		}

		return lineByLineTranscription;
	}

	@Override
	public void visit(AppText appText) {
		alignSpaces(appText);

		generateLineByLine(appText);

		propagate2FirstChild(appText);

		alignSpaces(appText);

		propagate2NextSibling(appText);
	}

	private void generateLineByLine(AppText appText) {
		if (lineByLine && (appText.getInterps().containsAll(interps))) {
			int lineLength = 66;

			int longestLength = 0;
			for (FragInter inter : interps) {
				longestLength = Math.max(longestLength, transcriptionsLengthMap.get(inter));
			}

			if (longestLength >= lineLength) {
				for (FragInter inter : interps) {
					lineByLineTranscription = lineByLineTranscription + transcriptionsMap.get(inter) + "<br>";
					transcriptionsMap.put(inter, "");
					transcriptionsLengthMap.put(inter, 0);
				}
			}
		}
	}

	private void alignSpaces(AppText appText) {
		if (showSpaces) {
			Set<FragInter> appInterps = new HashSet<FragInter>(interps);
			appInterps.retainAll(appText.getInterps());

			int longestLength = 0;
			for (FragInter inter : appInterps) {
				longestLength = Math.max(longestLength, transcriptionsLengthMap.get(inter));
			}

			for (FragInter inter : appInterps) {
				String addSpace = "";
				int diff = longestLength - transcriptionsLengthMap.get(inter);
				for (int i = 0; i < diff; i++) {
					addSpace = addSpace + "&nbsp;";
				}
				String newTranscription = transcriptionsMap.get(inter) + addSpace;

				transcriptionsMap.put(inter, newTranscription);
				transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + diff);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		propagate2FirstChild(rdgGrpText);

		propagate2NextSibling(rdgGrpText);
	}

	@Override
	public void visit(RdgText rdgText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(rdgText.getInterps());

		if (!intersection.isEmpty()) {

			Boolean color = false;

			int size = interps.size();
			String colorCode = "";
			if (intersection.size() < size) {
				color = true;
				int colorValue = 255 - (255 / size) * (size - intersection.size() - 1);
				colorCode = "<span style=\"background-color: rgb(0," + colorValue + ",255);\">";
			}

			for (FragInter inter : intersection) {
				String separator = rdgText.writeSeparator(true, false, inter);

				String newTranscription = transcriptionsMap.get(inter) + separator;

				if (color) {
					newTranscription = newTranscription + colorCode;
				}

				transcriptionsMap.put(inter, newTranscription);
				transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
			}

			propagate2FirstChild(rdgText);

			if (color) {
				for (FragInter inter : intersection) {
					String newTranscription = transcriptionsMap.get(inter) + "</span>";
					transcriptionsMap.put(inter, newTranscription);
				}
			}

		}

		propagate2NextSibling(rdgText);
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		propagate2FirstChild(paragraphText);

		propagate2NextSibling(paragraphText);
	}

	@Override
	public void visit(SegText segText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(segText.getInterps());

		for (FragInter inter : intersection) {
			String separator = segText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator;
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(segText);

		propagate2NextSibling(segText);
	}

	@Override
	public void visit(SimpleText simpleText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(simpleText.getInterps());

		String value = simpleText.getValue();

		for (FragInter inter : intersection) {
			String separator = simpleText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator + value;
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter,
					transcriptionsLengthMap.get(inter) + value.length() + separator.length());
		}

		propagate2NextSibling(simpleText);
	}

	@Override
	public void visit(LbText lbText) {
		propagate2NextSibling(lbText);
	}

	@Override
	public void visit(PbText pbText) {
		propagate2NextSibling(pbText);
	}

	@Override
	public void visit(SpaceText spaceText) {
		propagate2NextSibling(spaceText);
	}

	@Override
	public void visit(AddText addText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(addText.getInterps());

		for (FragInter inter : intersection) {
			String separator = addText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator + "<ins>";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(addText);

		for (FragInter inter : intersection) {
			String newTranscription = transcriptionsMap.get(inter) + "</ins>";
			transcriptionsMap.put(inter, newTranscription);
		}

		propagate2NextSibling(addText);
	}

	@Override
	public void visit(DelText delText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(delText.getInterps());

		for (FragInter inter : intersection) {
			String separator = delText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator + "<del>";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(delText);

		for (FragInter inter : intersection) {
			String newTranscription = transcriptionsMap.get(inter) + "</del>";
			transcriptionsMap.put(inter, newTranscription);
		}

		propagate2NextSibling(delText);
	}

	@Override
	public void visit(SubstText substText) {
		propagate2FirstChild(substText);

		propagate2NextSibling(substText);
	}

	@Override
	public void visit(GapText gapText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(gapText.getInterps());

		String value = gapText.getGapValue();

		for (FragInter inter : intersection) {
			String separator = gapText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator + value;
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter,
					transcriptionsLengthMap.get(inter) + value.length() + separator.length());
		}

		propagate2NextSibling(gapText);
	}

	@Override
	public void visit(UnclearText unclearText) {

		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(unclearText.getInterps());

		for (FragInter inter : intersection) {
			String separator = unclearText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator
					+ "<span style=\"text-shadow: black 0.0em 0.0em 0.1em; -webkit-filter: blur(0.005em);\">";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(unclearText);

		for (FragInter inter : intersection) {
			String newTranscription = transcriptionsMap.get(inter) + "</span>";
			transcriptionsMap.put(inter, newTranscription);
		}

		propagate2NextSibling(unclearText);
	}

	@Override
	public void visit(AltText altText) {
		propagate2NextSibling(altText);
	}

	@Override
	public void visit(NoteText noteText) {
		propagate2NextSibling(noteText);
	}

	@Override
	public void visit(RefText refText) {
		propagate2FirstChild(refText);
		propagate2NextSibling(refText);
	}

	public Boolean getShowSpaces() {
		return showSpaces;
	}
	public Boolean getLineByLine() {
		return lineByLine;
	}

}
