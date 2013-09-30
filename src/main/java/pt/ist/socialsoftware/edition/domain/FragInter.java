package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.Edition.SourceType;

public abstract class FragInter extends FragInter_Base implements
		Comparable<FragInter> {

	public FragInter() {
		super();
	}

	public void remove() {
		setFragment(null);
		setHeteronym(null);

		for (Annotation annotation : getAnnotationSet()) {
			annotation.remove();
		}

		for (Category cat : getCategoriesSet()) {
			removeCategories(cat);
		}

		for (VirtualEditionInter inter : getIsUsedBySet()) {
			removeIsUsedBy(inter);
		}

		for (RdgText rdg : getRdgSet()) {
			removeRdg(rdg);
		}

		for (LbText lb : getLbTextSet()) {
			removeLbText(lb);
		}

		for (PbText pb : getPbTextSet()) {
			removePbText(pb);
		}

		deleteDomainObject();
	}

	public abstract String getShortName();

	public abstract int getNumber();

	public abstract String getTitle();

	public abstract SourceType getSourceType();

	public abstract Edition getEdition();

	public abstract List<FragInter> getListUsed();

	@Override
	public int compareTo(FragInter other) {
		if (getSourceType() != other.getSourceType()) {
			if (getSourceType() == SourceType.EDITORIAL) {
				return -1;
			} else if (getSourceType() == SourceType.AUTHORIAL) {
				return 1;
			} else if ((getSourceType() == SourceType.VIRTUAL)
					&& (other.getSourceType() == SourceType.EDITORIAL)) {
				return 1;
			} else if ((getSourceType() == SourceType.VIRTUAL)
					&& (other.getSourceType() == SourceType.AUTHORIAL)) {
				return 1;
			}
		} else if (getSourceType() == other.getSourceType()) {
			if (getSourceType() == SourceType.EDITORIAL) {
				return ((ExpertEditionInter) this)
						.compareExpertEditionInter((ExpertEditionInter) other);
			} else if (getSourceType() == SourceType.VIRTUAL) {
				return ((VirtualEditionInter) this)
						.compareVirtualEditionInter((VirtualEditionInter) other);
			} else if (getSourceType() == SourceType.AUTHORIAL) {
				return ((SourceInter) this)
						.compareSourceInter((SourceInter) other);
			}
		}
		return 0;
	}

	public abstract String getMetaTextual();

	public abstract boolean belongs2Edition(Edition edition);

	public abstract FragInter getLastUsed();

	public Set<Tag> getTagSet() {
		Set<Tag> tags = new HashSet<Tag>();
		for (Annotation annotation : getAnnotationSet()) {
			tags.addAll(annotation.getTagSet());
		}
		return tags;
	}

	public abstract String getReference();

}
