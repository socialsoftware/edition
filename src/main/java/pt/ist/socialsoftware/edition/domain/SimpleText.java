package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

/**
 * Contains the simple text, no line or pages breaks, spaces, formating, etc
 * 
 * @author ars
 * 
 */
public class SimpleText extends SimpleText_Base {

	public SimpleText() {
		super();
	}

	@Override
	public void print() {
		System.out.print(getValue() + " ");
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);

	}

}
