package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.utils.RangeJson;

public abstract class FragInter extends FragInter_Base implements
		Comparable<FragInter> {

	public void remove() {
		setFragment(null);
		setHeteronym(null);

		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		for (Annotation annotation : getAnnotationSet()) {
			annotation.remove();
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

		for (AnnexNote annexNote : getAnnexNoteSet()) {
			annexNote.remove();
		}

		for (RefText ref : getRefTextSet()) {
			ref.setFragInter(null);
		}

		deleteDomainObject();
	}

	public abstract String getShortName();

	public abstract int getNumber();

	public abstract String getTitle();

	public abstract EditionType getSourceType();

	public abstract Edition getEdition();

	public abstract List<FragInter> getListUsed();

	@Override
	public int compareTo(FragInter other) {
		if (getSourceType() != other.getSourceType()) {
			if (getSourceType() == EditionType.EDITORIAL) {
				return -1;
			} else if (getSourceType() == EditionType.AUTHORIAL) {
				return 1;
			} else if ((getSourceType() == EditionType.VIRTUAL)
					&& (other.getSourceType() == EditionType.EDITORIAL)) {
				return 1;
			} else if ((getSourceType() == EditionType.VIRTUAL)
					&& (other.getSourceType() == EditionType.AUTHORIAL)) {
				return 1;
			}
		} else if (getSourceType() == other.getSourceType()) {
			if (getSourceType() == EditionType.EDITORIAL) {
				return ((ExpertEditionInter) this)
						.compareExpertEditionInter((ExpertEditionInter) other);
			} else if (getSourceType() == EditionType.VIRTUAL) {
				return ((VirtualEditionInter) this)
						.compareVirtualEditionInter((VirtualEditionInter) other);
			} else if (getSourceType() == EditionType.AUTHORIAL) {
				return ((SourceInter) this)
						.compareSourceInter((SourceInter) other);
			}
		}
		return 0;
	}

	public String getMetaTextual() {
		String result = "";
		for (AnnexNote note : getSortedAnnexNote()) {
			result = result + "(" + note.getNumber() + ") "
					+ note.getNoteText().generatePresentationText() + "<br>";
		}
		return result;
	}

	public abstract boolean belongs2Edition(Edition edition);

	public abstract FragInter getLastUsed();

	public abstract String getReference();

	@Atomic(mode = TxMode.WRITE)
	public Annotation createAnnotation(String quote, String text,
			LdoDUser user, List<RangeJson> rangeList, List<String> tagList) {

		Taxonomy taxonomy = getEdition().getTaxonomy("adHoc");
		if (taxonomy == null) {
			taxonomy = new Taxonomy(LdoD.getInstance(), getEdition(), "adHoc");
		}

		SimpleText startText = getFragment().getTextPortion().getSimpleText(
				getLastUsed(), 0, rangeList.get(0).getStartOffset());
		SimpleText endText = getFragment().getTextPortion().getSimpleText(
				getLastUsed(), 0, rangeList.get(0).getEndOffset());

		Annotation annotation = new Annotation(this, startText, endText, quote,
				text, user);

		for (RangeJson rangeJson : rangeList) {
			new Range(annotation, rangeJson.getStart(),
					rangeJson.getStartOffset(), rangeJson.getEnd(),
					rangeJson.getEndOffset());
		}

		for (String tag : tagList) {
			createUserTagInTextPortion(taxonomy, annotation, tag);
		}

		return annotation;
	}

	public void createUserTagInTextPortion(Taxonomy taxonomy,
			Annotation annotation, String tag) {
		Category category = taxonomy.getActiveCategory(tag);
		if (category == null) {
			category = new AdHocCategory();
			category.init(taxonomy, tag);
			new UserTagInTextPortion().init(category, annotation);
		} else {
			UserTagInTextPortion tagInTextPortion = (UserTagInTextPortion) category
					.getTag(this);
			if (tagInTextPortion == null) {
				new UserTagInTextPortion().init(category, annotation);
			} else {
				tagInTextPortion.addAnnotation(annotation);
			}
		}
	}

	public List<AnnexNote> getSortedAnnexNote() {
		List<AnnexNote> results = new ArrayList<AnnexNote>(getAnnexNoteSet());

		Collections.sort(results);

		return results;
	}

	@Atomic(mode = TxMode.WRITE)
	public void associate(LdoDUser ldoDUser, Taxonomy taxonomy,
			Set<Category> categories) {
		for (Category category : categories) {
			Tag existTag = null;
			UserTagInFragInter userTag = null;
			for (Tag tag : taxonomy.getTagSet(this)) {
				if (tag.getCategory() == category) {
					existTag = tag;
					if (existTag instanceof UserTagInFragInter) {
						userTag = (UserTagInFragInter) tag;
						userTag.setContributor(ldoDUser);
						userTag.setDeprecated(false);
					}
					break;
				}

			}

			if (userTag == null) {
				userTag = new UserTagInFragInter().init(this, category,
						ldoDUser, existTag);
			}
		}
	}
}
