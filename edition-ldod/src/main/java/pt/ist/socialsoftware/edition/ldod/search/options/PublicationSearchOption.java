package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

public final class PublicationSearchOption extends SearchOption {

	private final DateSearchOption dateSearchOption;

	public PublicationSearchOption(@JsonProperty("date") DateSearchOption date) {
		this.dateSearchOption = date;
	}

	@Override
	public String toString() {
		return "publication:" + dateSearchOption;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(SourceInter.class::isInstance).map(SourceInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	private boolean verifiesSearchOption(SourceInter inter) {
		return inter.getSource().getType().equals(Source.SourceType.PRINTED)
				&& dateSearchOption.verifiesSearchOption(inter);
	}

	public boolean hasDate() {
		return dateSearchOption != null && dateSearchOption.hasDate();
	}

}