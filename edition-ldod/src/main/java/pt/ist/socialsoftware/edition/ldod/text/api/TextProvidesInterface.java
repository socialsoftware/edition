package pt.ist.socialsoftware.edition.ldod.text.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TextProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(TextProvidesInterface.class);

    public HeteronymDto getScholarInterHeteronym(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getHeteronym()).map(HeteronymDto::new).orElse(null);
    }

    public String getHeteronymXmlId(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getHeteronym().getXmlId()).orElse(null);
    }

    public Set<HeteronymDto> getHeteronymDtoSet() {
        return TextModule.getInstance().getHeteronymsSet().stream().map(HeteronymDto::new).collect(Collectors.toSet());
    }

    public String getScholarInterTitle(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getTitle()).orElse(null);
    }

    public LdoDDateDto getScholarInterDate(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getLdoDDate()).map(LdoDDateDto::new).orElse(null);
    }

    public String getScholarInterReference(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).getReference();
    }

    public String getScholarInterEditionReference(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).getEdition().getReference();
    }

    public int getScholarInterNumber(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).getNumber();
    }

    public String getScholarInterShortName(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).getShortName();
    }

    public String getScholarInterUrlId(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).getUrlId();
    }

    public ScholarInterDto getScholarInter(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(ScholarInterDto::new).orElse(null);
    }

    public boolean isExpertInter(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).isExpertInter();
    }

    public SourceDto getSourceOfSourceInter(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(SourceInter.class::cast).map(SourceInter::getSource).map(SourceDto::new).orElseThrow(LdoDException::new);
    }

    public String getSourceInterType(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource().getType().toString()).orElseThrow(LdoDException::new);
    }

    public String getRepresentativeSourceInterExternalId(String fragmentXmlId) {
        return getFragmentByFragmentXmlId(fragmentXmlId).orElse(null).getRepresentativeSourceInter().getExternalId();
    }

    public String getEditionAcronymOfInter(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getEdition().getAcronym()).orElse(null);
    }

    public String getExpertEditionAcronym(String scholarInterId) {
        return getExpertEditionByExpertEditionInterId(scholarInterId).map(expertEdition -> expertEdition.getAcronym()).orElse(null);
    }

    public String getExpertEditionEditorByScholarInter(String scholarInterId) {
        return getExpertEditionByExpertEditionInterId(scholarInterId).map(expertEdition -> expertEdition.getEditor()).orElse(null);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return getExpertEditionByAcronym(acronym).orElseThrow(LdoDException::new).getSortedInterps().stream().map(ScholarInterDto::new).collect(Collectors.toList());
    }

    public boolean acronymExists(String acronym) {
        return getExpertEditionByAcronym(acronym).orElse(null) != null;
    }

    public boolean isExpertEdition(String acronym) {
        return getExpertEditionByAcronym(acronym).isPresent() ? getExpertEditionByAcronym(acronym).get().isExpertEdition() : false;
    }

    public int getNumberOfTimesCited(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> new Integer(scholarInter.getInfoRangeSet().size())).orElseThrow(LdoDException::new);
    }

    public int getNumberOfTimesCitedIncludingRetweets(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId)
                .map(scholarInter -> scholarInter.getInfoRangeSet().stream().map(infoRange -> infoRange.getCitation().getNumberOfRetweets()).count() + 1).orElse(0L).intValue();
    }

    public List<ScholarInterDto> getScholarInterDtoListTwitterEdition(LocalDateTime editionBeginDateTime) {
        return TextModule.getInstance().getRZEdition().getExpertEditionIntersSet().stream()
                .filter(inter -> inter.getNumberOfTwitterCitationsSince(editionBeginDateTime) > 0)
                .sorted((inter1,
                         inter2) -> Math.toIntExact(inter2.getNumberOfTwitterCitationsSince(editionBeginDateTime)
                        - inter1.getNumberOfTwitterCitationsSince(editionBeginDateTime))).map(ScholarInterDto::new)
                .collect(Collectors.toList());
    }

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId).map(FragmentDto::new).orElse(null);
    }

    public FragmentDto getFragmentOfScholarInterDto(ScholarInterDto scholarInterDto) {
        return getFragmentByInterXmlId(scholarInterDto.getXmlId()).map(FragmentDto::new).orElse(null);
    }

    public Set<SourceDto> getFragmentSourceSet(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId).orElse(null).getSourcesSet().stream().map(SourceDto::new).collect(Collectors.toSet());
    }

    public ScholarInterDto getScholarInterDtoByFragmentXmlIdAndUrlId(String fragmentXmlId, String scholarInterUrlId) {
        ScholarInter scholarInter = getFragmentByFragmentXmlId(fragmentXmlId).orElse(null).getScholarInterByUrlId(scholarInterUrlId);
        return scholarInter != null ? new ScholarInterDto(scholarInter) : null;
    }

    public String getFragmentTitle(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId).map(fragment -> fragment.getTitle()).orElse(null);
    }

    // Only necessary due to manual ordering of virtual edition javascript code
    public String getFragmentExternalId(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId).map(fragment -> fragment.getExternalId()).orElse(null);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return TextModule.getInstance().getFragmentsSet().stream().map(FragmentDto::new).collect(Collectors.toSet());
    }

    public Set<ScholarInterDto> getScholarInterDto4FragmentXmlId(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId).map(fragment -> fragment.getScholarInterSet()).orElse(new HashSet<>()).stream().map(ScholarInterDto::new).collect(Collectors.toSet());
    }

    public List<ScholarInterDto> searchScholarInterForWords(String words) {
        Indexer indexer = Indexer.getIndexer();
        return indexer.search(words).stream().map(ScholarInterDto::new).collect(Collectors.toList());
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return TextModule.getInstance().getSortedExpertEdition().stream().map(ExpertEditionDto::new).collect(Collectors.toList());
    }

    public String getExpertEditionEditorByEditionAcronym(String acronym) {
        return getExpertEditionByAcronym(acronym).map(expertEdition -> expertEdition.getEditor()).orElse(null);
    }

    private Optional<ScholarInter> getScholarInterByXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId)).findAny();
    }

    private Optional<Fragment> getFragmentByInterXmlId(String scholarInterId) {
        return TextModule.getInstance().getFragmentsSet().stream().filter(f -> f.getScholarInterByXmlId(scholarInterId) != null).findAny();
    }

    private Optional<Fragment> getFragmentByFragmentXmlId(String xlmId) {
        return TextModule.getInstance().getFragmentsSet().stream().filter(fragment -> fragment.getXmlId().equals(xlmId)).findAny();
    }

    private Optional<ExpertEdition> getExpertEditionByAcronym(String acronym) {
        return TextModule.getInstance().getExpertEditionsSet().stream().filter(expertEdition -> expertEdition.getAcronym().equals(acronym))
                .findAny();
    }

    private Optional<ExpertEdition> getExpertEditionByExpertEditionInterId(String expertEditionInterId) {
        return TextModule.getInstance().getExpertEditionsSet().stream().filter(expertEdition -> expertEdition.getFragInterByXmlId(expertEditionInterId) != null)
                .findAny();
    }

}
