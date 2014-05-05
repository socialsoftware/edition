package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public abstract class Tag extends Tag_Base implements Comparable<Tag> {

	public enum TagType {
		TEXTPORTION("textportion"), FRAGINTER("fraginter");

		private final String desc;

		TagType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public Tag init() {
		setDeprecated(false);

		return this;
	}

	public void remove() {
		setFragInter(null);
		setCategory(null);

		if (getMergeTag() != null)
			getMergeTag().remove();

		if (getSplitTag() != null)
			getSplitTag().remove();

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

	public Category getActiveCategory() {
		return getActiveTag().getCategory().getActiveCategory();
	}

	public Tag getActiveTag() {
		if (!getDeprecated()) {
			return this;
		} else if (getMergeTag() != null) {
			return getMergeTag().getActiveTag();
		} else {
			return getSplitTag().getActiveTag();
		}
	}

	public abstract int getWeight();

	public abstract Set<LdoDUser> getContributorSet();

	public void undo() {
		this.remove();
	}

}
