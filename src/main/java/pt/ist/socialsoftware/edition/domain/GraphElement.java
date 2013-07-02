/**
 * 
 */
package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

/**
 * An interface for the text graph elements: Reading, VariationPoint and LdoText
 * and its subclasses
 * 
 * @author ars
 * 
 */
public interface GraphElement {
	public void accept(TextTreeVisitor visitor);

}
