package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;

public class Category extends Category_Base {

	public Category(Taxonomy taxonomy) {
		setTaxonomy(taxonomy);
	}

	public Category(Taxonomy taxonomy, String name) {
		setTaxonomy(taxonomy);
		setName(name);
	}

	public void remove() {
		setTaxonomy(null);

		for (FragWordInCategory fragWordInCategory : getFragWordInCategorySet()) {
			fragWordInCategory.remove();
		}

		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		deleteDomainObject();
	}

	@Atomic(mode = TxMode.WRITE)
	@Override
	public void setName(String name) {
		for (Category category : getTaxonomy().getCategoriesSet()) {
			if ((category != this) && (category.getName().equals(name))) {
				throw new LdoDDuplicateNameException();
			}
		}
		super.setName(name);
	}

	public List<FragWordInCategory> getSortedFragWordInCategory() {
		List<FragWordInCategory> results = new ArrayList<FragWordInCategory>(
				getFragWordInCategorySet());

		Collections.sort(results);

		return results;
	}

	public List<Tag> getSortedTag() {
		List<Tag> results = new ArrayList<Tag>(getTagSet());

		Collections.sort(results);

		return results;
	}

	public Tag getTag(FragInter fragInter) {
		for (Tag tag : getTagSet()) {
			if (tag.getFragInter() == fragInter)
				return tag;
		}
		return null;
	}

}
