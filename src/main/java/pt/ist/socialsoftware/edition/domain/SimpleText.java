package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

/**
 * Contains the simple text, no line or pages breaks, spaces, formating, etc
 * 
 * @author ars
 * 
 */
public class SimpleText extends SimpleText_Base {

	public SimpleText(TextPortion parent, String value) {
		parent.addChildText(this);
		setValue(value);
	}

	@Override
	public void remove() {
		for (Annotation annotation : getStartAnnotationsSet()) {
			annotation.remove();
		}

		for (Annotation annotation : getEndAnnotationsSet()) {
			annotation.remove();
		}

		super.remove();
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);

	}

	@Override
	public SimpleText getNextSimpleText(FragInter inter) {
		return this;
	}

	@Override
	public Boolean getBreakWord() {
		return true;
	}

	@Override
	public String getSeparator(FragInter inter) {
		if (getInterps().contains(inter)) {
			String separators = ".,?!:;";
			String separator = null;

			String firstChar = getValue().substring(0, 1);

			if (separators.contains(firstChar) || !super.getBreakWord()) {
				separator = "";
			} else {
				separator = " ";
			}
			return separator;
		} else {
			return super.getSeparator(inter);
		}
	}

	@Override
	public SimpleText getSimpleText(FragInter inter, int currentOffset,
			int offset) {
		int nextCurrentOffset = currentOffset + getSeparator(inter).length()
				+ getValue().length();

		if (nextCurrentOffset >= offset) {
			return this;
		} else {
			return super.getNextSimpleText(inter).getSimpleText(inter,
					nextCurrentOffset, offset);
		}
	}

}
