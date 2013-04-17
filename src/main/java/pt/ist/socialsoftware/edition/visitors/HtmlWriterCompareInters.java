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
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

public class HtmlWriterCompareInters extends HtmlWriter {

	private final Map<FragInter, String> transcriptionsMap = new HashMap<FragInter, String>();
	private final List<FragInter> compareAgaints = new ArrayList<FragInter>();

	private Boolean breakWord = true;
	private int differences = 0;

	/**
	 * @param list
	 *            compare against interpretations in the list
	 */
	public HtmlWriterCompareInters(List<FragInter> list) {
		for (FragInter inter : list) {
			compareAgaints.add(inter);
		}
	}

	/**
	 * 
	 * @param interList
	 *            write comparisons for this elements
	 */
	public void write(List<FragInter> interList) {
		for (FragInter inter : interList) {
			fragInter = inter;
			transcription = "";
			breakWord = true;
			differences = 0;
			visit(inter.getFragment().getVariationPoint());
			transcriptionsMap.put(inter, transcription);
		}
	}

	public String getTranscription(FragInter inter) {
		return transcriptionsMap.get(inter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.ist.socialsoftware.edition.visitors.HtmlWriter#getTranscription()
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

		if (differences > 0) {
			int size = compareAgaints.size() == 0 ? 0
					: compareAgaints.size() - 1;

			int color = 255 - (255 / size) * (differences - 1);
			toAdd = "<span style=\"background-color: rgb(0," + color
					+ ",255);\">" + toAdd + "</span>";
		}
		transcription = transcription + separator + toAdd;

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
	public void visit(AddText addText) {
		switch (addText.getOpenClose()) {
		case CLOSE:
			transcription = transcription + "</a></ins>";
			break;
		case OPEN:
			transcription = transcription + "<ins>";
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
			transcription = transcription + "</del>";
			break;
		case OPEN:
			transcription = transcription + "<del>";
			break;
		}

		if (delText.getNextText() != null) {
			delText.getNextText().accept(this);
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