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
		List<FragInter> interps = new ArrayList<FragInter>(
				getFragmentInterSet());

		Collections.sort(interps);

		return interps;
	}

	public ExpertEditionInter getExpertEditionInter(String editor) {
		for (FragInter inter : getFragmentInterSet()) {
			if (inter.getSourceType() == SourceType.EDITORIAL) {
				ExpertEditionInter edInter = (ExpertEditionInter) inter;
				if (edInter.getExpertEdition().getEditor().equals(editor)) {
					return edInter;
				}
			}
		}
		return null;
	}

	public void remove() {
		setLdoD(null);

		for (FragInter inter : getFragmentInterSet()) {
			inter.remove();
		}

		for (Source source : getSourcesSet()) {
			source.remove();
		}

		for (VariationPoint point : getVariationPointsSet()) {
			point.remove();
		}

		deleteDomainObject();
	}

}
