package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class NullEdition extends NullEdition_Base {

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>();
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.AUTHORIAL;
	}

	@Override
	public String getReference() {
		return "";
	}

}
