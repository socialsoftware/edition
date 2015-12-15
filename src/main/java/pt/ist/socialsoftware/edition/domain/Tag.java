package pt.ist.socialsoftware.edition.domain;

public class Tag extends Tag_Base implements Comparable<Tag> {

	public Tag init(VirtualEditionInter inter, Category category, Annotation annotation, LdoDUser user) {
		setInter(inter);
		setCategory(category);
		setAnnotation(annotation);
		setContributor(user);

		return this;
	}

	public void remove() {
		setInter(null);
		setCategory(null);
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
