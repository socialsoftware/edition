package pt.ist.socialsoftware.edition.ldod.text.api.remote.visual;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.ldod.dto.EditionTranscriptionsDto;
import pt.ist.socialsoftware.edition.ldod.dto.FragmentViewDto;
import pt.ist.socialsoftware.edition.ldod.dto.TranscriptionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/text/visual/editions")
public class TextProvidesVisualController {
    private static final Logger logger = LoggerFactory.getLogger(TextProvidesVisualController.class);

    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    // SHOULD ALSO INCLUDE return this.axios.post('/recommendation/' + interId + '/intersByDistance',


    @GetMapping("/expert")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getExpertsEditions() {
        logger.debug("getExpertEditions");

        List<EditionInterListDto> result = this.textProvidesInterface.getEditionInterListDto();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/fragments")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<EditionFragmentsDto> getFragments(@PathVariable String acronym) {
        logger.debug("getFragments acronym:{}", acronym);

        EditionFragmentsDto editionFragments = new EditionFragmentsDto();
        ExpertEdition expertEdition = TextModule.getInstance().getExpertEdition(acronym);
        if (expertEdition != null) {
            editionFragments.setCategories(new ArrayList<>());

            List<FragmentViewDto> fragments = new ArrayList<>();

            expertEdition.getExpertEditionIntersSet().stream().sorted(Comparator.comparing(ExpertEditionInter::getTitle))
                    .forEach(inter -> {
                        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
                        writer.write(false);
                        FragmentViewDto fragment = new FragmentViewDto(inter, writer.getTranscription());

                        fragments.add(fragment);
                    });

            editionFragments.setFragments(fragments);

            return new ResponseEntity<>(editionFragments, HttpStatus.OK);
        } else {
            VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
            if (virtualEdition != null) {
                editionFragments.setCategories(virtualEdition.getTaxonomy().getSortedCategories().stream()
                        .map(c -> c.getName()).collect(Collectors.toList()));

                List<FragmentViewDto> fragments = new ArrayList<>();

                virtualEdition.getAllDepthVirtualEditionInters().stream().sorted(Comparator.comparing(VirtualEditionInter::getTitle))
                        .forEach(inter -> {
                            PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed().getXmlId());
                            writer.write(false);
                            FragmentViewDto fragment = new FragmentViewDto(inter, writer.getTranscription());

                            fragments.add(fragment);
                        });

                editionFragments.setFragments(fragments);

                return new ResponseEntity<>(editionFragments, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/interId/{interId}/tfidf")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<List<Map.Entry<String, Double>>> getInterIdTFIDFTerms(
            @PathVariable String acronym, @PathVariable String interId) throws IOException, ParseException {
        logger.debug("getInterIdTFIDFTerms acronym:{}", acronym);

        DomainObject domainObject = FenixFramework.getDomainObject(interId);
        ScholarInter scholarInter;


        if (domainObject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (domainObject instanceof VirtualEditionInter) {
            scholarInter = TextModule.getInstance().getScholarInterByXmlId(((VirtualEditionInter) domainObject).getLastUsed().getXmlId());
        } else if (domainObject instanceof ScholarInter) {
            scholarInter = (ScholarInter) domainObject;
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Map.Entry<String, Double>> result = Indexer.getIndexer()
                .getTermFrequency(scholarInter).entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/transcriptions")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<EditionTranscriptionsDto> getTranscriptions(Model model,
                                                               @PathVariable String acronym) {
        logger.debug("getTranscriptions acronym:{}", acronym);

        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);

        if (virtualEdition == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
            List<TranscriptionDto> transcriptions = new ArrayList<>();

            virtualEdition.getIntersSet().stream().sorted(Comparator.comparing(VirtualEditionInter::getTitle)).forEach(inter -> {
                ScholarInter lastInter = TextModule.getInstance().getScholarInterByXmlId(inter.getLastUsed().getXmlId());
                String title = lastInter.getTitle();
                String text;
                try {
                    text = new String(
                            Files.readAllBytes(Paths.get(intersFilesPath + lastInter.getExternalId() + ".txt")));
                } catch (IOException e) {
                    throw new LdoDException("VirtualEditionController::getTranscriptions IOException");
                }

                transcriptions.add(new TranscriptionDto(title, text));
            });

            return new ResponseEntity<>(new EditionTranscriptionsDto(transcriptions), HttpStatus.OK);
        }
    }


}
