package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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

	public Tag init(FragInter fragInter, Category category) {
		setFragInter(fragInter);
		setCategory(category);

		return this;
	}

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

	public void undo() {
		this.remove();
	}

	@Atomic(mode = TxMode.WRITE)
	public void dissociate() {
		remove();
	}

}
