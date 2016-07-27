package pt.ist.socialsoftware.edition.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	public boolean visit(SourceInter inter) {
		return inter.getSource().getType().equals(Source.SourceType.PRINTED) && inter.accept(date);
	}

	public boolean hasDate() {
		return date == null ? false : date.hasDate();
	}

}