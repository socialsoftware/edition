package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Edition.SourceType;
import pt.ist.socialsoftware.edition.utils.NullFactory;

public class SourceInter extends SourceInter_Base {

	public SourceInter() {
		setHeteronym(new NullHeteronym());
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
	public SourceType getSourceType() {
		return SourceType.AUTHORIAL;
	}

	public int compareSourceInter(SourceInter other) {
		return getSource().getName().compareTo(other.getSource().getName());
	}

	@Override
	public String getMetaTextual() {
		return getSource().getMetaTextual();
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
		return NullFactory.getNullEditionInstance();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = new ArrayList<FragInter>();
		return listUses;
	}

}