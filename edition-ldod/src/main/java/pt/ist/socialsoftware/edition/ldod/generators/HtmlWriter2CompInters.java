package pt.ist.socialsoftware.edition.ldod.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.*;

public class HtmlWriter2CompInters implements TextPortionVisitor {
	private final Map<ScholarInter, String> transcriptionsMap = new HashMap<ScholarInter, String>();
	private final Map<ScholarInter, Integer> transcriptionsLengthMap = new HashMap<ScholarInter, Integer>();
	private List<ScholarInter> interps = null;

	private Boolean lineByLine = false;
	private Boolean showSpaces = false;

	public Boolean getShowSpaces() {
		return showSpaces;
	}

	private String lineByLineTranscription = "";

	public HtmlWriter2CompInters(List<ScholarInter> interps) {
		this.interps = new ArrayList<ScholarInter>(interps);
	}

	public void write(Boolean lineByLine, Boolean showSpaces) {
		this.lineByLine = lineByLine;
		this.showSpaces = showSpaces;

		for (ScholarInter inter : interps) {
			transcriptionsMap.put(inter, "");
			transcriptionsLengthMap.put(inter, 0);
		}

		visit((AppText) interps.iterator().next().getFragment().getTextPortion());
	}

	public String getTranscription(ScholarInter inter) {
		return transcriptionsMap.get(inter);
	}

	public String getTranscriptionLineByLine() {
		// add the last line
		for (ScholarInter inter : interps) {
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
			for (ScholarInter inter : interps) {
				longestLength = Math.max(longestLength, transcriptionsLengthMap.get(inter));
			}

			if (longestLength >= lineLength) {
				for (ScholarInter inter : interps) {
					lineByLineTranscription = lineByLineTranscription + transcriptionsMap.get(inter) + "<br>";
					transcriptionsMap.put(inter, "");
					transcriptionsLengthMap.put(inter, 0);
				}
			}
		}
	}

	private void alignSpaces(AppText appText) {
		if (showSpaces) {
			Set<ScholarInter> appInterps = new HashSet<ScholarInter>(interps);
			appInterps.retainAll(appText.getInterps());

			int longestLength = 0;
			for (ScholarInter inter : appInterps) {
				longestLength = Math.max(longestLength, transcriptionsLengthMap.get(inter));
			}

			for (ScholarInter inter : appInterps) {
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
		Set<ScholarInter> intersection = new HashSet<ScholarInter>(interps);
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

			for (ScholarInter inter : intersection) {
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
				for (ScholarInter inter : intersection) {
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
		Set<ScholarInter> intersection = new HashSet<ScholarInter>(interps);
		intersection.retainAll(segText.getInterps());

		for (ScholarInter inter : intersection) {
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
		Set<ScholarInter> intersection = new HashSet<ScholarInter>(interps);
		intersection.retainAll(simpleText.getInterps());

		String value = simpleText.getValue();

		for (ScholarInter inter : intersection) {
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
		Set<ScholarInter> intersection = new HashSet<>(interps);
		intersection.retainAll(addText.getInterps());

		for (ScholarInter inter : intersection) {
			String separator = addText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator + "<ins>";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(addText);

		for (ScholarInter inter : intersection) {
			String newTranscription = transcriptionsMap.get(inter) + "</ins>";
			transcriptionsMap.put(inter, newTranscription);
		}

		propagate2NextSibling(addText);
	}

	@Override
	public void visit(DelText delText) {
		Set<ScholarInter> intersection = new HashSet<>(interps);
		intersection.retainAll(delText.getInterps());

		for (ScholarInter inter : intersection) {
			String separator = delText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator + "<del>";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(delText);

		for (ScholarInter inter : intersection) {
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
		Set<ScholarInter> intersection = new HashSet<>(interps);
		intersection.retainAll(gapText.getInterps());

		String value = gapText.getGapValue();

		for (ScholarInter inter : intersection) {
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

		Set<ScholarInter> intersection = new HashSet<>(interps);
		intersection.retainAll(unclearText.getInterps());

		for (ScholarInter inter : intersection) {
			String separator = unclearText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator
					+ "<span style=\"text-shadow: black 0.0em 0.0em 0.1em; -webkit-filter: blur(0.005em);\">";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter, transcriptionsLengthMap.get(inter) + separator.length());
		}

		propagate2FirstChild(unclearText);

		for (ScholarInter inter : intersection) {
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

}
