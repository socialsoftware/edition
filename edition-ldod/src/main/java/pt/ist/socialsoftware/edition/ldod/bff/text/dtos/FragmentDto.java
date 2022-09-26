package pt.ist.socialsoftware.edition.ldod.bff.text.dtos;

import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

import java.util.List;
import java.util.Map;

public class FragmentDto {

    private String externalId;
    private String title;
    private String xmlId;
    private Map<String, List<EditorialInterDto>> expertsInterMap;
    private List<?> sortedSourceInterList;
    private List<?> inters;
    private List<String> transcriptions;

    private List<List<String>> variations;

    public FragmentDto(Fragment fragment, Map<String, List<EditorialInterDto>> expertsInterMap, List<?> sortedSourceInterList) {
        setExternalId(fragment.getExternalId());
        setTitle(fragment.getTitle());
        setXmlId(fragment.getXmlId());
        setExpertsInterMap(expertsInterMap);
        setSortedSourceInterList(sortedSourceInterList);
    }



    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public Map<String, List<EditorialInterDto>> getExpertsInterMap() {
        return expertsInterMap;
    }

    public void setExpertsInterMap(Map<String, List<EditorialInterDto>> expertsInterMap) {
        this.expertsInterMap = expertsInterMap;
    }

    public List<?> getSortedSourceInterList() {
        return sortedSourceInterList;
    }

    public void setSortedSourceInterList(List<?> sortedSourceInterList) {
        this.sortedSourceInterList = sortedSourceInterList;
    }

    private boolean isManuscript(SourceInter sourceInter) {
        return sourceInter.getSource().getType().equals(Source.SourceType.MANUSCRIPT);
    }

    public List<?> getInters() {
        return inters;
    }

    public void setInters(List<?> inters) {
        this.inters = inters;
    }

    public List<String> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<String> transcriptions) {
        this.transcriptions = transcriptions;
    }

    public List<List<String>> getVariations() {
        return variations;
    }

    public void setVariations(List<List<String>> variations) {
        this.variations = variations;
    }

    public static final class FragmentDtoBuilder {
        private final FragmentDto fragmentDto;

        private FragmentDtoBuilder(Fragment fragment, Map<String, List<EditorialInterDto>> expertsInterMap, List<?> sortedSourceInterList) {
            fragmentDto = new FragmentDto(fragment, expertsInterMap, sortedSourceInterList);
        }

        public static FragmentDtoBuilder aFragmentDto(Fragment fragment, Map<String, List<EditorialInterDto>> expertsInterMap, List<?> sortedSourceInterList) {
            return new FragmentDtoBuilder(fragment, expertsInterMap, sortedSourceInterList);
        }

        public FragmentDtoBuilder inters(List<FragInterDto> inters) {
            fragmentDto.setInters(inters);
            return this;
        }


        public FragmentDtoBuilder transcription(List<String> transcriptions) {
            fragmentDto.setTranscriptions(transcriptions);
            return this;

        }

        public FragmentDtoBuilder variations(List<List<String>> variations) {
            fragmentDto.setVariations(variations);
            return this;

        }

        public FragmentDto build() {
            return fragmentDto;
        }
    }
}

