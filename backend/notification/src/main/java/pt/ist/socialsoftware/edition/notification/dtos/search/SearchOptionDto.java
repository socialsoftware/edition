package pt.ist.socialsoftware.edition.notification.dtos.search;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@Type(value = EditionSearchOptionDto.class, name = SearchOptionDto.EDITION),
        @Type(value = ManuscriptSearchOptionDto.class, name = SearchOptionDto.MANUSCRIPT),
        @Type(value = TypescriptSearchOptionDto.class, name = SearchOptionDto.DACTILOSCRIPT),
        @Type(value = PublicationSearchOptionDto.class, name = SearchOptionDto.PUBLICATION),
        @Type(value = HeteronymSearchOptionDto.class, name = SearchOptionDto.HETERONYM),
        @Type(value = DateSearchOptionDto.class, name = SearchOptionDto.DATE),
        @Type(value = TaxonomySearchOptionDto.class, name = SearchOptionDto.TAXONOMY),
        @Type(value = TextSearchOptionDto.class, name = SearchOptionDto.TEXT),
        @Type(value = VirtualEditionSearchOptionDto.class, name = SearchOptionDto.VIRTUALEDITION)})
public abstract class SearchOptionDto {
    /* Json Properties */
    public static final String EDITION = "edition";
    public static final String MANUSCRIPT = "manuscript";
    public static final String DACTILOSCRIPT = "dactiloscript";
    public static final String PUBLICATION = "publication";
    public static final String HETERONYM = "heteronym";
    public static final String DATE = "date";
    public static final String TEXT = "text";
    public static final String TAXONOMY = "taxonomy";
    public static final String VIRTUALEDITION = "virtualedition";


//    public abstract SearchOption createSearchOption();

}