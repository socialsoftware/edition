package pt.ist.socialsoftware.edition.text.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jasper.tagplugins.jstl.core.Remove;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.text.deleters.FragInterDeleter;

public abstract class FragInter extends FragInter_Base implements Comparable<FragInter> {
	private static Logger logger = LoggerFactory.getLogger(FragInter.class);

	public String getUrlId() {
		return getXmlId().replace(".", "_");
	}

	public void remove() {

		Remover.remove(this);
//		setFragment(null);
//		setHeteronym(null);
//
//		if (getLdoDDate() != null) {
//			getLdoDDate().remove();
//		}
//
//		//		TODO REMOVE VIRTUAL RELATIONS
////		for (VirtualEditionInter inter : getIsUsedBySet()) {
////			removeIsUsedBy(inter);
////		}
//
//		for (RdgText rdg : getRdgSet()) {
//			removeRdg(rdg);
//		}
//
//		for (LbText lb : getLbTextSet()) {
//			removeLbText(lb);
//		}
//
//		for (PbText pb : getPbTextSet()) {
//			removePbText(pb);
//		}
//
//		for (AnnexNote annexNote : getAnnexNoteSet()) {
//			annexNote.remove();
//		}
//
//		for (RefText ref : getRefTextSet()) {
//			ref.setFragInter(null);
//		}
//
//		//		TODO REMOVE VIRTUAL RELATIONS
//		// adicionado recentemente, testar
////		getInfoRangeSet().forEach(infoRange -> infoRange.remove());

		deleteDomainObject();
	}

	@Component
	public static class Remover {
		public static FragInterDeleter fragInterDeleter;

		@Autowired
		public Remover(FragInterDeleter fragInterDeleter) {
			this.fragInterDeleter = fragInterDeleter;
		}

		public static void remove(FragInter fragInter) {fragInterDeleter.remove(fragInter);}
	}

	public abstract String getShortName();

	public abstract int getNumber();

	public abstract String getTitle();

	public abstract String getReference();

	public abstract Edition.EditionType getSourceType();

	public abstract Edition getEdition();

	public abstract List<FragInter> getListUsed();

	@Override
	public int compareTo(FragInter other) {
		if (getSourceType() != other.getSourceType()) {
			if (getSourceType() == Edition.EditionType.EDITORIAL) {
				return -1;
			} else if (getSourceType() == Edition.EditionType.AUTHORIAL) {
				return 1;
			} else if ((getSourceType() == Edition.EditionType.VIRTUAL)
					&& (other.getSourceType() == Edition.EditionType.EDITORIAL)) {
				return 1;
			} else if ((getSourceType() == Edition.EditionType.VIRTUAL)
					&& (other.getSourceType() == Edition.EditionType.AUTHORIAL)) {
				return 1;
			}
		} else if (getSourceType() == other.getSourceType()) {
			if (getSourceType() == Edition.EditionType.EDITORIAL) {
				return ((ExpertEditionInter) this).compareExpertEditionInter((ExpertEditionInter) other);
			} else if (getSourceType() == Edition.EditionType.AUTHORIAL) {
				return ((SourceInter) this).compareSourceInter((SourceInter) other);
			}
		}
		return 0;
	}

	public abstract boolean belongs2Edition(Edition edition);

	public List<AnnexNote> getSortedAnnexNote() {
		List<AnnexNote> results = new ArrayList<>(getAnnexNoteSet());

		Collections.sort(results);

		return results;
	}

}