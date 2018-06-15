package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.ldod.domain.NullEdition_Base;

public class NullEdition extends NullEdition_Base {

	@Override
	public Boolean getPub() {
		return true;
	}

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>();
	}

	@Override
	public Edition.EditionType getSourceType() {
		return Edition.EditionType.AUTHORIAL;
	}

	@Override
	public String getReference() {
		return "";
	}

}
