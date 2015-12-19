package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Taxonomy extends Taxonomy_Base {

	public String getName() {
		return getEdition().getTitle();
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setEdition(null);

		for (Category category : getCategoriesSet()) {
			category.remove();
		}

		for (TaxonomyWeight taxonomyWeight : getTaxonomyWeightSet()) {
			taxonomyWeight.remove();
		}

		deleteDomainObject();
	}

	public Set<Tag> getTagSet(VirtualEditionInter inter) {
		Set<Tag> set = new HashSet<Tag>();
		for (Tag tag : inter.getTagSet()) {
			if (tag.getCategory().getTaxonomy() == this) {
				set.add(tag);
			}
		}
		return set;
	}

	public List<Tag> getSortedTags(VirtualEditionInter fragInter) {
		List<Tag> tags = new ArrayList<Tag>(getTagSet(fragInter));
		Collections.sort(tags);
		return tags;
	}

	public List<VirtualEditionInter> getSortedFragInter() {
		Set<VirtualEditionInter> set = new HashSet<VirtualEditionInter>();
		for (Category category : getCategoriesSet()) {
			for (Tag tag : category.getTagSet()) {
				set.add(tag.getInter());
			}
		}
		List<VirtualEditionInter> list = new ArrayList<VirtualEditionInter>(set);
		Collections.sort(list);

		return list;
	}

	public Set<LdoDUser> getTagContributorSet(VirtualEditionInter inter) {
		Set<LdoDUser> contributors = new HashSet<LdoDUser>();
		for (Tag tag : getTagSet(inter)) {
			contributors.add(tag.getContributor());
		}
		return contributors;
	}

	public Category getCategory(String name) {
		for (Category category : getCategoriesSet()) {
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}

	public List<Category> getSortedCategories(VirtualEditionInter inter) {
		return getCategoriesSet().stream().filter(c -> c.isInVirtualEditionInter(inter))
				.sorted((c1, c2) -> c1.getWeight(inter) < c2.getWeight(inter) ? -1 : 1).collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public Category merge(List<Category> categories) {

		String name = categories.stream().map(c -> c.getName()).collect(Collectors.joining(";"));

		while (getCategory(name) != null) {
			name = name + "1";
		}

		Category category = new Category().init(this, name);

		categories.stream().flatMap(c -> c.getTagSet().stream()).forEach(t -> category.addTag(t));

		categories.stream().forEach(c -> c.remove());

		return category;
	}

	@Atomic(mode = TxMode.WRITE)
	public Category extract(Category category, Set<Tag> tags) {
		Set<Tag> remainingTags = new HashSet<Tag>(category.getSortedTags());
		remainingTags.removeAll(tags);

		Category newCategory = new Category().init(this, category.getName() + "_Ext");

		tags.stream().forEach(t -> newCategory.addTag(t));

		return newCategory;
	}

	public void createTag(Annotation annotation, String tag) {
		if (getCategory(tag) == null)
			new Category().init(this, tag);

		getCategory(tag).createTag(annotation, tag);
	}

	@Atomic(mode = TxMode.WRITE)
	public Category createCategory(String name) {
		return new Category().init(this, name);
	}

}
