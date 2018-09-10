package pt.ist.socialsoftware.edition.text.generators;

import pt.ist.socialsoftware.edition.text.domain.AddText;
import pt.ist.socialsoftware.edition.text.domain.AltText;
import pt.ist.socialsoftware.edition.text.domain.AppText;
import pt.ist.socialsoftware.edition.text.domain.DelText;
import pt.ist.socialsoftware.edition.text.domain.GapText;
import pt.ist.socialsoftware.edition.text.domain.LbText;
import pt.ist.socialsoftware.edition.text.domain.NoteText;
import pt.ist.socialsoftware.edition.text.domain.ParagraphText;
import pt.ist.socialsoftware.edition.text.domain.PbText;
import pt.ist.socialsoftware.edition.text.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.text.domain.RdgText;
import pt.ist.socialsoftware.edition.text.domain.RefText;
import pt.ist.socialsoftware.edition.text.domain.SegText;
import pt.ist.socialsoftware.edition.text.domain.SimpleText;
import pt.ist.socialsoftware.edition.text.domain.SpaceText;
import pt.ist.socialsoftware.edition.text.domain.SubstText;
import pt.ist.socialsoftware.edition.text.domain.TextPortion;
import pt.ist.socialsoftware.edition.text.domain.UnclearText;

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
