package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public final class PublicationSearchOption extends SearchOption {

	private final String title;

	public PublicationSearchOption(@JsonProperty("pubPlace") String title) {
		this.title = title;
	}

	@Override
	public boolean visit(SourceInter inter) {
		return inter.getSource().getType().equals(Source.SourceType.PRINTED)
				&& (((PrintedSource) inter.getSource()).getTitle().equals(title) || title.equals(ALL));
	}
}