package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class GeneratedCategory extends GeneratedCategory_Base {

	@Override
	public GeneratedCategory init(Taxonomy taxonomy) {
		super.init(taxonomy);
		setType(CategoryType.GENERATED);

		return this;
	}

	@Override
	public GeneratedCategory init(Taxonomy taxonomy, String name) {
		super.init(taxonomy, name);
		setType(CategoryType.GENERATED);

		return this;
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		for (FragWordInCategory fragWordInCategory : getFragWordInCategorySet()) {
			fragWordInCategory.remove();
		}

		super.remove();
	}

	public List<FragWordInCategory> getSortedFragWordInCategory() {
		List<FragWordInCategory> results = new ArrayList<FragWordInCategory>(
				getFragWordInCategorySet());

		Collections.sort(results);

		return results;
	}

	@Override
	public List<Tag> getSortedTags() {
		List<Tag> tags = new ArrayList<Tag>(getTagSet());

		Collections.sort(tags);

		return tags;
	}

	@Override
	public List<Tag> getSortedActiveTags() {
		List<Tag> tags = new ArrayList<Tag>(getActiveTags());

		Collections.sort(tags);

		return tags;
	}

}
