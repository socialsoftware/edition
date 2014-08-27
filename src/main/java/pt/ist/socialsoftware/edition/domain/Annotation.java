package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Annotation extends Annotation_Base {

	public Annotation(FragInter inter, SimpleText startText,
			SimpleText endText, String quote, String text, LdoDUser user) {
		setFragInter(inter);
		setStartText(startText);
		setEndText(endText);
		setQuote(quote);
		setText(text);
		setUser(user);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setFragInter(null);
		setUser(null);
		setStartText(null);
		setEndText(null);

		for (Range range : getRangeSet()) {
			range.remove();
		}

		for (UserTagInTextPortion tag : getUserTagInTextPortionSet()) {
			tag.remove();
		}

		deleteDomainObject();
	}

	private Taxonomy getTaxonomy() {
		for (UserTagInTextPortion tagInTextPortion : getUserTagInTextPortionSet()) {
			return tagInTextPortion.getCategory().getTaxonomy();
		}
		return null;
	}

	public void updateTags(List<String> tags) {
		for (String tag : tags) {
			if (!existsActiveTag(tag)) {
				getFragInter().createUserTagInTextPortion(getTaxonomy(), this,
						tag);
			}
		}

		for (UserTagInTextPortion tag : getUserTagInTextPortionSet()) {
			if (!tags
					.contains(tag.getActiveTag().getActiveCategory().getName())) {
				tag.removeThisAnnotation(this);
			}
		}
	}

	private boolean existsActiveTag(String name) {
		for (UserTagInTextPortion tag : getUserTagInTextPortionSet()) {
			if (tag.getActiveTag().getActiveCategory().getName().equals(name)) {
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
