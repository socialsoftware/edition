package pt.ist.socialsoftware.edition.recommendation.properties;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;

public class Pair {
	private Taxonomy taxonomy;
	private Category category;

	public Pair(Category t, Taxonomy p) {
		this.category = t;
		this.taxonomy = p;
	}

	public Pair(Tag tag) {
		this(tag.getCategory(), tag.getCategory().getTaxonomy());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pair) {
			Pair pair = (Pair) o;
			return category.getName().equals(pair.getCategory().getName()) && (taxonomy == pair.getTaxonomy());
		}
		return false;
	}

	public Category getCategory() {
		return category;
	}

	public Taxonomy getTaxonomy() {
		return taxonomy;
	}

}
