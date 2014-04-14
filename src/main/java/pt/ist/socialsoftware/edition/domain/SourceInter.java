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
		return getSource().getMetaTextual() + super.getMetaTextual();
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

}