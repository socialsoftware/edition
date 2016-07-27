package pt.ist.socialsoftware.edition.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public final class PublicationSearchOption extends SearchOption {

	private final DateSearchOption date;

	public PublicationSearchOption(@JsonProperty("date") DateSearchOption date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "publication:" + date;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(SourceInter.class::isInstance).map(SourceInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	@Override
	public boolean visit(SourceInter inter) {
		return verifiesSearchOption(inter);
	}

	private boolean verifiesSearchOption(SourceInter inter) {
		return inter.getSource().getType().equals(Source.SourceType.PRINTED) && inter.accept(date);
	}

	public boolean hasDate() {
		return date == null ? false : date.hasDate();
	}

}