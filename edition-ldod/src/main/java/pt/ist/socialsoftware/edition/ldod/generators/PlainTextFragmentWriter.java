package pt.ist.socialsoftware.edition.ldod.generators;

import pt.ist.socialsoftware.edition.ldod.domain.AddText;
import pt.ist.socialsoftware.edition.ldod.domain.AltText;
import pt.ist.socialsoftware.edition.ldod.domain.AppText;
import pt.ist.socialsoftware.edition.ldod.domain.DelText;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.GapText;
import pt.ist.socialsoftware.edition.ldod.domain.LbText;
import pt.ist.socialsoftware.edition.ldod.domain.NoteText;
import pt.ist.socialsoftware.edition.ldod.domain.ParagraphText;
import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.ldod.domain.RdgText;
import pt.ist.socialsoftware.edition.ldod.domain.RefText;
import pt.ist.socialsoftware.edition.ldod.domain.SegText;
import pt.ist.socialsoftware.edition.ldod.domain.SimpleText;
import pt.ist.socialsoftware.edition.ldod.domain.SpaceText;
import pt.ist.socialsoftware.edition.ldod.domain.SubstText;
import pt.ist.socialsoftware.edition.ldod.domain.UnclearText;

public class PlainTextFragmentWriter implements TextPortionVisitor {

	protected FragInter fragInter = null;
	protected String transcription = "";

	private void append2Transcription(String generated) {
		transcription = transcription + generated;
	}

	public String getTranscription() {
		return transcription;
	}

	public PlainTextFragmentWriter(FragInter fragInter) {
		this.fragInter = fragInter;
		transcription = "";
	}

	public void write() {
		if (fragInter.getLastUsed() != fragInter) {
			fragInter = fragInter.getLastUsed();
		}
		visit((AppText) fragInter.getFragment().getTextPortion());
	}

	@Override
	public void visit(AppText appText) {
		propagate2FirstChild(appText);

		propagate2NextSibling(appText);
	}

	@Override
	public void visit(RdgGrpText rdgGrpText) {
		if (rdgGrpText.getInterps().contains(this.fragInter)) {
			propagate2FirstChild(rdgGrpText);
		}

		propagate2NextSibling(rdgGrpText);
	}

	@Override
	public void visit(RdgText rdgText) {
		if (rdgText.getInterps().contains(this.fragInter)) {
			append2Transcription(rdgText.writeSeparator(true, false, this.fragInter));

			propagate2FirstChild(rdgText);
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
		propagate2FirstChild(segText);
		propagate2NextSibling(segText);
	}

	@Override
	public void visit(AltText altText) {
		propagate2NextSibling(altText);
	}

	@Override
	public void visit(SimpleText simpleText) {
		String value = simpleText.getValue();
		append2Transcription(simpleText.writeSeparator(true, false, fragInter) + value);

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
		append2Transcription(addText.writeSeparator(true, false, fragInter));

		propagate2FirstChild(addText);

		propagate2NextSibling(addText);
	}

	@Override
	public void visit(DelText delText) {
		propagate2FirstChild(delText);
		propagate2NextSibling(delText);
	}

	@Override
	public void visit(SubstText substText) {
		propagate2FirstChild(substText);
		propagate2NextSibling(substText);
	}

	@Override
	public void visit(GapText gapText) {
		propagate2NextSibling(gapText);
	}

	@Override
	public void visit(UnclearText unclearText) {
		propagate2FirstChild(unclearText);
		propagate2NextSibling(unclearText);
	}

	@Override
	public void visit(NoteText noteText) {
		propagate2FirstChild(noteText);
		propagate2NextSibling(noteText);
	}

	@Override
	public void visit(RefText refText) {
		propagate2FirstChild(refText);
		propagate2NextSibling(refText);
	}

}
