package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;
import pt.ist.socialsoftware.edition.ldod.domain.SimpleText_Base;

/**
 * Contains the simple text, no line or pages breaks, spaces, formating, etc
 * 
 * @author ars
 * 
 */
public class SimpleText extends SimpleText_Base {
	private static Logger logger = LoggerFactory.getLogger(SimpleText.class);

	public SimpleText(TextPortion parent, String value) {
		parent.addChildText(this);
		setValue(value);
	}

	@Override
	public void remove() {
		for (HumanAnnotation annotation : getStartAnnotationsSet()) {
			annotation.remove();
		}

		for (HumanAnnotation annotation : getEndAnnotationsSet()) {
			annotation.remove();
		}

		super.remove();
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
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
	public SimpleText getSimpleText(FragInter inter, int currentOffset, int offset) {
		int nextCurrentOffset = currentOffset + getSeparator(inter).length() + getValue().length();

		logger.debug("getSimpleText value:{}, separator:{}, currentOffset:{}, offset:{}, nextCurrentOffset:{}",
				getValue(), getSeparator(inter), currentOffset, offset, nextCurrentOffset);

		if (nextCurrentOffset >= offset) {
			return this;
		} else {
			return super.getNextSimpleText(inter).getSimpleText(inter, nextCurrentOffset, offset);
		}
	}

}
