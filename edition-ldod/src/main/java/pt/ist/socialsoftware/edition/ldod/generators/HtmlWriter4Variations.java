package pt.ist.socialsoftware.edition.ldod.generators;

import pt.ist.socialsoftware.edition.ldod.domain.*;

public class HtmlWriter4Variations extends PlainHtmlWriter4OneInter {

	public String getAppTranscription(AppText appText) {
		this.transcription = "";


		if (!appText.getType().equals(TextPortion.VariationType.UNSPECIFIED)) {
			this.transcription = this.transcription + "(" + generateType(appText.getType()) + ") ";
		}

		appText.getFirstChildText().accept(this);

		return this.transcription;
	}

	public HtmlWriter4Variations(ScholarInter fragInter) {
		super(fragInter);
		this.highlightDiff = false;
		this.displayDel = true;
		this.highlightIns = true;
		this.highlightSubst = false;
		this.showNotes = true;
	}

	@Override
	public void visit(AppText appText) {
		if (!appText.getType().equals(TextPortion.VariationType.UNSPECIFIED)) {
			this.transcription = this.transcription + " (" + generateType(appText.getType()) + ") ";
		}

		super.visit(appText);
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		if (!rdgGrpText.getType().equals(TextPortion.VariationType.UNSPECIFIED)) {
			this.transcription = this.transcription + " (" + generateType(rdgGrpText.getType()) + ") ";
		}

		super.visit(rdgGrpText);

	}

	private String generateType(TextPortion.VariationType type) {
		switch (type) {
		case SUBSTANTIVE:
			return "<strong>" + type.getDesc() + "</strong>";
		default:
			return type.getDesc();
		}
	}

}
