package pt.ist.socialsoftware.edition.text.api;


import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.text.api.dto.*;

import pt.ist.socialsoftware.edition.text.domain.*;
import pt.ist.socialsoftware.edition.text.feature.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.text.feature.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.text.feature.generators.PlainTextFragmentWriter;
import pt.ist.socialsoftware.edition.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.text.feature.inout.ExpertEditionTEIExport;
import pt.ist.socialsoftware.edition.text.feature.inout.LoadTEICorpus;
import pt.ist.socialsoftware.edition.text.feature.inout.LoadTEIFragments;
import pt.ist.socialsoftware.edition.text.utils.TextBootstrap;


import javax.jms.Queue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TextProvidesInterface {
    private static final Logger logger = LoggerFactory.getLogger(TextProvidesInterface.class);


    private static Map<String, String> fragmentMap = new HashMap<>();

    @GetMapping("/cleanFragmentMapCache")
    public static void cleanFragmentMapCache() {
        fragmentMap = new HashMap<>();
    }

    private static Map<String, String> scholarInterMap = new HashMap<>();

    @GetMapping("/cleanScholarInterMapCache")
    public static void cleanScholarInterMapCache() {
        scholarInterMap = new HashMap<>();
    }


    @GetMapping("/heteronym/scholarInter/{scholarInterId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public HeteronymDto getScholarInterHeteronym(@PathVariable("scholarInterId") String scholarInterId) {
        logger.debug("getScholarInterHeteronym: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getHeteronym()).map(HeteronymDto::new).orElse(null);
    }

    @GetMapping("/heteronym/xmlId/{scholarInterId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getHeteronymXmlId(@PathVariable("scholarInterId") String scholarInterId) {
        logger.debug("getHeteronymXmlId: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getHeteronym().getXmlId()).orElse(null);
    }

    @GetMapping("/heteronyms")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<HeteronymDto> getHeteronymDtoSet() {
        logger.debug("getHeteronymDtoSet");
        return TextModule.getInstance().getHeteronymsSet().stream().map(HeteronymDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/sortedHeteronyms")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<HeteronymDto> getSortedHeteronymList() {
        logger.debug("getSortedHeteronymList");
        return TextModule.getInstance().getSortedHeteronyms().stream()
                .map(HeteronymDto::new).collect(Collectors.toList());
    }

    // Due to Visual Module
    public String getScholarInterExternalId(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getExternalId()).orElse(null);
    }

    @GetMapping("/scholarInter/{scholarInterId}/title")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getScholarInterTitle(@PathVariable(name = "scholarInterId") String scholarInterId) {
        logger.debug("getScholarInterTitle: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getTitle()).orElse(null);
    }

    @GetMapping("/scholarInter/{scholarInterId}/date")
    @Atomic(mode = Atomic.TxMode.READ)
    public LdoDDateDto getScholarInterDate(@PathVariable(name = "scholarInterId") String scholarInterId) {
        logger.debug("getScholarInterDate: " + scholarInterId);
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

    @GetMapping("/scholarInter/{xmlId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getScholarInter(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("getScholarInter: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(ScholarInterDto::new).orElse(null);
    }

    @GetMapping("/scholarInter/{xmlId}/expertEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public ExpertEditionDto getScholarInterExpertEdition(@PathVariable("xmlId") String xmlId) {
        logger.debug("getScholarInterExpertEdition: " + xmlId);
        return getExpertEditionByExpertEditionInterId(xmlId).map(ExpertEditionDto::new).orElse(null);
    }

    @GetMapping("/scholarInter/{xmlId}/isExpert")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isExpertInter(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("isExpertInter: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).orElseThrow(LdoDException::new).isExpertInter();
    }

    @GetMapping("/scholarInter/{xmlId}/source")
    @Atomic(mode = Atomic.TxMode.READ)
    public SourceDto getSourceOfSourceInter(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("getSourceOfSourceInter: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(SourceInter.class::cast).map(SourceInter::getSource).map(SourceDto::new).orElseThrow(LdoDException::new);
    }

    @GetMapping("/scholarInter/{xmlId}/sourceInterType")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getSourceInterType(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("getSourceInterType: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(SourceInter.class::cast).map(sourceInter -> sourceInter.getSource().getType().toString()).orElseThrow(LdoDException::new);
    }

    @GetMapping("/representativeSourceInter/{xmlId}/externalId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getRepresentativeSourceInterExternalId(@PathVariable("xmlId") String fragmentXmlId) {
        logger.debug("getRepresentativeSourceInterExternalId: " + fragmentXmlId);
        return getFragmentByFragmentXmlId(fragmentXmlId).orElse(null).getRepresentativeSourceInter().getExternalId();
    }

    @GetMapping("/scholarInter/{xmlId}/editionAcronym")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getEditionAcronymOfInter(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("getEditionAcronymOfInter: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getEdition().getAcronym()).orElse(null);
    }

    @GetMapping("/expertEdition/{xmlId}/acronym")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getExpertEditionAcronym(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("getExpertEditionAcronym: " + scholarInterId);

        return getExpertEditionByExpertEditionInterId(scholarInterId).map(expertEdition -> expertEdition.getAcronym()).orElse(null);
    }


    @GetMapping("/expertEdition/{xmlId}/editor")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getExpertEditionEditorByScholarInter(@PathVariable("xmlId") String scholarInterId) {
        logger.debug("getExpertEditionEditorByScholarInter: " + scholarInterId);
        return getExpertEditionByExpertEditionInterId(scholarInterId).map(expertEdition -> expertEdition.getEditor()).orElse(null);
    }

    public String getExpertEditionInterVolume(String xmlId) {
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getVolume).orElseThrow(LdoDException::new);
    }

    public String getExpertEditionInterNotes(String xmlId) {
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNotes).orElseThrow(LdoDException::new);
    }

    public int getExpertEditionInterStartPage(String xmlId) {
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getStartPage).orElseThrow(LdoDException::new);
    }

    public int getExpertEditionInterEndPage(String xmlId) {
        return getScholarInterByXmlId(xmlId).filter(ExpertEditionInter.class::isInstance)
                .map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getEndPage).orElseThrow(LdoDException::new);
    }

    @GetMapping("/expertEdition/{acronym}/scholarInterList")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(@PathVariable(name = "acronym") String acronym) {
        logger.debug("getExpertEditionScholarInterDtoList: " + acronym);
        return getExpertEditionByAcronym(acronym).orElseThrow(LdoDException::new).getSortedInterps().stream().map(ScholarInterDto::new).collect(Collectors.toList());
    }

    @GetMapping("/expertEdition/{acronym}/exists")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean acronymExists(@PathVariable(name = "acronym") String acronym) {
        logger.debug("acronymExists: " + acronym);
        return getExpertEditionByAcronym(acronym).orElse(null) != null;
    }

    @GetMapping("/expertEdition/{acronym}/isExpert")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isExpertEdition(@PathVariable(name = "acronym") String acronym) {
        logger.debug("isExpertEdition: " + acronym);
        return getExpertEditionByAcronym(acronym).isPresent() ? getExpertEditionByAcronym(acronym).get().isExpertEdition() : false;
    }

    @GetMapping("/scholarEdition/{xmlId}/citednumber")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getNumberOfTimesCited(@PathVariable(name = "xmlId") String scholarInterId) {
        logger.debug("getNumberOfTimesCited: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> new Integer(scholarInter.getInfoRangeSet().size())).orElseThrow(LdoDException::new);
    }

    @GetMapping("/scholarEdition/{xmlId}/citednumberPlusretweets")
    @Atomic(mode = Atomic.TxMode.READ)
    public int getNumberOfTimesCitedIncludingRetweets(@PathVariable(name = "xmlId") String scholarInterId) {
        logger.debug("getNumberOfTimesCitedIncludingRetweets: " + scholarInterId);
        return getScholarInterByXmlId(scholarInterId)
                .map(scholarInter -> scholarInter.getInfoRangeSet().stream().map(infoRange -> infoRange.getCitation().getNumberOfRetweets()).count() + 1).orElse(0L).intValue();
    }

    @GetMapping("/twitterScholarInterList")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ScholarInterDto> getScholarInterDtoListTwitterEdition(@RequestParam("beginTime") LocalDateTime editionBeginDateTime) {
        logger.debug("getScholarInterDtoListTwitterEdition: " + editionBeginDateTime);
        return TextModule.getInstance().getRZEdition().getExpertEditionIntersSet().stream()
                .filter(inter -> inter.getNumberOfTwitterCitationsSince(editionBeginDateTime) > 0)
                .sorted((inter1,
                         inter2) -> Math.toIntExact(inter2.getNumberOfTwitterCitationsSince(editionBeginDateTime)
                        - inter1.getNumberOfTwitterCitationsSince(editionBeginDateTime))).map(ScholarInterDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/fragment/xmlId/{xmlId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public FragmentDto getFragmentByXmlId(@PathVariable(name = "xmlId") String xmlId) {
        logger.debug("getFragmentByXmlId: " + xmlId);
        return getFragmentByFragmentXmlId(xmlId).map(FragmentDto::new).orElse(null);
    }

    @GetMapping("/fragment/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public FragmentDto getFragmentByExternalId(@PathVariable(name = "externalId") String externalId) {
        logger.debug("getFragmentByExternalId: " + externalId);

        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Fragment) {
            return new FragmentDto((Fragment) domainObject);
        } else {
            return null;
        }
    }

    @GetMapping("/scholarInter/fragment/{xmlId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public FragmentDto getFragmentOfScholarInterDto(@PathVariable(name = "xmlId") String xmlId) {
        logger.debug("getFragmentOfScholarInterDto: " + xmlId);
        return getFragmentByInterXmlId(xmlId).map(FragmentDto::new).orElse(null);
    }

    @GetMapping("/fragment/{xmlId}/sources")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<SourceDto> getFragmentSourceSet(@PathVariable(name = "xmlId") String xmlId) {
        logger.debug("getFragmentSourceSet: " + xmlId);

        Fragment fragment = getFragmentByFragmentXmlId(xmlId).orElse(null);

        if (fragment == null) {
            return new HashSet<>();
        } else {
            Set<SourceDto> result = new HashSet<>();
            for (Source source : fragment.getSourcesSet()) {
                result.add(new SourceDto(source));
            }
            return result;
        }

        //       return getFragmentByFragmentXmlId(xmlId).orElse(null).getSourcesSet().stream().map(SourceDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/fragment/{xmlId}/scholarInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getScholarInterDtoByFragmentXmlIdAndUrlId(@PathVariable(name = "xmlId") String fragmentXmlId, @RequestParam("urlId") String scholarInterUrlId) {
        logger.debug("getScholarInterDtoByFragmentXmlIdAndUrlId: " + fragmentXmlId + "  " + scholarInterUrlId);

        ScholarInter scholarInter = getFragmentByFragmentXmlId(fragmentXmlId).orElse(null).getScholarInterByUrlId(scholarInterUrlId);
        return scholarInter != null ? new ScholarInterDto(scholarInter) : null;
    }

    @GetMapping("/fragment/{xmlId}/title")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getFragmentTitle(@PathVariable(name = "xmlId") String xmlId) {
        logger.debug("getFragmentTitle: " + xmlId );
        return getFragmentByFragmentXmlId(xmlId).map(fragment -> fragment.getTitle()).orElse(null);
    }

    // Only necessary due to manual ordering of virtual edition javascript code
    public String getFragmentExternalId(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId).map(fragment -> fragment.getExternalId()).orElse(null);
    }

    @GetMapping(value = "/fragment/{xmlId}/TFIDF", params = {"terms"})
    @Atomic(mode = Atomic.TxMode.READ)
    public Map<String, Double> getFragmentTFIDF(@PathVariable(name = "xmlId") String xmlId, @RequestParam(name = "terms") List<String> commonTerms) {
        logger.debug("getFragmentTFIDF: " + xmlId + ", " + commonTerms);
        try {
            return Indexer.getIndexer().getTFIDF(getFragmentByFragmentXmlId(xmlId).get(), commonTerms);
        } catch (IOException | ParseException e) {
            throw new LdoDException("IO or Parse exception when getting tfidf from indexer");
        }
    }

    @GetMapping(value = "/fragment/{xmlId}/TFIDF", params = {"numberOfTerms"})
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getFragmentTFIDF(@PathVariable("xmlId") String xmlId, @RequestParam(name = "numberOfTerms") int numberOfTerms) {
        logger.debug("getFragmentTFIDF: " + xmlId + ", " + numberOfTerms);
        try {
            return Indexer.getIndexer().getTFIDFTerms(getFragmentByFragmentXmlId(xmlId).get(), numberOfTerms);
        } catch (IOException | ParseException e) {
            throw new LdoDException("IO or Parse exception when getting tfidf from indexer");
        }
    }

    @GetMapping("/scholarInter/{xmlId}/next")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getScholarInterNextNumberInter(@PathVariable("xmlId") String xmlId) {
        logger.debug("getScholarInterNextNumberInter: " + xmlId );
        return getScholarInterByXmlId(xmlId).filter(ScholarInter::isExpertInter).map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNextNumberInter).map(ScholarInterDto::new).orElse(null);
    }

    @GetMapping("/scholarInter/{xmlId}/prev")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getScholarInterPrevNumberInter(@PathVariable("xmlId") String xmlId) {
        logger.debug("getScholarInterPrevNumberInter: " + xmlId );
        return getScholarInterByXmlId(xmlId).filter(ScholarInter::isExpertInter).map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNextNumberInter).map(ScholarInterDto::new).orElse(null);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    @GetMapping("/fragments")
    public Set<FragmentDto> getFragmentDtoSet() {
        logger.debug("getFragmentDtoSet: ");
        Set<FragmentDto> result = new HashSet<>();
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            result.add(new FragmentDto(fragment));
        }
        return result;
//        return TextModule.getInstance().getFragmentsSet().stream().map(FragmentDto::new).collect(Collectors.toSet());
    }

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    @Atomic(mode = Atomic.TxMode.READ)
    @GetMapping("/fragmentDtosWithSourceDtos")
    public List<FragmentDto> getFragmentDtosWithSourceDtos() {
        logger.debug("getFragmentDtosWithSourceDtos: ");

        List<FragmentDto> result = new ArrayList<>();
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            FragmentDto fragmentDto = new FragmentDto(fragment);
            result.add(fragmentDto);

            Set<SourceDto> sources = new HashSet<>();
            for (Source source : fragment.getSourcesSet()) {
                SourceDto sourceDto = new SourceDto(source);
                sourceDto.setSourceInters(this.getSourceIntersSet(sourceDto.getXmlId()));
                sources.add(sourceDto);
            }
            fragmentDto.setEmbeddedSourceDtos(sources);
        }

        return result;
    }

//    public Set<FragmentDto> getFragmentDtosWithSourceDtos() {
//        Set<FragmentDto> result = new HashSet<>();
//        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
//            FragmentDto fragmentDto = new FragmentDto(fragment);
//            result.add(fragmentDto);
//
//            Set<SourceDto> sources = new HashSet<>();
//            for (Source source : fragment.getSourcesSet()) {
//                sources.add(new SourceDto(source));
//            }
//            fragmentDto.setEmbeddedSourceDtos(sources);
//        }
//
//
//        return result;
//    }

    @GetMapping("/fragmentDtosWithScholarInterDtos")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<FragmentDto> getFragmentDtosWithScholarInterDtos() {
        logger.debug("getFragmentDtosWithScholarInterDtos: ");

        Set<FragmentDto> result = new HashSet<>();
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            FragmentDto fragmentDto = new FragmentDto(fragment);
            result.add(fragmentDto);

            Set<ScholarInterDto> scholarInterDtos = new HashSet<>();
            for (ScholarInter scholarInter : fragment.getScholarInterSet()) {
                scholarInterDtos.add(new ScholarInterDto(scholarInter));
            }
            fragmentDto.setEmbeddedScholarInterDtos(scholarInterDtos);
        }

        return result;
    }


    @GetMapping("/fragment/{xmlId}/scholarInters")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ScholarInterDto> getScholarInterDto4FragmentXmlId(@PathVariable("xmlId") String xmlId) {
        logger.debug("getScholarInterDto4FragmentXmlId: "+ xmlId);
        return getFragmentByFragmentXmlId(xmlId).map(fragment -> fragment.getScholarInterSet()).orElse(new HashSet<>()).stream().map(ScholarInterDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/scholarInter/search")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ScholarInterDto> searchScholarInterForWords(@RequestParam("words") String words) {
        logger.debug("searchScholarInterForWords: "+ words);

        Indexer indexer = Indexer.getIndexer();
        return indexer.search(words).stream().map(ScholarInterDto::new).collect(Collectors.toList());
    }

    @GetMapping("/expertEdition/acronym/{acronym}")
    @Atomic(mode = Atomic.TxMode.READ)
    public ExpertEditionDto getExpertEditionDto(@PathVariable(name = "acronym") String acronym) {
        logger.debug("getExpertEditionDto: " + acronym);
        return getExpertEditionByAcronym(acronym).map(ExpertEditionDto::new).orElse(null);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    @GetMapping("/sortedExpertEditions")
    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        logger.debug("getSortedExpertEditionsDto: ");
        return TextModule.getInstance().getSortedExpertEdition().stream().map(ExpertEditionDto::new).collect(Collectors.toList());
    }

    public String getExpertEditionEditorByEditionAcronym(String acronym) {
        return getExpertEditionByAcronym(acronym).map(expertEdition -> expertEdition.getEditor()).orElse(null);
    }


    @GetMapping("/expertEditionInters")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ExpertEditionInterListDto> getEditionInterListDto() {
        logger.debug("getEditionInterListDto: ");
        return TextModule.getInstance().getExpertEditionsSet().stream()
                .map(expertEdition -> new ExpertEditionInterListDto(expertEdition)).collect(Collectors.toList());
    }

    @GetMapping("/scholarInter/{xmlId}/transcription")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getScholarInterTranscription(@PathVariable("xmlId") String xmlId) {
        logger.debug("getScholarInterTranscription: " + xmlId);
        ScholarInter inter = getScholarInterByXmlId(xmlId).orElse(null);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(false);
        return writer.getTranscription();
    }

    @GetMapping("/sourceInter/{xmlId}/transcription")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getSourceInterTranscription(@PathVariable("xmlId") String xmlId, @RequestParam("diff") boolean diff, @RequestParam("del") boolean del, @RequestParam("ins") boolean ins,
                                              @RequestParam("subst") boolean subst, @RequestParam("notes") boolean notes) {

        logger.debug("getSourceInterTranscription: " + xmlId);
        ScholarInter inter = getScholarInterByXmlId(xmlId).orElse(null);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(diff, del, ins, subst, notes, false, null);
        return writer.getTranscription();
    }

    @GetMapping("/expertInter/{xmlId}/transcription")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getExpertInterTranscription(@PathVariable("xmlId") String xmlId, @RequestParam("diff") boolean diff) {

        logger.debug("getExpertInterTranscription: " + xmlId);
        ScholarInter inter = getScholarInterByXmlId(xmlId).orElse(null);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
        writer.write(diff);
        return writer.getTranscription();
    }

    @GetMapping("/multipleInterTranscription")
    @Atomic(mode = Atomic.TxMode.READ)
    public Map<String, String> getMultipleInterTranscription(@RequestParam("externalIds") List<String> externalIds, @RequestParam("lineByLine") boolean lineByLine, @RequestParam("showSpaces") boolean showSpaces) {

        logger.debug("getMultipleInterTranscription: " + externalIds);
        List<ScholarInter> inters = new ArrayList<>();

        for (String id : externalIds) {
            ScholarInter inter = FenixFramework.getDomainObject(id);
            if (inter != null) {
                inters.add(inter);
            }
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters.stream().map(ScholarInterDto::new).collect(Collectors.toList()));

        writer.write(lineByLine, showSpaces);

        Map<String, String> result = new LinkedHashMap<>();

        if (!lineByLine) {
            for (ScholarInter inter : inters) {
                result.put(inter.getExternalId(), writer.getTranscription(inter));
            }
        } else {
            result.put("transcription", writer.getTranscriptionLineByLine());
        }

        return result;
    }

    @GetMapping("/multipleInterVariations")
    @Atomic(mode = Atomic.TxMode.READ)
    public Map<String, List<String>> getMultipleInterVariations(@RequestParam("externalIds") List<String> externalIds) {
        logger.debug("getMultipleInterVariations: " + externalIds);
        List<ScholarInter> inters = new ArrayList<>();

        for (String id : externalIds) {
            ScholarInter inter = FenixFramework.getDomainObject(id);
            if (inter != null) {
                inters.add(inter);
            }
        }

        List<AppText> apps = new ArrayList<>();
        inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
        Collections.reverse(apps);

        Map<String, List<String>> variations = new HashMap<>();

        for (ScholarInter scholarInter : inters) {
            List<String> interVariation = new ArrayList<>();
            for (AppText app : apps) {
                HtmlWriter4Variations writer4Variations = new HtmlWriter4Variations(scholarInter);
                interVariation.add(writer4Variations.getAppTranscription(app));

            }
            variations.put(scholarInter.getShortName() + "#" + scholarInter.getTitle(), interVariation);
        }

        return variations;
    }

    @GetMapping("/sourceInter/{xmlId}/facUrls")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> getSourceInterFacUrls(@PathVariable("xmlId") String xmlId) {
        logger.debug("getSourceInterFacUrls: " + xmlId);
        List<Surface> surfaces = getScholarInterByXmlId(xmlId).map(SourceInter.class::cast).map(SourceInter::getSource)
                .map(Source::getFacsimile).map(Facsimile::getSurfaces).orElse(null);
        return surfaces != null ? surfaces.stream().map(Surface::getGraphic).collect(Collectors.toList()) : new ArrayList<>();
    }

    @GetMapping("/scholarInter/{xmlId}/sortedAnnexNotes")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<AnnexNoteDto> getScholarInterSortedAnnexNotes(@PathVariable("xmlId") String xmlId) {
        logger.debug("getScholarInterSortedAnnexNotes: " + xmlId);
        List<AnnexNote> notes = getScholarInterByXmlId(xmlId).map(ScholarInter::getSortedAnnexNote).orElse(new ArrayList<>());
        return notes.stream().map(AnnexNoteDto::new).collect(Collectors.toList());
    }


    @GetMapping("/scholarInter/{xmlId}/termFrequency")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<Map.Entry<String, Double>> getScholarInterTermFrequency(String xmlId) {
        logger.debug("getScholarInterTermFrequency: " + xmlId);

        ScholarInter scholarInter = getScholarInterByXmlId(xmlId).orElseThrow(LdoDException::new);

        try {
            return Indexer.getIndexer().getTermFrequency(scholarInter).entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());
        } catch (IOException | ParseException e) {
            new LdoDException("Indexer IOException or ParseException");
        }

        return null;
    }

    @GetMapping("/scholarinter/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getScholarInterbyExternalId(@PathVariable(name = "externalId") String interId) {
        logger.debug("getScholarInterbyExternalId: " + interId);

        DomainObject domainObject = FenixFramework.getDomainObject(interId);

        if (domainObject instanceof ScholarInter) {
            return new ScholarInterDto((ScholarInter) domainObject);
        }

        return null;
    }


    @GetMapping("/fragment/{xmlId}/scholarInters4Expert")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ScholarInterDto> getFragmentScholarInterDtoSetForExpertEdtion(@PathVariable("xmlId") String fragmentXmlId, @RequestParam("acronym") String acronym) {
        logger.debug("getFragmentScholarInterDtoSetForExpertEdtion: " + fragmentXmlId);

        return getFragmentByFragmentXmlId(fragmentXmlId).map(fragment -> fragment.getScholarInterSet()).orElse(new HashSet<>()).stream()
                .filter(scholarInter -> scholarInter.getEdition().getAcronym().equals(acronym)).map(ScholarInterDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/expertEdition/{acronym}/firstInterpretation")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getExpertEditionFirstInterpretation(@PathVariable("acronym") String acronym) {
        logger.debug("getExpertEditionFirstInterpretation: " + acronym);
        return getExpertEditionByAcronym(acronym).map(ExpertEdition::getFirstInterpretation).map(ScholarInterDto::new).orElse(null);
    }

    @GetMapping("/fragment/{xmlId}/{urlId}/scholarInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public ScholarInterDto getFragmentScholarInterByUrlId(@PathVariable("xmlId") String fragmentXmlId, @PathVariable("urlId") String urlId) {
        logger.debug("getFragmentScholarInterByUrlId: " + fragmentXmlId);
        return getFragmentByFragmentXmlId(fragmentXmlId).map(fragment -> fragment.getScholarInterByUrlId(urlId)).map(ScholarInterDto::new).orElse(null);
    }

    @GetMapping("/fragment/{xmlId}/expertEditionSortedInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ScholarInterDto> getExpertEditionSortedInter4Frag(@RequestParam("acronym") String acronym, @PathVariable("xmlId") String fragmentXmlId) {
        logger.debug("getExpertEditionSortedInter4Frag: " + fragmentXmlId + ", " + acronym);
        Fragment fragment = getFragmentByFragmentXmlId(fragmentXmlId).orElse(null);
        return getExpertEditionByAcronym(acronym)
                .map(expertEdition -> expertEdition.getSortedInter4Frag(fragment).stream()
                        .map(ScholarInterDto::new).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }


    @GetMapping("/fragment/{xmlId}/sortedSourceInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<ScholarInterDto> getFragmentSortedSourceInter(@PathVariable("xmlId") String xmlId) {
        logger.debug("getFragmentSortedSourceInter: " + xmlId);
        return getFragmentByFragmentXmlId(xmlId)
                .map(fragment -> fragment.getSortedSourceInter().stream()
                        .map(ScholarInterDto::new)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }


    @PostMapping("/loadTEICorpus")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void LoadTEICorpus(@RequestBody byte[] bytes) {
        logger.debug("LoadTEICorpus: ");
        InputStream file = new ByteArrayInputStream(bytes);
        new LoadTEICorpus().loadTEICorpus(file);
        logger.debug("yo");
    }

    @PostMapping("/loadFragmentsAtOnce")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public String LoadFragmentsAtOnce(@RequestBody byte[] bytes) {
        logger.debug("LoadFragmentsAtOnce: ");

        InputStream file = new ByteArrayInputStream(bytes);
        return new LoadTEIFragments().loadFragmentsAtOnce(file);
    }

    @PostMapping("/loadFragmentsStepByStep")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public String LoadTEIFragmentsStepByStep(@RequestBody byte[] bytes) {
        logger.debug("LoadTEIFragmentsStepByStep: ");
        InputStream file = new ByteArrayInputStream(bytes);
        return new LoadTEIFragments().loadFragmentsStepByStep(file);
    }

    @GetMapping(value = "/writeFromPlainHtmlWriter4OneInter/", params = {"xmlId", "highlightDiff"})
    @Atomic(mode = Atomic.TxMode.READ)
    public String getWriteFromPlainHtmlWriter4OneInter(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "highlightDiff") boolean highlightDiff) {
        logger.debug("getWriteFromPlainHtmlWriter4OneInter: " + xmlId);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(xmlId);
        writer.write(highlightDiff);
        return writer.getTranscription();
    }

    @GetMapping("/appTextWithVariations")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<String> putAppTextWithVariations(@RequestParam(name = "externalId") String externalId, @RequestParam(name = "scholarInters") List<String> scholarInters) {
        logger.debug("putAppTextWithVariations: " + externalId + ", " + scholarInters);
        List<AppText> apps = new ArrayList<>();
        Fragment fragment = FenixFramework.getDomainObject(externalId);
        fragment.getTextPortion().putAppTextWithVariations(apps, scholarInters.stream()
                        .map(xmlId -> TextModule.getInstance().getScholarInterByXmlId(xmlId))
                        .collect(Collectors.toList()));
        Collections.reverse(apps);
        return apps.stream().map(appText -> appText.getExternalId()).collect(Collectors.toList());

    }

    @GetMapping(value = "/writeFromPlainHtmlWriter4OneInter/", params = {"xmlId", "displayDiff", "displayDel", "highlightIns", "highlightSubst", "showNotes", "showFacs", "pbTextId"})
    @Atomic(mode = Atomic.TxMode.READ)
    public String getWriteFromPlainHtmlWriter4OneInter(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "displayDiff") boolean displayDiff, @RequestParam(name = "displayDel") boolean displayDel, @RequestParam(name = "highlightIns") boolean highlightIns, @RequestParam(name = "highlightSubst") boolean highlightSubst, @RequestParam(name = "showNotes") boolean showNotes, @RequestParam(name = "showFacs") boolean showFacs, @RequestParam(name = "pbTextId") String pbTextId) {
        logger.debug("getWriteFromPlainHtmlWriter4OneInter: " + xmlId);
        PbText pbText = null;
        if (pbTextId != null && !pbTextId.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextId);
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(xmlId);
        writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbText);
        return writer.getTranscription();
    }

    @DeleteMapping("/fragment/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeFragmentByExternalId(@PathVariable(name = "externalId") String externalId) {
        logger.debug("removeFragmentByExternalId: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof Fragment) {
            fragmentMap.remove(((Fragment) domainObject).getXmlId());
            ((Fragment) domainObject).remove();
        }
    }

    @GetMapping("/exportExpertEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public String exportExpertEditionTEI(@RequestParam(name = "query") String query) {
        logger.debug("exportExpertEditionTEI: " + query);

        Map<Fragment, Set<ScholarInter>> searchResult = new HashMap<>();

        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
            if (frag.getTitle().contains(query)) {
                Set<ScholarInter> inters = new HashSet<>();
                for (ScholarInter inter : frag.getScholarInterSet()) {
                    inters.add(inter);
                }
                searchResult.put(frag, inters);
            }
        }

        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(searchResult);
        return teiGenerator.getXMLResult();
    }

    @GetMapping("/exportAllExpertEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public String exportAllExpertEditionTEI() {
        logger.debug("exportAllExpertEditionTEI: ");

        Map<Fragment, Set<ScholarInter>> searchResult = new HashMap<>();
        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
            Set<ScholarInter> inters = new HashSet<>();

            for (ScholarInter inter : frag.getScholarInterSet()) {
                inters.add(inter);
            }
            searchResult.put(frag, inters);
        }

        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(searchResult);
        return teiGenerator.getXMLResult();
    }

    @GetMapping("/exportRandomExpertEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public String exportRandomExpertEditionTEI() {
        logger.debug("exportRandomExpertEditionTEI: ");

        Map<Fragment, Set<ScholarInter>> searchResult = new HashMap<>();
        List<Fragment> fragments = new ArrayList<>(TextModule.getInstance().getFragmentsSet());

        List<String> fragsRandom = new ArrayList<>();

        int size = fragments.size();

        int fragPos = 0;
        Fragment frag = null;

        for (int i = 0; i < 3; i++) {
            fragPos = (int) (Math.random() * size);
            frag = fragments.get(fragPos);

            fragsRandom.add("<a href=\"/fragments/fragment/" + frag.getExternalId() + "\">" + frag.getTitle() + "</a>");

            Set<ScholarInter> inters = new HashSet<>();
            for (ScholarInter inter : frag.getScholarInterSet()) {
                //TODO: fragments have source inters. Should they be ingnored or should the code be rewritten for shcolar inters?
                inters.add(inter);
            }
            searchResult.put(frag, inters);
        }

        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(searchResult);
        return teiGenerator.getXMLResult();
    }

    @GetMapping("/citations")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<CitationDto> getCitationSet() {
        logger.debug("getCitationSet: ");
        if (TextModule.getInstance() == null) {
            return new HashSet<>();
        }

        return TextModule.getInstance()
                .getFragmentsSet().stream()
                .flatMap(f -> f.getCitationSet().stream()).map(CitationDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/citations/{id}")
    @Atomic(mode = Atomic.TxMode.READ)
    public CitationDto getCitationById(@PathVariable(name = "id") long id) {
        logger.debug("getCitationById: " + id);

        return getCitationSet().stream().filter(citation -> citation.getId() == id).findFirst().orElse(null);
    }

    @GetMapping("/citationsInfoRanges")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<CitationDto> getCitationsWithInfoRanges() {
        logger.debug("getCitationsWithInfoRanges: ");

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

        return TextModule.getInstance()
                .getFragmentsSet().stream()
                .flatMap(f -> f.getCitationSet().stream()).filter(c -> !c.getInfoRangeSet().isEmpty())
                .sorted((c1, c2) -> LocalDateTime.parse(c2.getDate(), formater)
                        .compareTo(LocalDateTime.parse(c1.getDate(), formater)))
                .map(CitationDto::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/removeAllCitations")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeAllCitations() {
        logger.debug("removeAllCitations: ");
        if (TextModule.getInstance() != null) {
            TextModule.getInstance().getFragmentsSet().stream().flatMap(f -> f.getCitationSet().stream()).filter(citation -> citation != null).forEach(citation -> citation.remove());
        }
    }

    @PostMapping("/createInfoRange")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createInfoRange(@RequestParam(name = "id") long id, @RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "s") String s, @RequestParam(name = "htmlStart") int htmlStart, @RequestParam(name = "s1") String s1, @RequestParam(name = "htmlEnd") int htmlEnd, @RequestParam(name = "infoQuote") String infoQuote, @RequestParam(name = "infoText") String infoText) {
        logger.debug("createInfoRange: " + id);
        Citation citation = TextModule.getInstance()
                .getFragmentsSet().stream()
                .flatMap(f -> f.getCitationSet().stream()).filter(c -> c.getId() == id).findFirst().orElse(null);


        ScholarInter sourceInter = TextModule.getInstance().getScholarInterByXmlId(xmlId);

        new InfoRange(citation, sourceInter, s, htmlStart, s1, htmlEnd, infoQuote, infoText, id);

    }

    @PostMapping("/createCitation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void createCitation(@RequestParam(name = "fragXmlId") String fragXmlId, @RequestParam(name = "sourceLink") String sourceLink, @RequestParam(name = "date") String date, @RequestParam(name = "fragText") String fragText, @RequestParam(name = "id") long id) {
//        Fragment fragment = getFragmentByFragmentXmlId(fragXmlId).orElse(null);
        new Citation().init(fragXmlId, sourceLink, date, fragText, id);
    }

    @GetMapping("/isScholarInter")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isDomainObjectScholarInter(@RequestParam("interId") String interId) {
        logger.debug("isDomainObjectScholarInter: " + interId);
        DomainObject domainObject = FenixFramework.getDomainObject(interId);
        if (domainObject instanceof ScholarInter) {
            return true;
        }
        return false;
    }

    @GetMapping("/isScholarEdition")
    @Atomic(mode = Atomic.TxMode.READ)
    public boolean isDomainObjectScholarEdition(@RequestParam("externalId") String externalId) {
        logger.debug("isDomainObjectScholarEdition: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof ScholarEdition) {
            return true;
        }
        return false;
    }

    @GetMapping("/scholarEdition/acronym/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getScholarEditionAcronymbyExternal(@PathVariable(name = "externalId") String externalId) {
        logger.debug("getScholarEditionAcronymbyExternal: " + externalId);
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof ScholarEdition) {
            return ((ScholarEdition) domainObject).getAcronym();
        }
        return null;
    }

    @GetMapping("/expertEdition/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public ExpertEditionDto getExpertEditionByExternalId(@PathVariable(name = "externalId") String id1) {
        logger.debug("getExpertEditionByExternalId: " + id1);
        DomainObject domainObject = FenixFramework.getDomainObject(id1);
        if (domainObject instanceof ExpertEdition) {
            return new ExpertEditionDto((ExpertEdition) domainObject);
        }
        return null;
    }

    @GetMapping("/heteronym/ext/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public HeteronymDto getHeteronymByExternalId(@PathVariable(name = "externalId") String id2) {
        logger.debug("getHeteronymByExternalId: " + id2);
        DomainObject domainObject = FenixFramework.getDomainObject(id2);
        if (domainObject instanceof Heteronym) {
            return new HeteronymDto((Heteronym) domainObject);
        }
        return null;
    }

    @GetMapping("/appTranscriptionFromHtmlWriter4Variations")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getAppTranscriptionFromHtmlWriter4Variations(@RequestParam(name = "xmlId") String xmlId, @RequestParam(name = "externalAppId") String externalAppId) {
        logger.debug("getAppTranscriptionFromHtmlWriter4Variations: " + xmlId);
        HtmlWriter4Variations variations = new HtmlWriter4Variations(xmlId);
        return variations.getAppTranscription(externalAppId);
    }

    @GetMapping("/writeFromHtmlWriter2CompIntersLineByLine")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getWriteFromHtmlWriter2CompIntersLineByLine(@RequestParam(name = "scholarInters") List<String> scholarInters, @RequestParam("lineByLine") boolean lineByLine, @RequestParam("showSpaces") boolean showSpaces) {
        logger.debug("getWriteFromHtmlWriter2CompIntersLineByLine: " + scholarInters);
        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(scholarInters, false);
        writer.write(lineByLine, showSpaces);
        return writer.getTranscriptionLineByLine();
    }

    @GetMapping("/transcriptionFromHtmlWriter2CompInters")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getTranscriptionFromHtmlWriter2CompInters(@RequestParam(name = "scholarInters") List<String> scholarInters, @RequestParam("inter") String inter, @RequestParam("lineByLine") boolean lineByLine, @RequestParam("showSpaces") boolean showSpaces) {
        logger.debug("getTranscriptionFromHtmlWriter2CompInters" + scholarInters);
        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(scholarInters, false);
        writer.write(lineByLine, showSpaces);
        return writer.getTranscription(inter);
    }

    @GetMapping("/surface")
    @Atomic(mode = Atomic.TxMode.READ)
    public SurfaceDto getSurfaceFromPbTextId(@RequestParam(name = "pbTextId") String pbTextId, @RequestParam(name = "interID") String interID) {
        logger.debug("getSurfaceFromPbTextId" + pbTextId);
        SourceInter inter = FenixFramework.getDomainObject(interID);
        SurfaceDto surface = null;
        PbText pbText = null;
        if (pbTextId != null && !pbTextId.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextId);
        }

        if (pbText == null) {
            surface = new SurfaceDto(inter.getSource().getFacsimile().getFirstSurface());
        } else {
            surface = new SurfaceDto(pbText.getSurface());
        }
        return surface;
    }

    @GetMapping("/surface/prev")
    @Atomic(mode = Atomic.TxMode.READ)
    public SurfaceDto getPrevSurfaceFromPbTextId(@RequestParam(name = "pbTextId") String pbTextId, @RequestParam(name = "interID") String interID) {
        logger.debug("getPrevSurfaceFromPbTextId" + pbTextId);
        SourceInter inter = FenixFramework.getDomainObject(interID);

        PbText pbText = null;
        if (pbTextId != null && !pbTextId.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextId);
            return new SurfaceDto(inter.getPrevSurface(pbText));
        }
        return null;

    }

    @GetMapping("/surface/next")
    @Atomic(mode = Atomic.TxMode.READ)
    public SurfaceDto getNextSurfaceFromPbTextId(@RequestParam(name = "pbTextId") String pbTextId, @RequestParam(name = "interID") String interID) {
        logger.debug("getNextSurfaceFromPbTextId" + pbTextId);
        SourceInter inter = FenixFramework.getDomainObject(interID);

        PbText pbText = null;
        if (pbTextId != null && !pbTextId.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextId);
            return new SurfaceDto(inter.getNextSurface(pbText));
        }
        return null;

    }

    @GetMapping("/prevPb/prev/externalId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getPrevPbTextExternalId(@RequestParam(name = "pbTextId") String pbTextId, @RequestParam(name = "interID") String interID) {
        logger.debug("getPrevPbTextExternalId" + pbTextId);
        SourceInter inter = FenixFramework.getDomainObject(interID);

        PbText pbText = null;
        if (pbTextId != null && !pbTextId.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextId);
        }

        if (inter.getPrevPbText(pbText) != null) {
            return  inter.getPrevPbText(pbText).getExternalId();
        }
        return null;
    }

    @GetMapping("/prevPb/next/externalId")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getNextPbTextExternalId(@RequestParam(name = "pbTextId") String pbTextId, @RequestParam(name = "interID") String interID) {
        logger.debug("getNextPbTextExternalId" + pbTextId);
        SourceInter inter = FenixFramework.getDomainObject(interID);

        PbText pbText = null;
        if (pbTextId != null && !pbTextId.equals("")) {
            pbText = FenixFramework.getDomainObject(pbTextId);
        }

        if (inter.getNextPbText(pbText) != null) {
            return  inter.getNextPbText(pbText).getExternalId();
        }
        return null;
    }

    @GetMapping("/initializeTextModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public boolean initializeTextModule() {
        logger.debug("initializeTextModule");
        return TextBootstrap.initializeTextModule();
    }

    @GetMapping("/clearTermsTFIDFCache")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void clearTermsTFIDFCache() {
        logger.debug("clearTermsTFIDFCache");
        Indexer.clearTermsTFIDFCache();
    }

    @GetMapping("/cleanLucene")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void cleanLucene() {
        logger.debug("cleanLucene");
        Indexer indexer = Indexer.getIndexer();
        indexer.cleanLucene();
    }

    @GetMapping("/writeFromPlainTextFragmentWriter")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getWriteFromPlainTextFragmentWriter(@RequestParam(name = "xmlId") String xmlId) {
        logger.debug("getWriteFromPlainTextFragmentWriter " + xmlId);
        PlainTextFragmentWriter writer = new PlainTextFragmentWriter(TextModule.getInstance().getScholarInterByXmlId(xmlId));
        writer.write();
        return writer.getTranscription();
    }

    @GetMapping("/fragment/citations/{xmlId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<CitationDto> getFragmentCitationSet(@PathVariable(name = "xmlId") String xmlId) {
        logger.debug("getFragmentCitationSet: " + xmlId);
        return TextModule.getInstance().getFragmentByXmlId(xmlId).getCitationSet().stream().map(CitationDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/citation/{id}/infoRange")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<InfoRangeDto> getInfoRangeDtoSetFromCitation(@PathVariable(name = "id") long id) {
        logger.debug("getInfoRangeDtoSetFromCitation " + id);
        Citation citation = TextModule.getInstance()
                .getFragmentsSet().stream()
                .flatMap(f -> f.getCitationSet().stream()).filter(c -> c.getId() == id).findFirst().orElse(null);

        if (citation != null) {
            return citation.getInfoRangeSet().stream().map(InfoRangeDto::new).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }



    @GetMapping("/addDocument")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void addDocumentToIndexer(@RequestParam("xmlId") String xmlId) throws IOException {
        logger.debug("addDocumentToIndexer " + xmlId);
        ScholarInterDto interDto = getScholarInter(xmlId);
        Indexer indexer = Indexer.getIndexer();
        indexer.addDocument(interDto);
    }

    @GetMapping("/simpleText/{externalId}")
    @Atomic(mode = Atomic.TxMode.READ)
    public SimpleTextDto getSimpleTextFromExternalId(@PathVariable("externalId") String startTextId) {
        logger.debug("getSimpleTextFromExternalId " + startTextId);
        DomainObject domainObject = FenixFramework.getDomainObject(startTextId);

        if (domainObject instanceof SimpleText) {
            return new SimpleTextDto((SimpleText) domainObject);
        }
        return null;
    }


    @GetMapping("/source/{xmlId}/interSet")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ScholarInterDto> getSourceIntersSet(@PathVariable("xmlId") String xmlId) {
        logger.debug("getSourceIntersSet " + xmlId);
        Set<SourceInter> sourceInters = getSourceByXmlId(xmlId).map(Source::getSourceIntersSet).orElse(new HashSet<>());
        return sourceInters.stream().map(ScholarInterDto::new).collect(Collectors.toSet());
    }

    @GetMapping("/source/{xmlId}/heteronym")
    @Atomic(mode = Atomic.TxMode.READ)
    public HeteronymDto getHeteronym(@PathVariable("xmlId") String xmlId) {
        logger.debug("getHeteronym " + xmlId);
        return getSourceByXmlId(xmlId)
                .map(Source::getHeteronym)
                .map(HeteronymDto::new)
                .orElse(null);
    }

    @GetMapping("/source/{xmlId}/surfaces")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<SurfaceDto> getSurfaces(@PathVariable("xmlId") String xmlId) {
        logger.debug("getSurfaces " + xmlId);
        List<Surface> surfaces = getSourceByXmlId(xmlId)
                .map(Source_Base::getFacsimile)
                .map(Facsimile::getSurfaces).orElse(new ArrayList<>());
        return surfaces.stream().map(SurfaceDto::new).collect(Collectors.toList());
    }

    @GetMapping("/source/{xmlId}/LdoDDateDto")
    @Atomic(mode = Atomic.TxMode.READ)
    public LdoDDateDto getLdoDDateDto(@PathVariable("xmlId") String xmlId) {
        logger.debug("getLdoDDateDto " + xmlId);
        return getSourceByXmlId(xmlId)
                .map(Source_Base::getLdoDDate)
                .map(LdoDDateDto::new)
                .orElse(null);
    }

    @GetMapping("/source/{xmlId}/LdoDDate")
    @Atomic(mode = Atomic.TxMode.READ)
    public LdoDDateDto getLdoDDate(@PathVariable("xmlId") String xmlId) {
        logger.debug("getLdoDDate " + xmlId);
        return getSourceByXmlId(xmlId)
                .map(source -> source.getLdoDDate() != null ? new LdoDDateDto(source.getLdoDDate()) : null)
                .orElse(null);
    }

    @GetMapping("/source/{xmlId}/typeNotes")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ManuscriptNote> getTypeNoteSet(@PathVariable("xmlId") String xmlId) {
        logger.debug("getTypeNoteSet " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(ManuscriptNote::new)
                .collect(Collectors.toSet());
    }


    @GetMapping("/source/{xmlId}/formattedTypeNotes")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<AbstractMap.SimpleEntry<String, String>> getFormattedTypeNote(@PathVariable("xmlId") String xmlId) {
        logger.debug("getFormattedTypeNote " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getTypeNoteSet)
                .orElse(new HashSet<>())
                .stream().map(typeNote ->
                        new AbstractMap.SimpleEntry<>((typeNote.getMedium() != null ? typeNote.getMedium().getDesc() : ""), typeNote.getNote()))
                .collect(Collectors.toList());
    }

    @GetMapping("/source/{xmlId}/handNotes")
    @Atomic(mode = Atomic.TxMode.READ)
    public Set<ManuscriptNote> getHandNoteSet(@PathVariable("xmlId") String xmlId) {
        logger.debug("getHandNoteSet " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(ManuscriptNote::new)
                .collect(Collectors.toSet());
    }

    @GetMapping("/source/{xmlId}/formattedHandNotes")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<AbstractMap.SimpleEntry<String, String>> getFormattedHandNote(@PathVariable("xmlId") String xmlId) {
        logger.debug("getFormattedHandNote " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getHandNoteSet)
                .orElse(new HashSet<>())
                .stream().map(handNote ->
                        new AbstractMap.SimpleEntry<>((handNote.getMedium() != null ? handNote.getMedium().getDesc() : ""), handNote.getNote()))
                .collect(Collectors.toList());
    }


    @GetMapping("/source/{xmlId}/sortedDimensions")
    @Atomic(mode = Atomic.TxMode.READ)
    public List<DimensionsDto> getSortedDimensionsDto(@PathVariable("xmlId") String xmlId) {
        logger.debug("getSortedDimensionsDto " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getSortedDimensions)
                .orElse(new ArrayList<>())
                .stream().map(DimensionsDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/source/{xmlId}/formattedDimensions")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getFormattedDimensions(@PathVariable("xmlId") String xmlId) {
        logger.debug("getFormattedDimensions " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getDimensionsSet)
                .orElse(new HashSet<>())
                .stream().map(dimensions -> dimensions.getHeight() + "x" + dimensions.getWidth())
                .collect(Collectors.joining(";"));
    }

    @GetMapping("/source/{xmlId}/settlement")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getSettlement(@PathVariable("xmlId") String xmlId) {
        logger.debug("getSettlement " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getSettlement)
                .orElse(null);
    }

    @GetMapping("/source/{xmlId}/repository")
    @Atomic(mode = Atomic.TxMode.READ)
    public String getRepository(@PathVariable("xmlId") String xmlId) {
        logger.debug("getRepository " + xmlId);
        return getSourceByXmlId(xmlId)
                .filter(ManuscriptSource.class::isInstance)
                .map(ManuscriptSource.class::cast)
                .map(ManuscriptSource::getRepository)
                .orElse(null);
    }

    @PostMapping("/removeModule")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeTextModule() {
        TextModule textModule = TextModule.getInstance();

        if (textModule != null) {
            textModule.remove();
        }
    }

    //For testing purposes
    @PostMapping("/createFragment")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public FragmentDto createFragment(@RequestParam(name = "title") String title, @RequestParam(name = "xmlId") String xmlId) {
        return new FragmentDto(new Fragment(TextModule.getInstance(), title, xmlId));
    }

    @PostMapping("/createExpertInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public ScholarInterDto createExpertInter(@RequestParam(name = "fragXmlId") String fragXmlId, @RequestParam(name = "acronym") String acronym, @RequestParam(name = "xmlId") String xmlId) {
        ExpertEditionInter expertEditionInter = new ExpertEditionInter();
        expertEditionInter.setFragment(TextModule.getInstance().getFragmentByXmlId(fragXmlId));
        expertEditionInter.setExpertEdition(TextModule.getInstance().getExpertEdition(acronym));
        expertEditionInter.setXmlId(xmlId);
        return new ScholarInterDto(expertEditionInter);
    }

    @PostMapping("/createSourceInter")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public ScholarInterDto createSourceInter(@RequestParam(name = "fragXmlId") String fragXmlId, @RequestParam(name = "xmlId") String xmlId) {
        SourceInter sourceInter = new SourceInter();
        sourceInter.setFragment(TextModule.getInstance().getFragmentByXmlId(fragXmlId));
        sourceInter.setXmlId(xmlId);
        return new ScholarInterDto(sourceInter);
    }

    @PostMapping("/scholarInter/{xmlId}/remove")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeScholarInter(@PathVariable("xmlId") String xmlId) {
        getScholarInterByXmlId(xmlId).orElse(null).remove();
    }

    @PostMapping("/removeCitation")
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void removeCitation(@RequestParam(name = "externalId") String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);
        if (domainObject instanceof Citation) {
            ((Citation) domainObject).remove();
        }
    }

    private static Optional<ScholarInter> getScholarInterByXmlId(String xmlId) {
        if (xmlId == null) {
            return Optional.empty();
        }
        String scholarInterId = scholarInterMap.get(xmlId);
        if (scholarInterId == null) {
            scholarInterId = TextModule.getInstance().getFragmentsSet().stream()
                    //   .peek(fragment -> logger.debug("estou ca dentro"))
                    .flatMap(fragment -> fragment.getScholarInterSet().stream())
                    .filter(sci -> sci.getXmlId().equals(xmlId))
                    .map(sci -> sci.getExternalId())
                    .findAny().orElse(null);

            if (scholarInterId != null) {
                scholarInterMap.put(xmlId, scholarInterId);
            }
        }

        return Optional.ofNullable(scholarInterId != null ? FenixFramework.getDomainObject(scholarInterId) : null);
    }

    private Optional<Fragment> getFragmentByInterXmlId(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(ScholarInter::getFragment);
    }


    private static Optional<Fragment> getFragmentByFragmentXmlId(String xmlId) {
        if (xmlId == null) {
            return Optional.empty();
        }

        String fragmentId = fragmentMap.get(xmlId);

        if (fragmentId == null) {
            fragmentId = TextModule.getInstance().getFragmentsSet().stream()
                    .filter(f -> f.getXmlId().equals(xmlId))
                    .map(f -> f.getExternalId())
                    .findAny().orElse(null);

            if (fragmentId != null) {
                fragmentMap.put(xmlId, fragmentId);
            }
        }

        return Optional.ofNullable(fragmentId != null ? FenixFramework.getDomainObject(fragmentId) : null) ;
    }

    private Optional<ExpertEdition> getExpertEditionByAcronym(String acronym) {
        return TextModule.getInstance().getExpertEditionsSet().stream().filter(expertEdition -> expertEdition.getAcronym().equals(acronym))
                .findAny();
    }

    private Optional<ExpertEdition> getExpertEditionByExpertEditionInterId(String expertEditionInterId) {
        return TextModule.getInstance().getExpertEditionsSet().stream().filter(expertEdition -> expertEdition.getFragInterByXmlId(expertEditionInterId) != null)
                .findAny();
    }

    private Optional<Source> getSourceByXmlId(String xmlId) {
        return TextModule.getInstance().getFragmentsSet().stream()
                .flatMap(fragment -> fragment.getSourcesSet().stream())
                .filter(source -> source.getXmlId().equals(xmlId))
                .findAny();
    }
}
