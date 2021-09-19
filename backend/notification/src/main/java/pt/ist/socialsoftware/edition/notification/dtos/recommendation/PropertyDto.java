package pt.ist.socialsoftware.edition.notification.dtos.recommendation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = HeteronymPropertyDto.class, name = PropertyDto.HETERONYM),
        @JsonSubTypes.Type(value = DatePropertyDto.class, name = PropertyDto.DATE),
        @JsonSubTypes.Type(value = TextPropertyDto.class, name = PropertyDto.TEXT),
        @JsonSubTypes.Type(value = TaxonomyPropertyDto.class, name = PropertyDto.TAXONOMY)})
public abstract class PropertyDto {
    public static final String HETERONYM = "heteronym";
    public static final String DATE = "date";
    public static final String TEXT = "text";
    public static final String TAXONOMY = "taxonomy";

    protected final double weight;
    protected final String acronym;

    public PropertyDto(double weight, String acronym) {
        this.weight = weight;
        this.acronym = acronym;
    }

//    public abstract Property getProperty();

    public abstract String getType();

    public double getWeight() {
        return this.weight;
    }

    public String getAcronym() {
        return this.acronym;
    }
}