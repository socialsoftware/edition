/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;

/**
 * The visitor pattern applied to the graph of text
 * 
 * @author ars
 * 
 */
public interface GraphVisitor {
	public void visit(VariationPoint variationPoint);

	public void visit(Reading reading);

	public void visit(SimpleText text);

	public void visit(LbText lbText);

	public void visit(FormatText formatText);

	public void visit(PbText pbText);

	public void visit(SpaceText spaceText);

	public void visit(EmptyText emptyText);
}
