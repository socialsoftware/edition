package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

import java.util.List;

public class VirtualEditionDto {

    private String externalId;
    private String acronym;
    private String title;
    private String date;
    private boolean pub;
    private String useEdition;
    private List<String> activeMembers;
    private List<String> pendingMembers;
    private List<String> categories;
    private List<String> annotations;

    private VeUserDto member;
    private boolean selected;
    private boolean taxonomyOpenManagement;
    private boolean taxonomyOpenAnnotation;
    private boolean taxonomyOpenVocab;
    private String mediaSource;
    private String beginDate;
    private String endDate;
    private Integer minFrequency;
    private String countries;
    private String synopsis;
    private List<VeUserDto> participants;

    private List<FragInterDto> virtualEditionInters;

    public VirtualEditionDto() {
    }


    public VirtualEditionDto(VirtualEdition virtualEdition) {
        setExternalId(virtualEdition.getExternalId());
        setTitle(StringEscapeUtils.unescapeHtml(virtualEdition.getTitle()));
        setAcronym(virtualEdition.getAcronym());
        setDate(virtualEdition.getDate().toString());
        setPub(virtualEdition.getPub());
        setUseEdition(virtualEdition.getUses() != null ? virtualEdition.getUses().getAcronym() : "");
        if (virtualEdition.getTaxonomy() != null) {
            Taxonomy t = virtualEdition.getTaxonomy();
            setTaxonomyOpenManagement(t.getOpenManagement());
            setTaxonomyOpenAnnotation(t.getOpenAnnotation());
            setTaxonomyOpenVocab(t.getOpenVocabulary());
        }
        setMediaSource(virtualEdition.getMediaSource() != null ? virtualEdition.getMediaSource().getName() : "");
        if (virtualEdition.getTimeWindow() != null) {
            setBeginDate(virtualEdition.getTimeWindow() != null
                    ? virtualEdition.getTimeWindow().getBeginDate() != null
                    ? virtualEdition.getTimeWindow().getBeginDate().toString()
                    : null
                    : null);
            setEndDate(virtualEdition.getTimeWindow() != null
                    ? virtualEdition.getTimeWindow().getEndDate() != null
                    ? virtualEdition.getTimeWindow().getEndDate().toString()
                    : null
                    : null);
        }
        setCountries(virtualEdition.getGeographicLocation() != null ? virtualEdition.getGeographicLocation().getCountry() : null);
        setMinFrequency(virtualEdition.getFrequency() != null ? virtualEdition.getFrequency().getFrequency() : null);
        setSynopsis(StringEscapeUtils.unescapeHtml(virtualEdition.getSynopsis()));
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getCountries() {
        return countries;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public List<FragInterDto> getVirtualEditionInters() {
        return virtualEditionInters;
    }

    public void setVirtualEditionInters(List<FragInterDto> virtualEditionInters) {
        this.virtualEditionInters = virtualEditionInters;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getMinFrequency() {
        return minFrequency;
    }

    public void setMinFrequency(Integer minFrequency) {
        this.minFrequency = minFrequency;
    }


    public String getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }

    public boolean isTaxonomyOpenVocab() {
        return taxonomyOpenVocab;
    }

    public void setTaxonomyOpenVocab(boolean taxonomyOpenVocab) {
        this.taxonomyOpenVocab = taxonomyOpenVocab;
    }

    public boolean isTaxonomyOpenManagement() {
        return taxonomyOpenManagement;
    }

    public void setTaxonomyOpenManagement(boolean taxonomyOpenManagement) {
        this.taxonomyOpenManagement = taxonomyOpenManagement;
    }

    public boolean isTaxonomyOpenAnnotation() {
        return taxonomyOpenAnnotation;
    }

    public void setTaxonomyOpenAnnotation(boolean taxonomyOpenAnnotation) {
        this.taxonomyOpenAnnotation = taxonomyOpenAnnotation;
    }

    public List<VeUserDto> getParticipants() {
        return participants;
    }

    public void setParticipants(List<VeUserDto> participants) {
        this.participants = participants;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<String> getPendingMembers() {
        return pendingMembers;
    }

    public void setPendingMembers(List<String> pendingMembers) {
        this.pendingMembers = pendingMembers;
    }

    public List<String> getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(List<String> activeMembers) {
        this.activeMembers = activeMembers;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public boolean isPub() {
        return pub;
    }

    public void setPub(boolean pub) {
        this.pub = pub;
    }

    public String getUseEdition() {
        return useEdition;
    }

    public void setUseEdition(String useEdition) {
        this.useEdition = useEdition;
    }


    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }


    public VeUserDto getMember() {
        return member;
    }

    public void setMember(VeUserDto member) {
        this.member = member;
    }


    public static final class VirtualEditionDtoBuilder {
        private final VirtualEditionDto virtualEditionDto;

        private VirtualEditionDtoBuilder(VirtualEdition ve) {
            virtualEditionDto = new VirtualEditionDto(ve);
        }

        public static VirtualEditionDtoBuilder aVirtualEditionDto(VirtualEdition ve) {
            return new VirtualEditionDtoBuilder(ve);
        }

        public VirtualEditionDtoBuilder activeMembers(List<String> members) {
            virtualEditionDto.setActiveMembers(members);
            return this;
        }

        public VirtualEditionDtoBuilder pendingMembers(List<String> members) {
            virtualEditionDto.setPendingMembers(members);
            return this;
        }

        public VirtualEditionDtoBuilder member(LdoDUser user, VirtualEdition ve, LdoDUser actor) {
            virtualEditionDto.setSelected(user != null && user.getSelectedVirtualEditionsSet().contains(ve));
            if (user == null) return this;
            virtualEditionDto.setMember(new VeUserDto(user, ve, actor));
            return this;
        }


        public VirtualEditionDtoBuilder categories(List<String> categories) {
            virtualEditionDto.setCategories(categories);
            return this;
        }

        public VirtualEditionDtoBuilder annotations(List<String> annotations) {
            virtualEditionDto.setAnnotations(annotations);
            return this;
        }

        public VirtualEditionDtoBuilder participants(List<VeUserDto> participants) {
            virtualEditionDto.setParticipants(participants);
            return this;
        }

        public VirtualEditionDtoBuilder virtualEditionInters(List<FragInterDto> inters) {
            virtualEditionDto.setVirtualEditionInters(inters);
            return this;
        }


        public VirtualEditionDto build() {
            return virtualEditionDto;
        }
    }
}
