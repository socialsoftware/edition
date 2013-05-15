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
		if (getInReadings().isEmpty()) {
			for (FragInter inter : getFragment().getFragmentInter()) {
				if (inter.getSourceType() != SourceType.VIRTUAL) {
					allFragInters.add(inter);
				}
			}
		} else {
			for (Reading rdg : getInReadings()) {
				allFragInters.addAll(rdg.getFragIntersSet());
			}
		}
		return allFragInters;
	}

	public Set<FragInter> getOutReadingsInters() {
		Set<FragInter> allFragInters = new HashSet<FragInter>();
		if (getOutReadings().isEmpty()) {
			for (FragInter inter : getFragment().getFragmentInter()) {
				if (inter.getSourceType() != SourceType.VIRTUAL) {
					allFragInters.add(inter);
				}
			}
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
		removeFragmentOfStart();

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
		removeFragmentOfStart();

		for (Reading reading : getInReadings()) {
			reading.remove();
		}

		for (Reading reading : getOutReadings()) {
			reading.remove();
		}

		deleteDomainObject();
	}

}
