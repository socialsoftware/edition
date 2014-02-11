package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.TextPortion;
import pt.ist.socialsoftware.edition.domain.TextPortion.VariationType;

public class HtmlWriter4Variations extends HtmlWriter4OneInter {

	public String getAppTranscription(AppText appText) {
		transcription = "";

		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}
		visit(appText);

		return transcription;
	}

	public HtmlWriter4Variations(FragInter fragInter) {
		super(fragInter);
	}

	@Override
	public void visit(AppText appText) {
		if (appText.getType() != VariationType.UNSPECIFIED) {
			transcription = transcription + "[(" + appText.getType().getDesc()
					+ ")";
		}

		for (TextPortion childText : appText.getChildTextSet()) {
			if (childText.getInterps().contains(this.fragInter)) {
				childText.accept(this);
			}
		}

		if (appText.getType() != VariationType.UNSPECIFIED) {
			transcription = transcription + "]";
		}
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		if (rdgGrpText.getType() != VariationType.UNSPECIFIED) {
			transcription = transcription + "[("
					+ rdgGrpText.getType().getDesc() + ")";
		}

		for (TextPortion childText : rdgGrpText.getChildTextSet()) {
			if (rdgGrpText.getInterps().contains(this.fragInter)) {
				childText.accept(this);
			}
		}

		if (rdgGrpText.getType() != VariationType.UNSPECIFIED) {
			transcription = transcription + "]";
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
				transcription = transcription + "</span>" + "]";
			}
		}
	}

}
