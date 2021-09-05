//package pt.ist.socialsoftware.edition.api.textDto;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class FragmentBaseDto {
//
//    private String xmlId;
//
//    //cached attributes
//    private String title;
//    private String externalId;
//
//    private Set<SourceDto> embeddedSourceDtos = new HashSet<>(  );
//
//    private Set<ScholarInterBaseDto> embeddedScholarInterDtos = new HashSet<>();
//
//    public FragmentBaseDto() {  }
//
//    public FragmentBaseDto(FragmentDto fragmentDto) {
//        this.xmlId = fragmentDto.getXmlId();
//        this.title = fragmentDto.getTitle();
//        this.externalId = fragmentDto.getExternalId();
//    }
//
//    public String getXmlId() {
//        return this.xmlId;
//    }
//
//    public void setXmlId(String xmlId) {
//        this.xmlId = xmlId;
//    }
//
//
//    public Set<SourceDto> getEmbeddedSourceDtos() {
//        return embeddedSourceDtos;
//    }
//
//    public void setEmbeddedSourceDtos(Set<SourceDto> embeddedSourceDtos) {
//        this.embeddedSourceDtos = embeddedSourceDtos;
//    }
//
//    public Set<ScholarInterBaseDto> getEmbeddedScholarInterDtos() {
//        return embeddedScholarInterDtos;
//    }
//
//    public void setEmbeddedScholarInterDtos(Set<ScholarInterBaseDto> embeddedScholarInterDtos) {
//        this.embeddedScholarInterDtos = embeddedScholarInterDtos;
//    }
//
//    public String getTitle() {
//        //return this.textProvidesInterface.getFragmentTitle(getXmlId());
//        return this.title;
//    }
//
//
//    // Only necessary due to manual ordering of virtual edition javascript code
//    public String getExternalId() {
//        //return this.textProvidesInterface.getFragmentExternalId(getXmlId());
//        return this.externalId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        FragmentDto other = (FragmentDto) o;
//        return this.xmlId.equals(other.getXmlId());
//    }
//
//    @Override
//    public int hashCode() {
//        return this.xmlId.hashCode();
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setExternalId(String externalId) {
//        this.externalId = externalId;
//    }
//}
