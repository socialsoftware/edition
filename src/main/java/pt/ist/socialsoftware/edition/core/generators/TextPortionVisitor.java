package pt.ist.socialsoftware.edition.core.generators;

import pt.ist.socialsoftware.edition.core.domain.AddText;
import pt.ist.socialsoftware.edition.core.domain.AltText;
import pt.ist.socialsoftware.edition.core.domain.AppText;
import pt.ist.socialsoftware.edition.core.domain.DelText;
import pt.ist.socialsoftware.edition.core.domain.GapText;
import pt.ist.socialsoftware.edition.core.domain.LbText;
import pt.ist.socialsoftware.edition.core.domain.NoteText;
import pt.ist.socialsoftware.edition.core.domain.ParagraphText;
import pt.ist.socialsoftware.edition.core.domain.PbText;
import pt.ist.socialsoftware.edition.core.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.core.domain.RdgText;
import pt.ist.socialsoftware.edition.core.domain.RefText;
import pt.ist.socialsoftware.edition.core.domain.SegText;
import pt.ist.socialsoftware.edition.core.domain.SimpleText;
import pt.ist.socialsoftware.edition.core.domain.SpaceText;
import pt.ist.socialsoftware.edition.core.domain.SubstText;
import pt.ist.socialsoftware.edition.core.domain.TextPortion;
import pt.ist.socialsoftware.edition.core.domain.UnclearText;

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
