package pt.ist.socialsoftware.edition.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.TextPortion;

public class HtmlWriter2CompInters extends HtmlWriter {
	private final Map<FragInter, String> transcriptionsMap = new HashMap<FragInter, String>();
	private final Map<FragInter, Integer> transcriptionsLengthMap = new HashMap<FragInter, Integer>();
	private List<FragInter> interps = null;

	private Boolean lineByLine = false;
	private Boolean showSpaces = false;

	public Boolean getShowSpaces() {
		return showSpaces;
	}

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

		visit((AppText) interps.iterator().next().getFragment()
				.getTextPortion());
	}

	public String getTranscription(FragInter inter) {
		return transcriptionsMap.get(inter);
	}

	public String getTranscriptionLineByLine() {
		// add the last line
		for (FragInter inter : interps) {
			lineByLineTranscription = lineByLineTranscription
					+ transcriptionsMap.get(inter) + "<br>";
			transcriptionsMap.put(inter, "");
			transcriptionsLengthMap.put(inter, 0);
		}

		return lineByLineTranscription;
	}

	@Override
	public void visit(AppText appText) {
		alignSpaces(appText);

		generateLineByLine(appText);

		TextPortion firstChild = appText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		alignSpaces(appText);

		if (appText.getParentOfLastText() == null) {
			if (appText.getNextText() != null) {
				appText.getNextText().accept(this);
			}
		}
	}

	private void generateLineByLine(AppText appText) {
		if (lineByLine && (appText.getInterps().containsAll(interps))) {
			int lineLength = 85;

			int longestLength = 0;
			for (FragInter inter : interps) {
				longestLength = Math.max(longestLength,
						transcriptionsLengthMap.get(inter));
			}

			if (longestLength >= lineLength) {
				for (FragInter inter : interps) {
					lineByLineTranscription = lineByLineTranscription
							+ transcriptionsMap.get(inter) + "<br>";
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
				longestLength = Math.max(longestLength,
						transcriptionsLengthMap.get(inter));
			}

			for (FragInter inter : appInterps) {
				String addSpace = "";
				int diff = longestLength - transcriptionsLengthMap.get(inter);
				for (int i = 0; i < diff; i++) {
					addSpace = addSpace + "&nbsp;";
				}
				String newTranscription = transcriptionsMap.get(inter)
						+ addSpace;

				transcriptionsMap.put(inter, newTranscription);
				transcriptionsLengthMap.put(inter,
						transcriptionsLengthMap.get(inter) + diff);
			}
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		TextPortion firstChild = rdgGrpText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (rdgGrpText.getParentOfLastText() == null) {
			rdgGrpText.getNextText().accept(this);
		}
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
				int colorValue = 255 - (255 / size)
						* (size - intersection.size() - 1);
				colorCode = "<span style=\"background-color: rgb(0,"
						+ colorValue + ",255);\">";
			}

			for (FragInter inter : intersection) {
				String separator = rdgText.writeSeparator(true, false, inter);

				String newTranscription = transcriptionsMap.get(inter)
						+ separator;

				if (color) {
					newTranscription = newTranscription + colorCode;
				}

				transcriptionsMap.put(inter, newTranscription);
				transcriptionsLengthMap
						.put(inter, transcriptionsLengthMap.get(inter)
								+ separator.length());
			}

			TextPortion firstChild = rdgText.getFirstChildText();
			if (firstChild != null) {
				firstChild.accept(this);
			}

			if (color) {
				for (FragInter inter : intersection) {
					String newTranscription = transcriptionsMap.get(inter)
							+ "</span>";
					transcriptionsMap.put(inter, newTranscription);
				}
			}

		}

		if (rdgText.getParentOfLastText() == null) {
			rdgText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText paragraphText) {
		TextPortion firstChild = paragraphText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (paragraphText.getParentOfLastText() == null) {
			paragraphText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SegText segText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(segText.getInterps());

		for (FragInter inter : intersection) {
			String separator = segText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator;
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter,
					transcriptionsLengthMap.get(inter) + separator.length());
		}

		TextPortion firstChild = segText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (segText.getParentOfLastText() == null) {
			segText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SimpleText simpleText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(simpleText.getInterps());

		String value = simpleText.getValue();

		for (FragInter inter : intersection) {
			String separator = simpleText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator
					+ value;
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter,
					transcriptionsLengthMap.get(inter) + value.length()
							+ separator.length());
		}

		if (simpleText.getParentOfLastText() == null) {
			simpleText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText lbText) {
		TextPortion firstChild = lbText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (lbText.getParentOfLastText() == null) {
			lbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText pbText) {
		TextPortion firstChild = pbText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (pbText.getParentOfLastText() == null) {
			pbText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText spaceText) {
		if (spaceText.getParentOfLastText() == null) {
			spaceText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText addText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(addText.getInterps());

		for (FragInter inter : intersection) {
			String separator = addText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator
					+ "<ins>";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter,
					transcriptionsLengthMap.get(inter) + separator.length());
		}

		TextPortion firstChild = addText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		for (FragInter inter : intersection) {
			String newTranscription = transcriptionsMap.get(inter) + "</ins>";
			transcriptionsMap.put(inter, newTranscription);
		}

		if (addText.getParentOfLastText() == null) {
			addText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText delText) {
		Set<FragInter> intersection = new HashSet<FragInter>(interps);
		intersection.retainAll(delText.getInterps());

		for (FragInter inter : intersection) {
			String separator = delText.writeSeparator(true, false, inter);
			String newTranscription = transcriptionsMap.get(inter) + separator
					+ "<del>";
			transcriptionsMap.put(inter, newTranscription);
			transcriptionsLengthMap.put(inter,
					transcriptionsLengthMap.get(inter) + separator.length());
		}

		TextPortion firstChild = delText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		for (FragInter inter : intersection) {
			String newTranscription = transcriptionsMap.get(inter) + "</del>";
			transcriptionsMap.put(inter, newTranscription);
		}

		if (delText.getParentOfLastText() == null) {
			delText.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText substText) {
		TextPortion firstChild = substText.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}

		if (substText.getParentOfLastText() == null) {
			substText.getNextText().accept(this);
		}
	}

}
