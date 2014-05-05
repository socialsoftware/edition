package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;

public abstract class Category extends Category_Base {

	public enum CategoryType {
		GENERATED("generated"), ADHOC("adhoc"), MERGED("merged"), EXTRACTED(
				"extracted");

		private final String desc;

		CategoryType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public Category init(Taxonomy taxonomy) {
		setTaxonomy(taxonomy);
		setDeprecated(false);

		return this;
	}

	public Category init(Taxonomy taxonomy, String name) {
		setTaxonomy(taxonomy);
		setName(name);
		setDeprecated(false);

		return this;
	}

	public void remove() {
		setTaxonomy(null);

		if (getMergeCategory() != null)
			getMergeCategory().remove();

		for (SplitCategory category : getSplitCategorySet()) {
			category.remove();
		}

		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		deleteDomainObject();
	}

	@Atomic(mode = TxMode.WRITE)
	@Override
	public void setName(String name) {
		for (Category category : getTaxonomy().getActiveCategorySet()) {
			if ((category != this) && (category.getName().equals(name))) {
				throw new LdoDDuplicateNameException();
			}
		}
		super.setName(name);
	}

	protected Set<Tag> getActiveTags() {
		Set<Tag> tags = new HashSet<Tag>();
		for (Tag tag : getTagSet()) {
			if (!tag.getDeprecated()) {
				tags.add(tag);
			}
		}
		return tags;
	}

	abstract public List<Tag> getSortedTags();

	abstract public List<Tag> getSortedActiveTags();

	public Tag getTag(FragInter fragInter) {
		for (Tag tag : getTagSet()) {
			if (tag.getFragInter() == fragInter)
				return tag;
		}
		return null;
	}

	public Category getActiveCategory() {
		if (!getDeprecated()) {
			return this;
		} else if (getMergeCategory() != null) {
			return getMergeCategory().getActiveCategory();
		} else {
			return null;
		}
	}
}
