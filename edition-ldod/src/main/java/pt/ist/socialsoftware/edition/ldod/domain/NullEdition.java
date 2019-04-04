package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.HashSet;
import java.util.Set;

public class NullEdition extends NullEdition_Base {

	@Override
	public Boolean getPub() {
		return true;
	}

	@Override
	public void remove() {
		setText4NullEdition(null);
		super.remove();
	}

	public Set<ScholarInter> getIntersSet() {
		return new HashSet<>();
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
