package pt.ist.socialsoftware.edition.visitors;

import java.util.HashMap;
import java.util.Map;

import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

public class HtmlWriterCompareInters extends HtmlWriter {

	private final Map<FragInter, String> transcriptionsMap = new HashMap<FragInter, String>();
	private final Map<FragInter, Integer> differencesMap = new HashMap<FragInter, Integer>();

	private Boolean isDel = false;
	private Boolean breakWord = true;

	public HtmlWriterCompareInters(FragInter interBase, FragInter interCompare) {
		transcriptionsMap.put(interBase, "");
		transcriptionsMap.put(interCompare, "");

		differencesMap.put(interBase, 0);
		differencesMap.put(interCompare, 0);
	}

	public void write() {
		for (FragInter inter : transcriptionsMap.keySet()) {
			fragInter = inter;
			transcription = "";
			visit(inter.getFragment().getVariationPoint());
			transcriptionsMap.put(inter, transcription);

			System.out.println(transcription);
			System.out.println("XXXXXXXX");
		}
	}

	public String getTranscription(FragInter inter) {
		return transcriptionsMap.get(inter);
	}

	@Override
	public void visit(VariationPoint variationPoint) {
		if (!variationPoint.getOutReadingsSet().isEmpty()) {
			for (Reading rdg : variationPoint.getOutReadings()) {
				if (rdg.getFragIntersSet().contains(fragInter)) {
					int count = 0;
					for (FragInter tmpInter : transcriptionsMap.keySet()) {
						if ((tmpInter != fragInter)
								&& (!rdg.getFragIntersSet().contains(tmpInter))) {
							count = count + 1;
						}
					}

					differencesMap.put(fragInter, count);

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
			if (differencesMap.get(fragInter) > 0) {
				toAdd = "<span style=\"background-color: rgb(0,255,255);\">"
						+ toAdd + "</span>";
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

}