package pt.ist.socialsoftware.edition.ldod.domain;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.Category_Base;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public class Category extends Category_Base implements Comparable<Category> {

	public String getUrlId() {
		String result = Normalizer.normalize(getName(), Normalizer.Form.NFD);
		return result.replaceAll("[^\\p{ASCII}]", "");
	}

	@Override
	public String getXmlId() {
		return getTaxonomy().getEdition().getXmlId() + "." + getName();
	}

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
		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		setTaxonomy(null);

		deleteDomainObject();
	}

	public static String purgeName(String name) {
		return name.replaceAll("[^\\p{L}0-9\\-\\s]+", "");
	}

	@Atomic(mode = TxMode.WRITE)
	@Override
	public void setName(String name) {
		String purgedName = Category.purgeName(name);

		if (purgedName == null || purgedName.equals("")) {
			throw new LdoDException("Category::setName is null or empty name");
		}

		for (Category category : getTaxonomy().getCategoriesSet()) {
			if ((category != this) && (category.getName().equals(purgedName))) {
				throw new LdoDDuplicateNameException();
			}
		}
		super.setName(purgedName);
	}

	@Override
	public int compareTo(Category other) {
		return getName().compareTo(other.getName());
	}

	public List<Tag> getSortedTags() {
		List<Tag> tags = new ArrayList<>(getTagSet());
		Collections.sort(tags);
		return tags;
	}

	public List<Tag> getSortedTags(VirtualEdition virtualEdition) {
		return getTagSet().stream().filter(t -> t.getInter().getVirtualEdition() == virtualEdition).sorted()
				.collect(Collectors.toList());
	}

	public List<VirtualEditionInter> getSortedInters() {
		return getTagSet().stream().map(t -> t.getInter()).distinct()
				.sorted((i1, i2) -> i1.getTitle().compareTo(i2.getTitle())).collect(Collectors.toList());
	}

	public List<VirtualEditionInter> getSortedInters(VirtualEdition virtualEdition) {
		return getTagSet().stream().map(t -> t.getInter()).filter(i -> i.getVirtualEdition() == virtualEdition)
				.distinct().sorted((i1, i2) -> i1.getTitle().compareTo(i2.getTitle())).collect(Collectors.toList());
	}

	public List<LdoDUser> getSortedUsers() {
		return getTagSet().stream().map(t -> t.getContributor()).distinct()
				.sorted((u1, u2) -> u1.getUsername().compareTo(u2.getUsername())).collect(Collectors.toList());
	}

	public List<VirtualEdition> getSortedEditions() {
		return getTagSet().stream().map(t -> t.getInter().getVirtualEdition()).distinct()
				.sorted((ve1, ve2) -> ve1.getTitle().compareTo(ve2.getTitle())).collect(Collectors.toList());
	}

	public String getNameInEditionContext(VirtualEdition edition) {
		return edition == getTaxonomy().getEdition() ? getName()
				: getTaxonomy().getEdition().getAcronym().toUpperCase() + "." + getName();
	}

	public int compareInEditionContext(VirtualEdition edition, Category other) {
		if (this.getTaxonomy() == other.getTaxonomy()) {
			return compareTo(other);
		} else if (edition.getTaxonomy() == this.getTaxonomy()) {
			return -1;
		} else if (edition.getTaxonomy() == other.getTaxonomy()) {
			return 1;
		} else {
			return this.getTaxonomy().getEdition().getAcronym()
					.compareTo(other.getTaxonomy().getEdition().getAcronym());
		}
	}

}
