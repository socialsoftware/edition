package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class VariationPoint extends VariationPoint_Base {

	public VariationPoint() {
		super();
	}

	public void print() {
		// Set<VariationPoint> nextPoints = new HashSet<VariationPoint>();
		for (Reading reading : getOutReadings()) {
			reading.print();
			// nextPoints.add(reading.getNextVariationPoint());
		}
		// for (VariationPoint vp : nextPoints) {
		// vp.print();
		// }
	}

	public void print(FragInter fragInter) {
		// Boolean exists = false;
		// Set<VariationPoint> nextPoints = new HashSet<VariationPoint>();
		for (Reading reading : getOutReadings()) {
			reading.print(fragInter);
			// if (reading.getFragmentInterSet().contains(fragInter)) {
			// reading.print();
			// reading.getNextVariationPoint().print(fragInter);
			// nextPoints.add(reading.getNextVariationPoint());
			// exists = true;
			// break;
			// }
		}
		// if (!exists)
		// for (VariationPoint nextPoint : nextPoints) {
		// nextPoint.print(fragInter);
		// }
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
