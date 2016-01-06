package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class Tag extends Tag_Base implements Comparable<Tag> {

	public Tag init(VirtualEditionInter inter, String categoryName, Annotation annotation, LdoDUser user) {
		setInter(inter);
		Taxonomy taxonomy = inter.getVirtualEdition().getTaxonomy();
		Category category = taxonomy.getCategory(categoryName);
		if (category == null)
			if (taxonomy.getOpenVocabulary()) {
				category = taxonomy.createCategory(categoryName);
			} else {
				throw new LdoDException("Create Category with Closed Vocabulary");
			}
		setCategory(category);
		setAnnotation(annotation);
		setContributor(user);

		return this;
	}

	public void remove() {
		setInter(null);
		if (getCategory() != null && getCategory().getTaxonomy().getOpenAnnotation()
				&& getCategory().getTagSet().size() == 1) {
			Category category = getCategory();
			setCategory(null);
			category.remove();
		} else {
			setCategory(null);
		}
		setContributor(null);
		setAnnotation(null);

		deleteDomainObject();
	}

	@Override
	public int compareTo(Tag other) {
		if (this.getWeight() < other.getWeight())
			return 1;
		else if (this.getWeight() > other.getWeight())
			return -1;
		else
			return 0;
	}

	public int getWeight() {
		return 1;
	}

}
