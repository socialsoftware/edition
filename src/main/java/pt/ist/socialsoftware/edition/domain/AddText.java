package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class AddText extends AddText_Base {

	public enum Place {
		ABOVE, SUPERIMPOSED, MARGIN, TOP, INLINE, UNSPECIFIED;
	};

	public AddText(OpenClose open, Place place) {
		super();
		setOpenClose(open);
		setPlace(place);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
