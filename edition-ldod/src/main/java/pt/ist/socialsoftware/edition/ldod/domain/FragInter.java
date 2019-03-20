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
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public abstract class FragInter extends FragInter_Base implements Comparable<FragInter> {
	private static Logger logger = LoggerFactory.getLogger(FragInter.class);

	public int getNumAnnexNotes() {
		throw new LdoDException();
	}

	public void setNumAnnexNotes(int numAnnexNotes){
		throw new LdoDException();
	}

	public abstract String getUrlId();

	public abstract String getXmlId();

	abstract public void remove();

	public abstract String getShortName();

	public abstract int getNumber();

	public abstract String getTitle();

	public abstract Edition.EditionType getSourceType();

	public abstract Edition getEdition();

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

	public abstract Fragment getFragment();

}