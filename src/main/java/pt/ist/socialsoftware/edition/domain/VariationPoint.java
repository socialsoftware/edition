package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class VariationPoint extends VariationPoint_Base {

	public VariationPoint() {
		super();
	}

	public void remove() {

		removeFragment();

		for (Reading reading : getInReadings()) {
			removeInReadings(reading);
		}

		for (Reading reading : getOutReadings()) {
			removeOutReadings(reading);
		}

		deleteDomainObject();
	}

	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public Set<FragInter> getInReadingsInterpretations() {
		Set<FragInter> allFragInters;
		if (getInReadings().isEmpty()) {
			allFragInters = new HashSet<FragInter>(getOutReadings().get(0)
					.getFragInters().get(0).getFragment().getFragmentInterSet());
		} else {
			allFragInters = new HashSet<FragInter>();
			for (Reading rdg : getInReadings()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

	public Set<FragInter> getOutReadingsInterpretations() {
		Set<FragInter> allFragInters;
		if (getOutReadings().isEmpty()) {
			allFragInters = new HashSet<FragInter>(getInReadings().get(0)
					.getFragInters().get(0).getFragment().getFragmentInterSet());
		} else {
			allFragInters = new HashSet<FragInter>();
			for (Reading rdg : getOutReadings()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

}
