package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class SubstText extends SubstText_Base {

	public SubstText(OpenClose value) {
		super();
		setOpenClose(value);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String writeHtml() {
		String result = "";
		switch (getOpenClose()) {
		case CLOSE:
			result = "</span>";
			break;
		case OPEN:
			result = "<span style=\"background-color: rgb(255,255,0);\">";
			break;
		case NO:
			assert false;
			break;
		default:
			break;
		}
		return result;
	}

}
