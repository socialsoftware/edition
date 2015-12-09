package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Annotation extends Annotation_Base {

	public Annotation(VirtualEditionInter inter, SimpleText startText, SimpleText endText, String quote, String text,
			LdoDUser user) {
		setVirtualEditionInter(inter);
		setStartText(startText);
		setEndText(endText);
		setQuote(quote);
		setText(text);
		setUser(user);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setVirtualEditionInter(null);
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
		for (Taxonomy taxonomy : getVirtualEditionInter().getEdition().getTaxonomiesSet()) {
			if (taxonomy.getAdHoc())
				return taxonomy;
		}

		return null;
	}

	public void updateTags(List<String> tags) {
		for (UserTagInTextPortion tag : getUserTagInTextPortionSet()) {
			if (!tags.contains(tag.getCategory().getName())) {
				tag.remove();
			}
		}

		for (String tag : tags) {
			if (!existsTag(tag)) {
				getTaxonomy().createUserTagInTextPortion(this, tag);
			}
		}

	}

	private boolean existsTag(String name) {
		for (UserTagInTextPortion tag : getUserTagInTextPortionSet()) {
			if (tag.getCategory().getName().equals(name)) {
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
