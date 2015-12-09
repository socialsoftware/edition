package pt.ist.socialsoftware.edition.domain;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;

public abstract class Category extends Category_Base implements Comparable<Category> {

	public enum CategoryType {
		GENERATED("generated"), ADHOC("adhoc");

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

		return this;
	}

	public Category init(Taxonomy taxonomy, String name) {
		setTaxonomy(taxonomy);
		setName(name);

		return this;
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setTaxonomy(null);

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

	@Override
	public int compareTo(Category other) {
		return getName().compareTo(other.getName());
	}

	public Tag getTag(FragInter fragInter) {
		for (Tag tag : getTagSet()) {
			if (tag.getFragInter() == fragInter)
				return tag;
		}
		return null;
	}

	abstract public List<Tag> getSortedTags();

	public boolean isInVirtualEditionInter(VirtualEditionInter inter) {
		return getTagSet().stream().anyMatch(t -> t.getFragInter() == inter);
	}

	public int getWeight(VirtualEditionInter inter) {
		return getTagSet().stream().filter(t -> t.getFragInter() == inter).collect(Collectors.toSet()).size();
	}

}
