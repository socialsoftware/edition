package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;

public abstract class HtmlWriter implements GraphVisitor {

	protected FragInter fragInter = null;
	protected String transcription = "";

	public String getTranscription() {
		return transcription;
	}

	public String getMetaTextual() {
		return fragInter.getMetaTextual();
	}

	@Override
	public void visit(Reading reading) {
		reading.getFirstText().accept(this);
		reading.getNextVariationPoint().accept(this);
	}

	@Override
	public void visit(FormatText text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(PbText text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SpaceText text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(AddText text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(SubstText text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	@Override
	public void visit(ParagraphText text) {
		switch (text.getOpenClose()) {
		case CLOSE:
			transcription = transcription + "</p>";
			break;
		case OPEN:
			transcription = transcription + "<p>";
			break;
		}

		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

}
