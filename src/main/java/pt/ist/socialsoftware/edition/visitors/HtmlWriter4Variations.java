package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;

public class HtmlWriter4Variations extends PlainHtmlWriter4OneInter {

	public String getAppTranscription(AppText appText) {
		this.transcription = "";

		if (this.fragInter.getLastUsed() != this.fragInter) {
			this.fragInter = this.fragInter.getLastUsed();
		}

		this.transcription = this.transcription + "(" + appText.getType().getDesc() + ") ";
		appText.getFirstChildText().accept(this);

		return this.transcription;
	}

	public HtmlWriter4Variations(FragInter fragInter) {
		super(fragInter);
		this.highlightDiff = false;
		this.displayDel = true;
		this.highlightIns = true;
		this.highlightSubst = false;
		this.showNotes = true;
	}

	@Override
	public void visit(AppText appText) {
		this.transcription = this.transcription + " (" + appText.getType().getDesc() + ") ";

		super.visit(appText);

		// for (TextPortion childText : appText.getChildTextSet()) {
		// if (childText.getInterps().contains(this.fragInter)) {
		// childText.accept(this);
		// }
		// }

	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		this.transcription = this.transcription + " (" + rdgGrpText.getType().getDesc() + ") ";

		super.visit(rdgGrpText);
		// for (TextPortion childText : rdgGrpText.getChildTextSet()) {
		// if (rdgGrpText.getInterps().contains(this.fragInter)) {
		// childText.accept(this);
		// }
		// }

	}

	// @Override
	// public void visit(RdgText rdgText) {
	// if (rdgText.getInterps().contains(this.fragInter)) {
	//
	// TextPortion firstChild = rdgText.getFirstChildText();
	// if (firstChild != null) {
	// firstChild.accept(this);
	// }
	//
	// }
	// }

}
