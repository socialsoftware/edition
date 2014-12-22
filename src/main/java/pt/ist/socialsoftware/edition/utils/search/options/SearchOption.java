package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = EditionSearchOption.class, name = SearchOption.EDITION),
		@Type(value = ManuscriptSearchOption.class, name = SearchOption.MANUSCRIPT),
		@Type(value = DactiloscryptSearchOption.class, name = SearchOption.DACTILOSCRIPT),
		@Type(value = PublicationSearchOption.class, name = SearchOption.PUBLICATION),
		@Type(value = HeteronymSearchOption.class, name = SearchOption.HETERONYM),
		@Type(value = DateSearchOption.class, name = SearchOption.DATE),
		@Type(value = TaxonomySearchOption.class, name = SearchOption.TAXONOMY),
		@Type(value = TextSearchOption.class, name = SearchOption.TEXT) })
public abstract class SearchOption {
	/* Json Properties */
	public static final String EDITION = "edition";
	public static final String MANUSCRIPT = "manuscript";
	public static final String DACTILOSCRIPT = "dactiloscript";
	public static final String PUBLICATION = "publication";
	public static final String HETERONYM = "heteronym";
	public static final String DATE = "date";
	public static final String TEXT = "text";
	public static final String TAXONOMY = "taxonomy";
	/* Search options to include everything */
	public static final String ALL = "all";

	/* Search modes */
	public enum Mode {
		AND("and"), OR("or");

		private final String mode;

		private Mode(String mode){
			this.mode = mode;
		}

		public String getMode() {
			return mode;
		}
	} 

	public boolean visit(ExpertEditionInter inter) {
		return false;
	}

	public boolean visit(SourceInter inter) {
		return false;
	}

	public boolean visit(VirtualEditionInter inter){
		return false;
	}

	public static boolean chooseMode(Mode mode, boolean belongsToResulSet, boolean working) {
		if(mode.equals(Mode.AND))
			return belongsToResulSet && working;
		else if(mode.equals(Mode.OR))
			return belongsToResulSet || working;
		else
			return false;
	}

	@Override
	public abstract String toString();
}