package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragWord extends FragWord_Base {

	public FragWord(Taxonomy taxonomy, String word) {
		setTaxonomy(taxonomy);
		setWord(word);
	}

	public void remove() {
		setTaxonomy(null);

		for (FragWordInCategory fragWordInCategory : getFragWordInCategorySet()) {
			fragWordInCategory.remove();
		}

		deleteDomainObject();
	}

	public List<FragWordInCategory> getSortedFragWordInCategory() {
		List<FragWordInCategory> results = new ArrayList<FragWordInCategory>(
				getFragWordInCategorySet());

		Collections.sort(results);

		return results;
	}

}
