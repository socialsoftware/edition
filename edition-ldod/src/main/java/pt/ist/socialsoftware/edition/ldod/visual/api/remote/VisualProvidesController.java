package pt.ist.socialsoftware.edition.ldod.visual.api.remote;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.ScholarInter;
import pt.ist.socialsoftware.edition.ldod.domain.TextModule;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.visual.api.VisualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/visual/editions")
public class VisualProvidesController {
    private static final Logger logger = LoggerFactory.getLogger(VisualProvidesController.class);

    private final VisualProvidesInterface visualProvidesInterface = new VisualProvidesInterface();


    // SHOULD ALSO INCLUDE return this.axios.post('/recommendation/' + interId + '/intersByDistance',

    @GetMapping("/public")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getExpertEditionsAndPublicVirtualEditions() {
        logger.debug("getExpertEditionsAndPublicVirtualEditions");

        List<EditionInterListDto> result = this.visualProvidesInterface.getExpertEditionsAndPublicVirtualEditions();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/fragments")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<EditionFragmentsDto> getFragments(@PathVariable String acronym) {
        logger.debug("getFragments acronym:{}", acronym);

        EditionFragmentsDto editionFragments = this.visualProvidesInterface.getEditionFragmentsForAcronym(acronym);

        if (editionFragments != null) {
            return new ResponseEntity<>(editionFragments, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

}
