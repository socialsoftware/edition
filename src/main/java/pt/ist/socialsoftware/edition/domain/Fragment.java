package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.FragInter.SourceType;

public class Fragment extends Fragment_Base {

	public Fragment() {
		super();
	}

	public List<FragInter> getSortedInterps() {
		List<FragInter> interps = new ArrayList<FragInter>(getFragmentInter());

		Collections.sort(interps);

		return interps;
	}

	public EditionInter getEditionInter(String editor) {
		for (FragInter inter : getFragmentInter()) {
			if (inter.getSourceType() == SourceType.EDITORIAL) {
				EditionInter edInter = (EditionInter) inter;
				if (edInter.getEdition().getEditor().equals(editor)) {
					return edInter;
				}
			}
		}
		return null;
	}

	public void remove() {
		removeLdoD();

		for (FragInter inter : getFragmentInter()) {
			inter.remove();
		}

		for (Source source : getSources()) {
			source.remove();
		}

		getVariationPoint().remove();

		deleteDomainObject();
	}

}
