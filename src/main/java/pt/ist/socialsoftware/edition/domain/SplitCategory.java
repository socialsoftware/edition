package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Tag.TagType;

public class SplitCategory extends SplitCategory_Base {

	public SplitCategory init(Taxonomy taxonomy, String name, Category origin,
			Set<Tag> tags) {
		super.init(taxonomy, name);

		setType(CategoryType.EXTRACTED);
		setOriginSplitCategory(origin);
		origin.setDeprecated(true);

		for (Tag tag : tags) {
			if (tag.getType() == TagType.FRAGINTER) {
				new SplitTagInFragInter().init(this, tag);
			} else if (tag.getType() == TagType.TEXTPORTION) {
				new SplitTagInTextPortion().init(this, tag);
			}
		}

		return this;
	}

	@Override
	public void remove() {
		setOriginSplitCategory(null);
		super.remove();
	}

	@Override
	public List<Tag> getSortedTags() {
		List<Tag> tags = new ArrayList<Tag>(getTagSet());

		Collections.sort(tags);

		return tags;
	}

	@Override
	public List<Tag> getSortedActiveTags() {
		return getSortedTags();
	}

	@Atomic(mode = TxMode.WRITE)
	public void undo() {
		Category category = getOriginSplitCategory();

		SplitCategory sibilingCategory = null;
		for (SplitCategory cat : category.getSplitCategorySet()) {
			if (cat != this)
				sibilingCategory = cat;
		}

		category.setDeprecated(false);

		sibilingCategory.remove();
		this.remove();
	}

}
