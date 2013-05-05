package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class DelText extends DelText_Base {

	public enum HowDel {
		OVERSTRIKE, OVERTYPED, OVERWRITTEN, UNSPECIFIED;
	};

	public DelText(OpenClose value, HowDel how) {
		super();
		setOpenClose(value);
		setHow(how);
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
			result = "</del>";
			break;
		case OPEN:
			result = "<del>";
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
				+ "[" + Integer.toString(refsCounter) + "] " + "Retirado - "
				+ getHow().toString() + "<br>";

		return result;
	}

}
