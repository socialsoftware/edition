package pt.ist.socialsoftware.edition.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.LdoDText;
import pt.ist.socialsoftware.edition.domain.LdoDText.OpenClose;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

public class HtmlWriter2CompInters extends HtmlWriter {

	private final Map<FragInter, String> transcriptionsMap = new HashMap<FragInter, String>();
	private final List<FragInter> compareAgaints = new ArrayList<FragInter>();

	private final Map<FragInter, List<Reading>> interReadingsMap = new HashMap<FragInter, List<Reading>>();
	private List<Reading> interReadings = null;

	private final Map<Reading, String> rdgTranscriptionMap = new HashMap<Reading, String>();
	private final Map<VariationPoint, Integer> pointStartMap = new HashMap<VariationPoint, Integer>();
	private final Map<Reading, Integer> rdgLengthMap = new HashMap<Reading, Integer>();
	private int rdgLength = 0;

	private final List<LdoDText> pendingWrite = new ArrayList<LdoDText>();

	private Boolean breakWord = true;
	private int differences = 0;

	/**
	 * compare against interpretations in the list
	 */
	public HtmlWriter2CompInters(List<FragInter> list) {
		for (FragInter inter : list) {
			compareAgaints.add(inter);
		}
	}

	/**
	 * write comparisons for this elements
	 */
	public void write(List<FragInter> interList) {
		for (FragInter inter : interList) {
			fragInter = inter;
			interReadings = new ArrayList<Reading>();
			breakWord = true;
			differences = 0;
			visit(inter.getFragment().getVariationPoint());

			interReadingsMap.put(inter, interReadings);

		}
		computeReadingsStartPosition(interList);
		generateTranscriptions(interList);
	}

	private void generateTranscriptions(List<FragInter> interList) {
		for (FragInter inter : interList) {
			transcription = "";
			int transcriptionLength = 0;
			for (Reading rdg : interReadingsMap.get(inter)) {
				int increaseLength = pointStartMap.get(rdg
						.getPreviousVariationPoint()) - transcriptionLength;

				String addSpace = "";
				for (int i = 0; i < increaseLength; i++) {
					addSpace = addSpace + "&nbsp; ";
				}

				transcription = transcription + addSpace
						+ rdgTranscriptionMap.get(rdg);

				increaseLength = increaseLength < 0 ? 0 : increaseLength;
				transcriptionLength = transcriptionLength + increaseLength
						+ rdgLengthMap.get(rdg);

			}
			transcriptionsMap.put(inter, transcription);
		}
	}

	private void computeReadingsStartPosition(List<FragInter> interList) {
		Boolean regenerate = true;
		// the computation of the starting points of one transcription may
		// require the recomputation of other
		while (regenerate) {
			System.out.println("REGENERATE");
			regenerate = false;
			for (FragInter inter : interList) {
				int transcriptionLength = 0;
				for (Reading rdg : interReadingsMap.get(inter)) {
					Integer oldRdgStart = pointStartMap.get(rdg
							.getPreviousVariationPoint());
					oldRdgStart = oldRdgStart == null ? 0 : oldRdgStart;
					int newRdgStart = Math
							.max(transcriptionLength, oldRdgStart);
					if (newRdgStart != oldRdgStart)
						regenerate = true;

					pointStartMap.put(rdg.getPreviousVariationPoint(),
							newRdgStart);
					transcriptionLength = newRdgStart + rdgLengthMap.get(rdg);
				}
			}
		}
	}

	public String getTranscription(FragInter inter) {
		return transcriptionsMap.get(inter);
	}

	/*
	 * should be invoked when there is a single fragInter
	 */
	@Override
	public String getTranscription() {
		assert transcriptionsMap.keySet().size() == 1;
		return transcriptionsMap.get(fragInter);
	}

	@Override
	public void visit(VariationPoint variationPoint) {
		if (!variationPoint.getOutReadingsSet().isEmpty()) {
			for (Reading rdg : variationPoint.getOutReadings()) {
				if (rdg.getFragIntersSet().contains(fragInter)) {
					int count = 0;
					for (FragInter tmpInter : compareAgaints) {
						if ((tmpInter != fragInter)
								&& (!rdg.getFragIntersSet().contains(tmpInter))) {
							count = count + 1;
						}
					}

					differences = count;

					rdg.accept(this);
				}
			}
		}
	}

	@Override
	public void visit(Reading reading) {

		if (rdgTranscriptionMap.get(reading) == null) {
			transcription = "";
			rdgLength = 0;

			reading.getFirstText().accept(this);

			if (differences > 0) {
				int size = compareAgaints.size() == 0 ? 0 : compareAgaints
						.size() - 1;

				int colorValue = 255 - (200 / size) * (differences - 1);
				String colorCode = "<span style=\"background-color: rgb(0,"
						+ colorValue + ",255);\">";

				if (transcription.equals("")
						|| !transcription.substring(0, 1).equals(" ")) {
					transcription = colorCode + transcription + "</span>";
				} else {
					transcription = " " + colorCode
							+ transcription.substring(1) + "</span>";
				}

			}

			rdgTranscriptionMap.put(reading, transcription);
			rdgLengthMap.put(reading, rdgLength);
		}

		interReadings.add(reading);

		reading.getNextVariationPoint().accept(this);
	}

	@Override
	public void visit(SimpleText text) {
		String separators = ".,?!:;";
		String separator = null;
		String toAdd = text.getValue();

		String firstChar = toAdd.substring(0, 1);

		if (separators.contains(firstChar) || !breakWord) {
			separator = "";
			breakWord = true;
		} else {
			separator = " ";
		}

		OpenClose state = OpenClose.CLOSE;
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

			transcription = transcription + pText.writeHtml();
		}
		pendingWrite.clear();

		if (state == OpenClose.OPEN) {
			transcription = transcription + toAdd;
		} else {
			transcription = transcription + separator + toAdd;
		}

		rdgLength = rdgLength + separator.length() + toAdd.length();

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(LbText text) {
		breakWord = text.getBreakWord();

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText text) {
		transcription = transcription + text.writeHtml();

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText text) {
		pendingWrite.add(text);

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(DelText text) {
		pendingWrite.add(text);

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(EmptyText text) {
		breakWord = text.getIsBreak();

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

}
