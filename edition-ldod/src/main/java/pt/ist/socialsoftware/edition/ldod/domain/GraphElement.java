/**
 * 
 */
package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

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
