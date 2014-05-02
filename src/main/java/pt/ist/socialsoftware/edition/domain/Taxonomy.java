package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;

public class Taxonomy extends Taxonomy_Base {

	Map<String, FragWord> mapOfWords = null;

	public Taxonomy(LdoD ldoD) {
		setLdoD(ldoD);
	}

	public Taxonomy(LdoD ldoD, Edition edition, String name, int numTopics,
			int numWords, int thresholdCategories, int numIterations) {
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

		for (FragWord fragWord : getFragWordSet()) {
			fragWord.remove();
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

	public Set<Tag> getActiveTagSet(FragInter fragInter) {
		Set<Tag> set = new HashSet<Tag>();
		for (Tag tag : getTagSet(fragInter)) {
			if (!tag.getDeprecated()) {
				set.add(tag.getActiveTag());
			}
		}
		return set;
	}

	public List<Tag> getSortedActiveTags(FragInter fragInter) {
		List<Tag> tags = new ArrayList<Tag>(getActiveTagSet(fragInter));
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

	public Category getActiveCategory(String name) {
		for (Category category : getActiveCategorySet()) {
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public MergeCategory merge(List<Category> categories) {
		return new MergeCategory().init(this, categories);
	}

	public Set<Category> getActiveCategorySet() {
		Set<Category> set = new HashSet<Category>();
		for (Category category : getCategoriesSet()) {
			if (!category.getDeprecated()) {
				set.add(category);
			}
		}
		return set;
	}

}
