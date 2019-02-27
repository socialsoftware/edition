package pt.ist.socialsoftware.edition.ldod.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventInterface;

public abstract class FragInter extends FragInter_Base implements Comparable<FragInter> {
	private static Logger logger = LoggerFactory.getLogger(FragInter.class);

	public String getUrlId() {
		return getXmlId().replace(".", "_");
	}

	public void remove() {
		setFragment(null);
		setHeteronym(null);

		if (getLdoDDate() != null) {
			getLdoDDate().remove();
		}

		System.out.println("Must send event!");

		EventInterface eventInterface = new EventInterface();
		if(!((this instanceof VirtualEditionInter) || (this instanceof ExpertEditionInter))) {
            eventInterface.publish(new Event(Event.EventType.FRAG_INTER_REMOVE, this.getXmlId()));
        }

		System.out.println("Event has been sent!");

        for (VirtualEditionInter inter : getIsUsedBySet()) {
			//removeIsUsedBy(inter);
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

		// adicionado recentemente, testar
		getInfoRangeSet().forEach(infoRange -> infoRange.remove());

		deleteDomainObject();
	}

	public abstract String getShortName();

	public abstract int getNumber();

	public abstract String getTitle();

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
			} else if (getSourceType() == Edition.EditionType.VIRTUAL
					&& other.getSourceType() == Edition.EditionType.EDITORIAL) {
				return 1;
			} else if (getSourceType() == Edition.EditionType.VIRTUAL
					&& other.getSourceType() == Edition.EditionType.AUTHORIAL) {
				return 1;
			}
		} else if (getSourceType() == other.getSourceType()) {
			if (getSourceType() == Edition.EditionType.EDITORIAL) {
				return ((ExpertEditionInter) this).compareExpertEditionInter((ExpertEditionInter) other);
			} else if (getSourceType() == Edition.EditionType.VIRTUAL) {
				return ((VirtualEditionInter) this).compareVirtualEditionInter((VirtualEditionInter) other);
			} else if (getSourceType() == Edition.EditionType.AUTHORIAL) {
				return ((SourceInter) this).compareSourceInter((SourceInter) other);
			}
		}
		return 0;
	}

	public abstract boolean belongs2Edition(Edition edition);

	public abstract FragInter getLastUsed();

	public abstract String getReference();

	public List<AnnexNote> getSortedAnnexNote() {
		List<AnnexNote> results = new ArrayList<>(getAnnexNoteSet());

		Collections.sort(results);

		return results;
	}

	// solução a funcionar
	public abstract Set<HumanAnnotation> getAllDepthHumanAnnotations();

	// tentativa de suporte de ambas as anotações
	public abstract Set<Annotation> getAllDepthAnnotations();

	public abstract Set<Tag> getAllDepthTags();

	public abstract Set<Category> getAllDepthCategories();

	public abstract int getUsesDepth();

	public long getNumberOfTwitterCitationsSince(LocalDateTime editionBeginDateTime) {
		return getInfoRangeSet().stream().map(ir -> ir.getCitation())
				.filter(cit -> cit.getFormatedDate().isAfter(editionBeginDateTime)).count();
	}

}