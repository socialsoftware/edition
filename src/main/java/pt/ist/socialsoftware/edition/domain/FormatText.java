package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class FormatText extends FormatText_Base {

	public enum Rendition {
		RIGHT, LEFT, CENTER, BOLD, RED, UNDERLINED;
	};

	public FormatText(OpenClose value) {
		super();
		setOpenClose(value);
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public String writeHtml() {
		String result = "";
		if (getRend() == Rendition.RIGHT) {
			if (getOpenClose() == OpenClose.OPEN) {
				result = "<div class=\"text-right\">";
			} else {
				result = "</div>";
			}

		} else if (getRend() == Rendition.LEFT) {
			if (getOpenClose() == OpenClose.OPEN) {
				result = "<div class=\"text-left\">";
			} else {
				result = "</div>";
			}

		} else if (getRend() == Rendition.CENTER) {
			if (getOpenClose() == OpenClose.OPEN) {
				result = "<div class=\"text-center\">";
			} else {
				result = "</div>";
			}
		} else if (getRend() == Rendition.BOLD) {
			if (getOpenClose() == OpenClose.OPEN) {
				result = "<strong>";
			} else {
				result = "</strong>";
			}

		} else if (getRend() == Rendition.RED) {
			if (getOpenClose() == OpenClose.OPEN) {
				result = "<span style=\"color: rgb(255,0,0);\"";
			} else {
				result = "</span>";
			}
		} else if (getRend() == Rendition.UNDERLINED) {
			if (getOpenClose() == OpenClose.OPEN) {
				result = "<div style=\"text-decoration: underline;\">";
			} else {
				result = "</span>";
			}
		}
		return result;
	}
}
