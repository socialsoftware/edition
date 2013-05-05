package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class VariationPoint extends VariationPoint_Base {

	public VariationPoint() {
		super();
	}

	@Override
	public Fragment getFragment() {
		if (getInReadings().isEmpty()) {
			return super.getFragment();
		} else {
			return getInReadings().get(0).getPreviousVariationPoint()
					.getFragment();
		}
	}

	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public Set<FragInter> getInReadingsInters() {
		Set<FragInter> allFragInters;
		if (getInReadings().isEmpty()) {
			allFragInters = new HashSet<FragInter>(getFragment()
					.getFragmentInterSet());
		} else {
			allFragInters = new HashSet<FragInter>();
			for (Reading rdg : getInReadings()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

	public Set<FragInter> getOutReadingsInters() {
		Set<FragInter> allFragInters;
		if (getOutReadings().isEmpty()) {
			allFragInters = new HashSet<FragInter>(getFragment()
					.getFragmentInterSet());
		} else {
			allFragInters = new HashSet<FragInter>();
			for (Reading rdg : getOutReadings()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

	public void removeOnlyThis() {
		removeFragment();

		for (Reading reading : getInReadings()) {
			removeInReadings(reading);
		}

		for (Reading reading : getOutReadings()) {
			removeOutReadings(reading);
		}

		deleteDomainObject();
	}

	public void remove() {
		removeFragment();

		for (Reading reading : getInReadings()) {
			removeInReadings(reading);
		}

		for (Reading reading : getOutReadings()) {
			reading.remove();
		}

		System.out.println("DELETE=" + getIdInternal());

		deleteDomainObject();
	}

}
