package pt.ist.socialsoftware.edition.visitors;

import pt.ist.socialsoftware.edition.domain.TextPortion;

public abstract class FragmentWriter implements TextTreeVisitor {

	protected void propagate2FirstChild(TextPortion text) {
		TextPortion firstChild = text.getFirstChildText();
		if (firstChild != null) {
			firstChild.accept(this);
		}
	}

	protected void propagate2NextSibling(TextPortion text) {
		if (text.getNextText() != null) {
			text.getNextText().accept(this);
		}
	}

}