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
		setLdoD(ldoD);
		setEdition(edition);
		setName(name);
		setNumTopics(numTopics);
		setNumWords(numWords);
		setThresholdCategories(thresholdCategories);
		setNumIterations(numIterations);
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

	public List<CategoryInFragInter> getSortedCategoryInFragInter(
			FragInter fragInter) {
		List<CategoryInFragInter> list = new ArrayList<CategoryInFragInter>();
		for (CategoryInFragInter categoryInFragInter : fragInter
				.getCategoryInFragInterSet()) {
			if (categoryInFragInter.getCategory().getTaxonomy() == this) {
				list.add(categoryInFragInter);
			}
		}

		Collections.sort(list);

		return list;
	}

	public List<FragInter> getSortedFragInter() {
		Set<FragInter> set = new HashSet<FragInter>();

		for (Category category : getCategoriesSet()) {
			for (CategoryInFragInter categoryInFragInter : category
					.getCategoryInFragInterSet()) {
				set.add(categoryInFragInter.getFragInter());
			}
		}

		List<FragInter> list = new ArrayList<FragInter>(set);
		Collections.sort(list);

		return list;
	}
}
