package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateAcronymException;

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
					throw new LdoDDuplicateAcronymException();
				}
			}

			for (VirtualEdition edition : LdoD.getInstance()
					.getVirtualEditionsSet()) {
				if (acronym.equals(edition.getAcronym())) {
					throw new LdoDDuplicateAcronymException();
				}
			}
		}

		super.setAcronym(acronym);
	}

	public abstract SourceType getSourceType();

	public abstract Set<FragInter> getIntersSet();

	public abstract String getReference();

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

	public Set<Tag> getTagSet() {
		Set<Tag> tags = new HashSet<Tag>();
		for (FragInter inter : getIntersSet()) {
			tags.addAll(inter.getTagSet());
		}
		return tags;
	}

}
