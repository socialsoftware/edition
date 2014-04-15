/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.AltText;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.GapText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.NoteText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.RdgGrpText;
import pt.ist.socialsoftware.edition.domain.RdgText;
import pt.ist.socialsoftware.edition.domain.RefText;
import pt.ist.socialsoftware.edition.domain.SegText;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.UnclearText;

/**
 * The visitor pattern applied to the tree of text
 * 
 * @author ars
 * 
 */
public interface TextTreeVisitor {
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
