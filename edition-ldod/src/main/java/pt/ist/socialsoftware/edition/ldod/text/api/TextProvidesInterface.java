package pt.ist.socialsoftware.edition.ldod.text.api;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainModel;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter4Variations;
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

    private static Map<String, String> fragmentMap = new HashMap<>();

    public static void cleanFragmentMapCache() {
        fragmentMap = new HashMap<>();
    }

    private static Map<String, String> scholarInterMap = new HashMap<>();

    public static void cleanScholarInterMapCache() {
        scholarInterMap = new HashMap<>();
    }

    public HeteronymDto getScholarInterHeteronym(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getHeteronym()).map(HeteronymDto::new).orElse(null);
    }

    public String getHeteronymXmlId(String scholarInterId) {
        return getScholarInterByXmlId(scholarInterId).map(scholarInter -> scholarInter.getHeteronym().getXmlId()).orElse(null);
    }

    public Set<HeteronymDto> getHeteronymDtoSet() {
        return TextModule.getInstance().getHeteronymsSet().stream().map(HeteronymDto::new).collect(Collectors.toSet());
    }

    public List<HeteronymDto> getSortedHeteronymList() {
        return TextModule.getInstance().getSortedHeteronyms().stream()
                .map(HeteronymDto::new).collect(Collectors.toList());
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

    public ExpertEditionDto getScholarInterExpertEdition(String xmlId) {
        return getExpertEditionByExpertEditionInterId(xmlId).map(ExpertEditionDto::new).orElse(null);
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

    public FragmentDto getFragmentByExternalId(String externalId) {
        DomainObject domainObject = FenixFramework.getDomainObject(externalId);

        if (domainObject instanceof Fragment) {
            return new FragmentDto((Fragment) domainObject);
        } else {
            return null;
        }
    }

    public FragmentDto getFragmentOfScholarInterDto(ScholarInterDto scholarInterDto) {
        return getFragmentByInterXmlId(scholarInterDto.getXmlId()).map(FragmentDto::new).orElse(null);
    }

    public Set<SourceDto> getFragmentSourceSet(String xmlId) {
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

    public Map<String, Double> getFragmentTFIDF(String xmlId, List<String> commonTerms) {
        try {
            return Indexer.getIndexer().getTFIDF(getFragmentByFragmentXmlId(xmlId).get(), commonTerms);
        } catch (IOException | ParseException e) {
            throw new LdoDException("IO or Parse exception when getting tfidf from indexer");
        }
    }

    public List<String> getFragmentTFIDF(String xmlId, int numberOfTerms) {
        try {
            return Indexer.getIndexer().getTFIDFTerms(getFragmentByFragmentXmlId(xmlId).get(), numberOfTerms);
        } catch (IOException | ParseException e) {
            throw new LdoDException("IO or Parse exception when getting tfidf from indexer");
        }
    }

    public ScholarInterDto getScholarInterNextNumberInter(String xmlId) {
        return getScholarInterByXmlId(xmlId).map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNextNumberInter).map(ScholarInterDto::new).orElse(null);
    }

    public ScholarInterDto getScholarInterPrevNumberInter(String xmlId) {
        return getScholarInterByXmlId(xmlId).map(ExpertEditionInter.class::cast)
                .map(ExpertEditionInter::getNextNumberInter).map(ScholarInterDto::new).orElse(null);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        Set<FragmentDto> result = new HashSet<>();
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            result.add(new FragmentDto(fragment));
        }
        return result;
//        return TextModule.getInstance().getFragmentsSet().stream().map(FragmentDto::new).collect(Collectors.toSet());
    }


    public Set<FragmentDto> getFragmentDtosWithSourceDtos() {
        Set<FragmentDto> result = new HashSet<>();
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            FragmentDto fragmentDto = new FragmentDto(fragment);
            result.add(fragmentDto);

            Set<SourceDto> sources = new HashSet<>();
            for (Source source : fragment.getSourcesSet()) {
                sources.add(new SourceDto(source));
            }
            fragmentDto.setEmbeddedSourceDtos(sources);
        }

        return result;
    }

    public Set<FragmentDto> getFragmentDtosWithScholarInterDtos() {
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

    public Map<String, String> getMultipleInterTranscription(List<String> externalIds, boolean lineByLine, boolean showSpaces) {
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

    public Map<String, List<String>> getMultipleInterVariations(List<String> externalIds) {
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

    public List<String> getSourceInterFacUrls(String xmlId) {
        List<Surface> surfaces = getScholarInterByXmlId(xmlId).map(SourceInter.class::cast).map(SourceInter::getSource)
                .map(Source::getFacsimile).map(Facsimile::getSurfaces).orElse(null);
        return surfaces != null ? surfaces.stream().map(Surface::getGraphic).collect(Collectors.toList()) : new ArrayList<>();
    }

    public List<AnnexNoteDto> getScholarInterSortedAnnexNotes(String xmlId) {
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

    public Set<ScholarInterDto> getFragmentScholarInterDtoSetForExpertEdtion(String fragmentXmlId, String acronym) {
        return getFragmentByFragmentXmlId(fragmentXmlId).map(fragment -> fragment.getScholarInterSet()).orElse(new HashSet<>()).stream()
                .filter(scholarInter -> scholarInter.getEdition().getAcronym().equals(acronym)).map(ScholarInterDto::new).collect(Collectors.toSet());
    }

    public ScholarInterDto getExpertEditionFirstInterpretation(String acronym) {
        return getExpertEditionByAcronym(acronym).map(ExpertEdition::getFirstInterpretation).map(ScholarInterDto::new).orElse(null);
    }

    public ScholarInterDto getFragmentScholarInterByUrlId(String fragmentXmlId, String urlId) {
        return getFragmentByFragmentXmlId(fragmentXmlId).map(fragment -> fragment.getScholarInterByUrlId(urlId)).map(ScholarInterDto::new).orElse(null);
    }

    public List<ScholarInterDto> getExpertEditionSortedInter4Frag(String acronym, String fragmentXmlId) {
        Fragment fragment = getFragmentByFragmentXmlId(fragmentXmlId).orElse(null);
        return getExpertEditionByAcronym(acronym)
                .map(expertEdition -> expertEdition.getSortedInter4Frag(fragment).stream()
                        .map(ScholarInterDto::new).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    private static Optional<ScholarInter> getScholarInterByXmlId(String xmlId) {
        if (xmlId == null) {
            return Optional.empty();
        }

        String scholarInterId = scholarInterMap.get(xmlId);

        if (scholarInterId == null) {
            scholarInterId = TextModule.getInstance().getFragmentsSet().stream()
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

    public List<ScholarInterDto> getFragmentSortedSourceInter(String xmlId) {
        return getFragmentByFragmentXmlId(xmlId)
                .map(fragment -> fragment.getSortedSourceInter().stream()
                        .map(ScholarInterDto::new)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

}
