package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter_Base;

public class SourceInter extends SourceInter_Base {
	public SourceInter() {
	}

	@Override
	public Heteronym getHeteronym() {
		return getSource().getHeteronym();
	};

	@Override
	public String getShortName() {
		return getSource().getName();
	}

	@Override
	public String getTitle() {
		return getFragment().getTitle();
	}

	@Override
	public LdoDDate getLdoDDate() {
		if (getSource() == null) {
			return null;
		}
		return getSource().getLdoDDate();
	}

	@Override
	public Edition.EditionType getSourceType() {
		return Edition.EditionType.AUTHORIAL;
	}

	public int compareSourceInter(SourceInter other) {
		SourceType thisType = getSource().getType();
		SourceType otherType = other.getSource().getType();

		if (thisType.equals(SourceType.MANUSCRIPT) && otherType.equals(SourceType.MANUSCRIPT)) {
			boolean thisIsManuscript = !((ManuscriptSource) this.getSource()).getHandNoteSet().isEmpty();
			boolean thisIsDactiloscript = !((ManuscriptSource) this.getSource()).getTypeNoteSet().isEmpty();
			boolean otherIsManuscript = !((ManuscriptSource) other.getSource()).getHandNoteSet().isEmpty();
			boolean otherIsDactiloscript = !((ManuscriptSource) other.getSource()).getTypeNoteSet().isEmpty();

			if (thisIsDactiloscript && otherIsDactiloscript) {
				return getShortName().compareTo(other.getShortName());
			}

			if (thisIsManuscript && otherIsManuscript) {
				return getShortName().compareTo(other.getShortName());
			}

			if (thisIsManuscript) {
				return -1;
			} else {
				// dactiloscript
				return 1;
			}
		}

		if (thisType.equals(SourceType.PRINTED) && otherType.equals(SourceType.PRINTED)) {
			return getShortName().compareTo(other.getShortName());
		}

		if (thisType.equals(SourceType.MANUSCRIPT)) {
			return -1;
		} else {
			// printed
			return 1;
		}
	}

	@Override
	public void remove() {
		setSource(null);

		for (VirtualEditionInter inter : getIsUsedByDepthSet()) {
			// it is necessary to remove all interpretations that use the expert
			// interpretation
			inter.remove();
		}

		super.remove();
	}

	@Override
	public int getNumber() {
		return 0;
	}

	@Override
	public boolean belongs2Edition(Edition edition) {
		return false;
	}

	@Override
	public FragInter getLastUsed() {
		return this;
	}

	@Override
	public Edition getEdition() {
		return getFragment().getLdoD().getNullEdition();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = new ArrayList<>();
		return listUses;
	}

	@Override
	public String getReference() {
		return getShortName();
	}

	public Surface getPrevSurface(PbText pbText) {
		if (pbText == null) {
			return null;
		} else {
			PbText prevPbText = pbText.getPrevPbText(this);
			if (prevPbText == null) {
				return getSource().getFacsimile().getFirstSurface();
			} else {
				return prevPbText.getSurface();
			}
		}
	}

	public Surface getNextSurface(PbText pbText) {
		if (pbText == null) {
			if (getFirstPbText() == null) {
				return null;
			} else {
				return getFirstPbText().getSurface();
			}
		} else {
			PbText nextPbText = pbText.getNextPbText(this);
			if (nextPbText == null) {
				return null;
			} else {
				return nextPbText.getSurface();
			}
		}
	}

	private PbText getFirstPbText() {
		PbText firstPbText = null;
		for (PbText pbText : getPbTextSet()) {
			if ((firstPbText == null) || (firstPbText.getOrder() > pbText.getOrder())) {
				firstPbText = pbText;
			}
		}
		return firstPbText;
	}

	public PbText getPrevPbText(PbText pbText) {
		if (pbText == null) {
			return null;
		} else {
			return pbText.getPrevPbText(this);
		}
	}

	public PbText getNextPbText(PbText pbText) {
		if (pbText == null) {
			return getFirstPbText();
		} else {
			return pbText.getNextPbText(this);
		}
	}

	@Override
	public Set<Category> getAllDepthCategories() {
		return new HashSet<>();
	}

	@Override
	public Set<HumanAnnotation> getAllDepthHumanAnnotations() {
		return new HashSet<>();
	}
	
	//solução para suportar os dois tipos de annotations
	@Override
	public Set<Annotation> getAllDepthAnnotations() {
		return new HashSet<>();
	}
	
	@Override
	public Set<Tag> getAllDepthTags() {
		return new HashSet<>();
	}

	@Override
	public int getUsesDepth() {
		return 0;
	}

}
