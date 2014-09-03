package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Edition.EditionType;

public class SourceInter extends SourceInter_Base {

	public SourceInter() {
		setHeteronym(NullHeteronym.getNullHeteronym());
	}

	@Override
	public String getShortName() {
		return getSource().getName();
	}

	@Override
	public String getTitle() {
		return getFragment().getTitle();
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.AUTHORIAL;
	}

	public int compareSourceInter(SourceInter other) {
		return getSource().getName().compareTo(other.getSource().getName());
	}

	@Override
	public String getMetaTextual() {
		String result = "Heterónimo: " + getHeteronym().getName() + "<br>";

		return result + getSource().getMetaTextual() + super.getMetaTextual();
	}

	@Override
	public void remove() {
		setSource(null);

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
		List<FragInter> listUses = new ArrayList<FragInter>();
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
			if (prevPbText == null)
				return getSource().getFacsimile().getFirstSurface();
			else
				return prevPbText.getSurface();
		}
	}

	public Surface getNextSurface(PbText pbText) {
		if (pbText == null) {
			if (getFirstPbText() == null)
				return null;
			else
				return getFirstPbText().getSurface();
		} else {
			PbText nextPbText = pbText.getNextPbText(this);
			if (nextPbText == null)
				return null;
			else
				return nextPbText.getSurface();
		}
	}

	private PbText getFirstPbText() {
		PbText firstPbText = null;
		for (PbText pbText : getPbTextSet()) {
			if ((firstPbText == null)
					|| (firstPbText.getOrder() > pbText.getOrder()))
				firstPbText = pbText;
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
}