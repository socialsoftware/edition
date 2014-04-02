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

	public void remove() {
		setTaxonomy(null);

		for (FragWordInCategory fragWordInCategory : getFragWordInCategorySet()) {
			fragWordInCategory.remove();
		}

		for (CategoryInFragInter categoryInFragInter : getCategoryInFragInterSet()) {
			categoryInFragInter.remove();
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

	public List<CategoryInFragInter> getSortedCategoryInFragInter() {
		List<CategoryInFragInter> results = new ArrayList<CategoryInFragInter>(
				getCategoryInFragInterSet());

		Collections.sort(results);

		return results;
	}

}
