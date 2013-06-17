package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.FragInter.SourceType;
import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class VariationPoint extends VariationPoint_Base {

	public VariationPoint(Fragment fragment) {
		super();
		setFragment(fragment);
	}

	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

	public Set<FragInter> getInReadingsInters() {
		Set<FragInter> allFragInters = new HashSet<FragInter>();
		if (getInReadingsSet().isEmpty()) {
			for (FragInter inter : getFragment().getFragmentInterSet()) {
				if (inter.getSourceType() != SourceType.VIRTUAL) {
					allFragInters.add(inter);
				}
			}
		} else {
			for (Reading rdg : getInReadingsSet()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

	public Set<FragInter> getOutReadingsInters() {
		Set<FragInter> allFragInters = new HashSet<FragInter>();
		if (getOutReadingsSet().isEmpty()) {
			for (FragInter inter : getFragment().getFragmentInterSet()) {
				if (inter.getSourceType() != SourceType.VIRTUAL) {
					allFragInters.add(inter);
				}
			}
		} else {
			allFragInters = new HashSet<FragInter>();
			for (Reading rdg : getOutReadingsSet()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

	public void removeOnlyThis() {
		setFragment(null);
		setFragmentOfStart(null);

		for (Reading reading : getInReadingsSet()) {
			removeInReadings(reading);
		}

		for (Reading reading : getOutReadingsSet()) {
			removeOutReadings(reading);
		}

		deleteDomainObject();
	}

	public void remove() {
		setFragment(null);
		setFragmentOfStart(null);

		for (Reading reading : getInReadingsSet()) {
			reading.remove();
		}

		for (Reading reading : getOutReadingsSet()) {
			reading.remove();
		}

		deleteDomainObject();
	}

}
