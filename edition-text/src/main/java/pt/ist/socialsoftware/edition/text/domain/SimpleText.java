package pt.ist.socialsoftware.edition.text.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.text.deleters.SimpleTextDeleter;
import pt.ist.socialsoftware.edition.text.generators.TextPortionVisitor;

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
		Remover.remove(this);
		super.remove();
	}

	@Component
	public static class Remover {
		public static SimpleTextDeleter simpleTextDeleter;

		@Autowired
		public Remover(SimpleTextDeleter simpleTextDeleter) {
			this.simpleTextDeleter = simpleTextDeleter;
		}

		public static void remove(SimpleText simpleText) {
			simpleTextDeleter.remove(simpleText);
		}
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
