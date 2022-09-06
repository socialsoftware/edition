package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;

public abstract class Edition extends pt.ist.socialsoftware.edition.ldod.domain.Edition_Base {
	public static final String COELHO_EDITION_ACRONYM = "JPC";
	public static final String CUNHA_EDITION_ACRONYM = "TSC";
	public static final String ZENITH_EDITION_ACRONYM = "RZ";
	public static final String PIZARRO_EDITION_ACRONYM = "JP";
	public static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";
	public static final String COELHO_EDITION_NAME = "Jacinto do Prado Coelho";
	public static final String CUNHA_EDITION_NAME = "Teresa Sobral Cunha";
	public static final String ZENITH_EDITION_NAME = "Richard Zenith";
	public static final String PIZARRO_EDITION_NAME = "Jerónimo Pizarro";
	public static final String ARCHIVE_EDITION_NAME = "Edição do Arquivo LdoD";;

	public enum EditionType {
		AUTHORIAL("authorial"), EDITORIAL("editorial"), VIRTUAL("virtual");

		private final String desc;

		EditionType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return this.desc;
		}
	}

	public void remove() {
		deleteDomainObject();
	}

	@Override
	public void setAcronym(String acronym) {
		if (getAcronym() != null && !getAcronym().equalsIgnoreCase(acronym) || getAcronym() == null) {
			for (ExpertEdition edition : LdoD.getInstance().getExpertEditionsSet()) {
				if (acronym.equalsIgnoreCase(edition.getAcronym())) {
					throw new LdoDDuplicateAcronymException(String.format("DUPLICATE_ACRONYM %s", acronym));
				}
			}

			for (VirtualEdition edition : LdoD.getInstance().getVirtualEditionsSet()) {
				if (edition.getAcronym() != null && acronym.equalsIgnoreCase(edition.getAcronym())) {
					throw new LdoDDuplicateAcronymException();
				}
			}
		}

		super.setAcronym(acronym);
	}

	public abstract EditionType getSourceType();

	public abstract Set<FragInter> getIntersSet();

	public abstract String getReference();

	public List<FragInter> getSortedInterps() {
		return getIntersSet().stream().sorted((i1, i2) -> i1.compareTo(i2)).collect(Collectors.toList());
	}

	public FragInter getNextNumberInter(FragInter inter, int number) {
		List<FragInter> interps = new ArrayList<>(inter.getEdition().getIntersSet());

		Collections.sort(interps);

		return findNextElementByNumber(inter, number, interps);
	}

	public FragInter getPrevNumberInter(FragInter inter, int number) {
		List<FragInter> interps = new ArrayList<>(inter.getEdition().getIntersSet());

		Collections.sort(interps, Collections.reverseOrder());

		return findNextElementByNumber(inter, number, interps);
	}

	private FragInter findNextElementByNumber(FragInter inter, int number, List<FragInter> interps) {
		Boolean stopNext = false;
		for (FragInter tmpInter : interps) {
			if (stopNext) {
				return tmpInter;
			}
			if (tmpInter.getNumber() == number && tmpInter == inter) {
				stopNext = true;
			}
		}
		return interps.get(0);
	}

	public boolean isLdoDEdition() {
		return getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM);
	}

	public FragInter getFragInterByUrlId(String urlId) {
		return getIntersSet().stream().filter(i -> i.getUrlId().equals(urlId)).findFirst().orElse(null);
	}

}
