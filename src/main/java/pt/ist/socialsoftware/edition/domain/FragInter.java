package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.domain.Edition.EditionType;

public abstract class FragInter extends FragInter_Base implements Comparable<FragInter> {
	private static Logger logger = LoggerFactory.getLogger(FragInter.class);

	public void remove() {
		setFragment(null);
		setHeteronym(null);

		if (getLdoDDate() != null) {
			getLdoDDate().remove();
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
			} else if ((getSourceType() == EditionType.VIRTUAL) && (other.getSourceType() == EditionType.EDITORIAL)) {
				return 1;
			} else if ((getSourceType() == EditionType.VIRTUAL) && (other.getSourceType() == EditionType.AUTHORIAL)) {
				return 1;
			}
		} else if (getSourceType() == other.getSourceType()) {
			if (getSourceType() == EditionType.EDITORIAL) {
				return ((ExpertEditionInter) this).compareExpertEditionInter((ExpertEditionInter) other);
			} else if (getSourceType() == EditionType.VIRTUAL) {
				return ((VirtualEditionInter) this).compareVirtualEditionInter((VirtualEditionInter) other);
			} else if (getSourceType() == EditionType.AUTHORIAL) {
				return ((SourceInter) this).compareSourceInter((SourceInter) other);
			}
		}
		return 0;
	}

	public String getMetaTextual() {
		String result = "";
		for (AnnexNote note : getSortedAnnexNote()) {
			result = result + "(" + note.getNumber() + ") " + note.getNoteText().generatePresentationText() + "<br>";
		}
		return result;
	}

	public abstract boolean belongs2Edition(Edition edition);

	public abstract FragInter getLastUsed();

	public abstract String getReference();

	public List<AnnexNote> getSortedAnnexNote() {
		List<AnnexNote> results = new ArrayList<AnnexNote>(getAnnexNoteSet());

		Collections.sort(results);

		return results;
	}

	public abstract Set<Annotation> getAllDepthAnnotations();

	public abstract Set<Tag> getAllDepthTags();

	public abstract Set<Category> getAllDepthCategories();

}