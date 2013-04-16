package pt.ist.socialsoftware.edition.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private Boolean isDel = false;
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
			isDel = false;
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

		if (!isDel) {
			if (differences > 0) {
				int color = 255 - (255 / compareAgaints.size()) * differences;
				toAdd = "<span style=\"background-color: rgb(0," + color
						+ ",255);\">" + toAdd + "</span>";
			}
			transcription = transcription + separator + toAdd;
		}

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
	public void visit(DelText text) {

		switch (text.getOpenClose()) {
		case CLOSE:
			isDel = false;
			break;
		case OPEN:
			isDel = true;
			break;
		}

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