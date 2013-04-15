package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

/**
 * Contains the simple text, no line or pages breaks, spaces, formating, etc
 * 
 * @author ars
 * 
 */
public class SimpleText extends SimpleText_Base {

	public SimpleText(String value) {
		super();
		setValue(value);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);

	}

}
