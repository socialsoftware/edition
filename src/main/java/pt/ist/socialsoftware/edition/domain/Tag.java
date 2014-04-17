package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public abstract class Tag extends Tag_Base implements Comparable<Tag> {

	public void remove() {
		setFragInter(null);
		setCategory(null);

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

	public abstract int getWeight();

	public abstract Set<LdoDUser> getContributorSet();
}
