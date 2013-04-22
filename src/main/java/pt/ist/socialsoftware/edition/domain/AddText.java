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

	@Override
	public String writeHtml() {
		String result = "";
		switch (getOpenClose()) {
		case CLOSE:
			result = "</ins>";
			break;
		case OPEN:
			result = "<ins>";
			break;
		case NO:
			assert false;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public String writeReference(int refsCounter) {
		String result = "";
		switch (getOpenClose()) {
		case CLOSE:
			result = "</a>";
			break;
		case OPEN:
			result = "<a href=\"#" + Integer.toString(refsCounter) + "\">";
			break;
		case NO:
			assert false;
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public String writeNote(int refsCounter) {
		String result = "<a id =\"" + Integer.toString(refsCounter) + "\"></a>"
				+ "[" + Integer.toString(refsCounter) + "] " + "Adicionado - "
				+ getPlace().toString() + "<br>";

		return result;
	}

}
