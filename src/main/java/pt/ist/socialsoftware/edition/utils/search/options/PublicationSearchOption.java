package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public final class PublicationSearchOption extends SearchOption {

	private final String pubPlace;

	public PublicationSearchOption(@JsonProperty("pubPlace") String pubPlace){
		this.pubPlace = pubPlace;
	}

	@Override
	public boolean visit(SourceInter inter) {
		return inter.getSource().getType().equals(Source.SourceType.PRINTED) && 
				(((PrintedSource)inter.getSource()).getPubPlace().equals(pubPlace) || 
						((PrintedSource)inter.getSource()).getPubPlace().equals(ALL));
	}
}