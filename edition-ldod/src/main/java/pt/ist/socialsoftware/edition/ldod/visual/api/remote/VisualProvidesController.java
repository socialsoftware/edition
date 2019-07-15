package pt.ist.socialsoftware.edition.ldod.visual.api.remote;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.VisualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/visual/editions")
public class VisualProvidesController {
    private static final Logger logger = LoggerFactory.getLogger(VisualProvidesController.class);

    private final VisualProvidesInterface visualProvidesInterface = new VisualProvidesInterface();

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

        List<Map.Entry<String, Double>> result = this.visualProvidesInterface.getInterTFIDFTerms(interId);

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{externalId}/intersByDistance")
    @PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
    public @ResponseBody
    ResponseEntity<InterIdDistancePairDto[]> getIntersByDistance(Model model,
                                                                 @PathVariable String externalId, @RequestBody WeightsDto weights) {
        logger.debug("getIntersByDistance externalId: {}, weights: {}", externalId,
                "(" + weights.getHeteronymWeight() + "," + weights.getTextWeight() + "," + weights.getDateWeight() + ","
                        + weights.getTaxonomyWeight() + ")");


        List<InterIdDistancePairDto> interIdDistancePairDtos = this.visualProvidesInterface.getIntersByDistance(externalId, weights);

        if (interIdDistancePairDtos != null) {
            return new ResponseEntity<>(interIdDistancePairDtos.stream()
                    .toArray(size -> new InterIdDistancePairDto[size]), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
