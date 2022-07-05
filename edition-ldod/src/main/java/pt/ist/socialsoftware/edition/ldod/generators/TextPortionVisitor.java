package pt.ist.socialsoftware.edition.ldod.generators;

import pt.ist.socialsoftware.edition.ldod.domain.AddText;
import pt.ist.socialsoftware.edition.ldod.domain.AltText;
import pt.ist.socialsoftware.edition.ldod.domain.AppText;
import pt.ist.socialsoftware.edition.ldod.domain.DelText;
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
import pt.ist.socialsoftware.edition.ldod.domain.TextPortion;
import pt.ist.socialsoftware.edition.ldod.domain.UnclearText;

public interface TextPortionVisitor {
	default void propagate2FirstChild(TextPortion text) {
		TextPortion firstChild = text.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}
	}

	default void propagate2NextSibling(TextPortion text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

	public void visit(AppText appText);

	public void visit(RdgGrpText rdgGrpText);

	public void visit(RdgText rdgText);

	public void visit(ParagraphText paragraphText);

	public void visit(SimpleText text);

	public void visit(SegText segText);

	public void visit(LbText lbText);

	public void visit(PbText pbText);

	public void visit(SpaceText spaceText);

	public void visit(AddText addText);

	public void visit(DelText delText);

	public void visit(SubstText substText);

	public void visit(GapText gapText);

	public void visit(UnclearText unclearText);

	public void visit(AltText altText);

	public void visit(NoteText noteText);

	public void visit(RefText refText);
}
