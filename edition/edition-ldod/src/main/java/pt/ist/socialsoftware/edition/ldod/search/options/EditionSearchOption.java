package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;

public final class EditionSearchOption extends SearchOption {

	private final boolean inclusion;
	private final String edition;
	private final HeteronymSearchOption heteronymSearchOption;
	private final DateSearchOption dateSearchOption;

	@JsonCreator
	public EditionSearchOption(@JsonProperty("inclusion") String inclusion, @JsonProperty("edition") String edition,
			@JsonProperty("heteronym") HeteronymSearchOption heteronym, @JsonProperty("date") DateSearchOption date) {

		if (inclusion.equals("in"))
			this.inclusion = true;
		else
			this.inclusion = false;

		this.edition = edition;
		this.heteronymSearchOption = heteronym;
		this.dateSearchOption = date;
	}

	@Override
	public String toString() {
		return "edition:" + edition + "\ninclusion:" + inclusion + "\n" + heteronymSearchOption + "\n" + dateSearchOption;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(ExpertEditionInter.class::isInstance).map(ExpertEditionInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	private boolean verifiesSearchOption(ExpertEditionInter inter) {
		if (inclusion) {
			if (edition.equals(ALL)) {
				return true;
			}
			if (!(edition.equals(inter.getEdition().getAcronym()))) {
				return false;
			}
			if (heteronymSearchOption != null && !heteronymSearchOption.verifiesSearchOption(inter)) {
				return false;
			}
			if (dateSearchOption != null && !dateSearchOption.verifiesSearchOption(inter)) {
				return false;
			}
		} else if ((edition.equals(inter.getEdition().getAcronym()) || edition.equals(ALL)) || (heteronymSearchOption != null
				&& heteronymSearchOption.verifiesSearchOption(inter)) || (dateSearchOption != null && dateSearchOption.verifiesSearchOption(inter)))
			return false;

		return true;
	}

	public boolean hasDate() {
		return dateSearchOption == null ? false : dateSearchOption.hasDate();
	}

	public boolean hasHeteronym() {
		return heteronymSearchOption == null ? false : heteronymSearchOption.hasHeteronym();
	}

	public String getEdition() {
		return edition;
	}
}
