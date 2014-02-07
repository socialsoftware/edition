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
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);

	}

	@Override
	public SimpleText getNextSimpleText(FragInter inter) {
		if (getInterps().contains(inter)) {
			return this;
		} else {
			return super.getNextSimpleText(inter);
		}
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

}
