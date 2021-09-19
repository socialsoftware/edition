package pt.ist.socialsoftware.edition.virtual.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.LocalDate;
import org.springframework.core.ParameterizedTypeReference;
import pt.ist.socialsoftware.edition.notification.config.CustomLocalDateDeserializer;
import pt.ist.socialsoftware.edition.notification.config.CustomLocalDateSerializer;
import pt.ist.socialsoftware.edition.virtual.domain.Member;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualEditionDto {

    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    private final String xmlId;
    private final String acronym;

    // cached attributes
    private String externalId;
    private String title;
    private String reference;
    private String synopsis;
    private boolean isLdoDEdition;
    private LocalDate date;
    private boolean pub;
    private boolean openVocabulary;
    private String shortAcronym;
    private int max;

    public VirtualEditionDto(VirtualEdition virtualEdition) {
        this.xmlId = virtualEdition.getXmlId();
        this.acronym = virtualEdition.getAcronym();
        this.externalId = virtualEdition.getExternalId();
        this.title = virtualEdition.getTitle();
        this.reference = virtualEdition.getReference();
        this.isLdoDEdition = virtualEdition.isLdoDEdition();
        this.date = virtualEdition.getDate();
        this.pub = virtualEdition.getPub();
        this.openVocabulary = virtualEdition.getTaxonomy().getOpenVocabulary();
        this.synopsis = virtualEdition.getSynopsis();
        this.shortAcronym = virtualEdition.getShortAcronym();
        this.max = virtualEdition.getMaxFragNumber();
    }

    public String getXmlId() {
        return this.xmlId;
    }

    public String getAcronym() {
        return this.acronym;
    }

    public String getExternalId() {
        //return this.virtualProvidesInterface.getVirtualEditionExternalIdByAcronym(this.acronym);
        return this.externalId;
    }

    public String getTitle() {
        //return this.virtualProvidesInterface.getVirtualEditionTitleByAcronym(this.acronym);
        return this.title;
    }

    public String getReference() {
        //return this.virtualProvidesInterface.getVirtualEditionReference(this.acronym);
        return this.reference;
    }

    public boolean getTaxonomyVocabularyStatus() {
        //return this.virtualProvidesInterface.getVirtualEditionTaxonomyVocabularyStatus(this.acronym);
        return this.openVocabulary;
    }

    public String getShortAcronym() {
        return shortAcronym;
    }

    public boolean isLdoDEdition() {
        //return this.virtualProvidesInterface.isLdoDEdition(this.acronym);
        return this.isLdoDEdition;
    }


    public boolean getPub() {
       //return this.virtualProvidesInterface.getVirtualEditionPub(this.acronym);
       return this.pub;
    }

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    public LocalDate getDate() {
        //return this.virtualProvidesInterface.getVirtualEditionDate(this.acronym);
        return this.date;
    }


    public boolean getOpenVocabulary() {
        //return this.virtualProvidesInterface.getOpenVocabulary(this.acronym);
        return this.openVocabulary;
    }

    public int getMaxFragNumber() {
        return max;
    }


    public String getSynopsis() {
        return this.synopsis;
    }

    public Set<String> getParticipantSet() {
         return this.virtualProvidesInterface.getVirtualEditionParticipantSet(this.acronym);
    }

}
