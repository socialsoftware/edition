package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Annotation extends Annotation_Base {

	public Annotation(FragInter inter, String quote, String text, LdoDUser user) {
		setFragInter(inter);
		setQuote(quote);
		setText(text);
		setUser(user);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setFragInter(null);
		setUser(null);

		for (Range range : getRangeSet()) {
			range.remove();
		}

		for (Tag tag : getTagSet()) {
			removeTag(tag);
		}

		deleteDomainObject();
	}

	public void updateTags(List<String> tags) {
		for (String tagName : tags) {
			if (!existTag(tagName)) {
				Tag.create(this, tagName);
			}
		}

		for (Tag tag : getTagSet()) {
			if (!tags.contains(tag.getTag())) {
				removeTag(tag);
			}
		}
	}

	private boolean existTag(String tagName) {
		for (Tag tag : getTagSet()) {
			if (tag.getTag().equals(tagName)) {
				return true;
			}
		}
		return false;
	}

	@Atomic(mode = TxMode.WRITE)
	public void update(String text, List<String> tags) {
		setText(text);
		updateTags(tags);
	}

}
