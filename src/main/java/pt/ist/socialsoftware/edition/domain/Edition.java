package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateValueException;

public abstract class Edition extends Edition_Base {

	public enum SourceType {
		AUTHORIAL, EDITORIAL, VIRTUAL;
	};

	public Edition() {
		super();
	}

	@Override
	public void setAcronym(String acronym) {
		if ((getAcronym() != null && !getAcronym().equals(acronym))
				|| getAcronym() == null) {
			for (ExpertEdition edition : LdoD.getInstance()
					.getExpertEditionsSet()) {
				if (acronym.equals(edition.getAcronym())) {
					throw new LdoDDuplicateValueException();
				}
			}

			for (VirtualEdition edition : LdoD.getInstance()
					.getVirtualEditionsSet()) {
				if (acronym.equals(edition.getAcronym())) {
					throw new LdoDDuplicateValueException();
				}
			}
		}

		super.setAcronym(acronym);
	}

	public abstract SourceType getSourceType();

	public abstract Set<FragInter> getIntersSet();

	public List<FragInter> getSortedInterps() {
		List<FragInter> interps = new ArrayList<FragInter>(getIntersSet());

		Collections.sort(interps);

		return interps;
	}

	public FragInter getNextNumberInter(FragInter inter, int number) {
		List<FragInter> interps = new ArrayList<FragInter>(inter.getEdition()
				.getIntersSet());

		Collections.sort(interps);

		return findNextElementByNumber(inter, number, interps);
	}

	public FragInter getPrevNumberInter(FragInter inter, int number) {
		List<FragInter> interps = new ArrayList<FragInter>(inter.getEdition()
				.getIntersSet());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByNumber(inter, number, interps);
	}

	private FragInter findNextElementByNumber(FragInter inter, int number,
			List<FragInter> interps) {
		Boolean stopNext = false;
		for (FragInter tmpInter : interps) {
			if (stopNext) {
				return tmpInter;
			}
			if ((tmpInter.getNumber() == number) && tmpInter == inter) {
				stopNext = true;
			}
		}
		return interps.get(0);
	}

}
