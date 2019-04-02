package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

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
	public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
		return inters.filter(searchableElement -> searchableElement.getType() == SearchableElement.Type.SCHOLAR_INTER)
				.filter(i -> verifiesSearchOption(i));
	}

	private boolean verifiesSearchOption(SearchableElement inter) {
		TextInterface textInterface = new TextInterface();
		if (textInterface.isExpertInter(inter.getXmlId()) && inclusion) {
			if (edition.equals(ALL)) {
				return true;
			}
			if (!(edition.equals(textInterface.getEditionAcronymOfInter(inter.getXmlId())))) {
				return false;
			}
			if (heteronymSearchOption != null && !heteronymSearchOption.verifiesSearchOption(inter)) {
				return false;
			}
			if (dateSearchOption != null && !dateSearchOption.verifiesSearchOption(inter)) {
				return false;
			}
		} else if ((edition.equals(textInterface.getEditionAcronymOfInter(inter.getXmlId())) || edition.equals(ALL)) || (heteronymSearchOption != null
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
