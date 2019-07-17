package pt.ist.socialsoftware.edition.ldod.text.api;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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

    // Due to Visual Module
    public String getScholarInterExternalId(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getExternalId()).orElse(null);
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

    public String getExpertInterCompleteNumber(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(ExpertEditionInter.class::cast).orElseThrow(LdoDException::new).getCompleteNumber();
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

    public String getExpertEditionInterVolume(String xmlId){
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getVolume).orElseThrow(LdoDException::new);
    }

    public String getExpertEditionInterNotes(String xmlId){
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNotes).orElseThrow(LdoDException::new);
    }

    public int getExpertEditionInterStartPage(String xmlId){
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getStartPage).orElseThrow(LdoDException::new);
    }

    public int getExpertEditionInterEndPage(String xmlId){
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getEndPage).orElseThrow(LdoDException::new);
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

    public ScholarInterDto getNextScholarInter(String xmlId) {
        return getScholarInterByXmlId(xmlId).map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNextNumberInter).map(ScholarInterDto::new).orElse(null);
    }

    public ScholarInterDto getPrevScholarInter(String xmlId) {
        return getScholarInterByXmlId(xmlId).map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNextNumberInter).map(ScholarInterDto::new).orElse(null);
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

    public ExpertEditionDto getExpertEditionDto(String acronym) {
        return getExpertEditionByAcronym(acronym).map(ExpertEditionDto::new).orElse(null);
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return TextModule.getInstance().getSortedExpertEdition().stream().map(ExpertEditionDto::new).collect(Collectors.toList());
    }

    public String getExpertEditionEditorByEditionAcronym(String acronym) {
        return getExpertEditionByAcronym(acronym).map(expertEdition -> expertEdition.getEditor()).orElse(null);
    }


    public List<EditionInterListDto> getEditionInterListDto() {
        return TextModule.getInstance().getExpertEditionsSet().stream()
                .map(expertEdition -> new EditionInterListDto(expertEdition)).collect(Collectors.toList());
    }

    public String getScholarInterTranscription(String xmlId) {
        ScholarInter inter = getScholarInterByXmlId(xmlId).orElse(null);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(false);
        return writer.getTranscription();
    }

    public String getSourceInterTranscription(String xmlId, boolean diff, boolean del, boolean ins,
                                              boolean subst, boolean notes) {
        ScholarInter inter = getScholarInterByXmlId(xmlId).orElse(null);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(diff, del, ins, subst, notes, false, null);
        return writer.getTranscription();
    }

    public String getExpertInterTranscription(String xmlId, boolean diff) {
        ScholarInter inter = getScholarInterByXmlId(xmlId).orElse(null);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(diff);
        return writer.getTranscription();
    }

    public Map<String,String> getMultipleInterTranscription(List<String> externalIds, boolean lineByLine, boolean showSpaces){
        List<ScholarInter> inters = new ArrayList<>();

        for(String id : externalIds){
            ScholarInter inter = FenixFramework.getDomainObject(id);
            if(inter != null)
                inters.add(inter);
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

        writer.write(lineByLine, showSpaces);

        Map<String, String> result = new LinkedHashMap<>();

        if(!lineByLine)
            for(ScholarInter inter : inters){
                result.put(inter.getExternalId(), writer.getTranscription(inter));
            }
        else
            result.put("transcription", writer.getTranscriptionLineByLine());

        return result;
    }

    public List<String> getSourceInterFacUrls(String xmlId) {
        List<Surface> surfaces = getScholarInterByXmlId(xmlId).map(SourceInter.class::cast).map(SourceInter::getSource)
                .map(Source::getFacsimile).map(Facsimile::getSurfaces).orElse(null);
        return surfaces != null ? surfaces.stream().map(Surface::getGraphic).collect(Collectors.toList()) : new ArrayList<>();
    }

    public List<AnnexNoteDto> getScholarInterSortedAnnexNotes(String xmlId){
        List<AnnexNote> notes = getScholarInterByXmlId(xmlId).map(ScholarInter::getSortedAnnexNote).orElse(new ArrayList<>());
        return notes.stream().map(AnnexNoteDto::new).collect(Collectors.toList());
    }

    public List<Map.Entry<String, Double>> getScholarInterTermFrequency(ScholarInterDto scholarInterDto) {
        ScholarInter scholarInter = getScholarInterByXmlId(scholarInterDto.getXmlId()).orElseThrow(LdoDException::new);

        try {
            return Indexer.getIndexer().getTermFrequency(scholarInter).entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());
        } catch (IOException | ParseException e) {
            new LdoDException("Indexer IOException or ParseException");
        }

        return null;
    }

    public ScholarInterDto getScholarInterbyExternalId(String interId) {
        DomainObject domainObject = FenixFramework.getDomainObject(interId);

        if (domainObject instanceof ScholarInter) {
            return new ScholarInterDto((ScholarInter) domainObject);
        }

        return null;
    }

    public List<InterIdDistancePairDto> getIntersByDistance(ScholarInterDto scholarInterDto, WeightsDto weights) {
        ScholarInter scholarInter = getScholarInterByXmlId(scholarInterDto.getXmlId()).orElse(null);

        if (scholarInter != null && scholarInter instanceof ExpertEditionInter) {
            ExpertEditionInter expertEditionInter = (ExpertEditionInter) scholarInter;
            return expertEditionInter.getExpertEdition()
                    .getIntersByDistance(expertEditionInter, weights);
        }

        return null;
    }

    private Optional<ScholarInter> getScholarInterByXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream()
                .filter(fragment -> fragment.getScholarInterByXmlId(xmlId) != null).map(fragment -> fragment.getScholarInterByXmlId(xmlId)).findAny();
    }

    private Optional<Fragment> getFragmentByInterXmlId(String scholarInterId) {
        return TextModule.getInstance().getFragmentsSet().stream().filter(f -> f.getScholarInterByXmlId(scholarInterId) != null).findAny();
    }

    private Optional<Fragment> getFragmentByFragmentXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream().filter(fragment -> fragment.getXmlId().equals(xmlId)).findAny();
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
