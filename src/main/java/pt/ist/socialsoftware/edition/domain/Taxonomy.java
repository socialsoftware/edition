package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Category.CategoryType;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;

public class Taxonomy extends Taxonomy_Base {

	public Taxonomy(LdoD ldoD) {
		setLdoD(ldoD);
	}

	public Taxonomy(LdoD ldoD, Edition edition, String name, int numTopics, int numWords, int thresholdCategories,
			int numIterations) {
		setAdHoc(false);
		setLdoD(ldoD);
		setEdition(edition);
		setName(name);
		setNumTopics(numTopics);
		setNumWords(numWords);
		setThresholdCategories(thresholdCategories);
		setNumIterations(numIterations);
	}

	public Taxonomy(LdoD ldoD, Edition edition, String name) {
		setAdHoc(true);
		setLdoD(ldoD);
		setEdition(edition);
		setName(name);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setLdoD(null);
		setEdition(null);

		for (Category category : getCategoriesSet()) {
			category.remove();
		}

		for (TaxonomyWeight taxonomyWeight : getTaxonomyWeightSet()) {
			taxonomyWeight.remove();
		}

		deleteDomainObject();
	}

	@Override
	public void setName(String name) {
		Set<Taxonomy> taxonomies = null;
		if (getEdition() != null) {
			taxonomies = getEdition().getTaxonomiesSet();
		} else {
			taxonomies = LdoD.getInstance().getTaxonomiesSet();
		}

		for (Taxonomy taxonomy : taxonomies) {
			if ((taxonomy != this) && (taxonomy.getName().equals(name))) {
				throw new LdoDDuplicateNameException();
			}
		}

		super.setName(name);
	}

	public Set<Tag> getTagSet(FragInter fragInter) {
		Set<Tag> set = new HashSet<Tag>();
		for (Tag tag : fragInter.getTagSet()) {
			if (tag.getCategory().getTaxonomy() == this) {
				set.add(tag);
			}
		}
		return set;
	}

	public List<Tag> getSortedTags(FragInter fragInter) {
		List<Tag> tags = new ArrayList<Tag>(getTagSet(fragInter));
		Collections.sort(tags);
		return tags;
	}

	public List<FragInter> getSortedFragInter() {
		Set<FragInter> set = new HashSet<FragInter>();
		for (Category category : getCategoriesSet()) {
			for (Tag tag : category.getTagSet()) {
				set.add(tag.getFragInter());
			}
		}
		List<FragInter> list = new ArrayList<FragInter>(set);
		Collections.sort(list);

		return list;
	}

	public Category getCategory(String name) {
		for (Category category : getCategoriesSet()) {
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public Category merge(List<Category> categories) {

		String name = categories.stream().map(c -> c.getName()).collect(Collectors.joining(";"));

		while (getCategory(name) != null) {
			name = name + "1";
		}

		Category category;
		if (categories.get(0).getType().equals(CategoryType.ADHOC)) {
			category = new AdHocCategory().init(this, name);
		} else {
			category = new GeneratedCategory().init(this, name);
		}

		categories.stream().flatMap(c -> c.getTagSet().stream()).forEach(t -> category.addTag(t));

		categories.stream().forEach(c -> c.remove());

		return category;
	}

	@Atomic(mode = TxMode.WRITE)
	public Category extract(Category category, Set<Tag> tags) {
		Set<Tag> remainingTags = new HashSet<Tag>(category.getSortedTags());
		remainingTags.removeAll(tags);

		Category newCategory;
		if (category.getType().equals(CategoryType.ADHOC)) {
			newCategory = new AdHocCategory().init(this, category.getName() + "-Extracted");
		} else {
			newCategory = new GeneratedCategory().init(this, category.getName() + "-Extracted");
		}

		tags.stream().forEach(t -> newCategory.addTag(t));

		return newCategory;
	}

}
