/**
 * 
 */
package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.generators.TextPortionVisitor;

/**
 * An interface for the text graph elements: Reading, VariationPoint and LdoText
 * and its subclasses
 * 
 * @author ars
 * 
 */
public interface GraphElement {
	public void accept(TextPortionVisitor visitor);

}
