package pt.ist.socialsoftware.edition.ldod.visual.api.remote;

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
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.VisualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.FragmentDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/visual/editions")
public class VisualRemoteController {
    private static final Logger logger = LoggerFactory.getLogger(VisualRemoteController.class);

    private final VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();

    @GetMapping("/public")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getExpertEditionsAndPublicVirtualEditions() {
        logger.debug("getExpertEditionsAndPublicVirtualEditions");

        List<EditionInterListDto> result = new ArrayList<>(this.visualRequiresInterface.getEditionInterListDto());
        result.addAll(this.visualRequiresInterface.getPublicVirtualEditionInterListDto());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/fragments")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<EditionFragmentsDto> getFragments(@PathVariable String acronym) {
        logger.debug("getFragments acronym:{}", acronym);

        EditionFragmentsDto editionFragments = new EditionFragmentsDto();
        ExpertEditionDto expertEdition = this.visualRequiresInterface.getExpertEditionDto(acronym);
        if (expertEdition != null) {
            editionFragments.setCategories(new ArrayList<>());

            List<FragmentDto> fragments = new ArrayList<>();

            this.visualRequiresInterface.getExpertEditionScholarInterDtoList(acronym).stream().sorted(Comparator.comparing(ScholarInterDto::getTitle))
                    .forEach(inter -> {
                        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getXmlId());
                        writer.write(false);
                        FragmentDto fragment = new FragmentDto(inter, writer.getTranscription());

                        fragments.add(fragment);
                    });

            editionFragments.setFragments(fragments);

            return new ResponseEntity<>(editionFragments, HttpStatus.OK);

        } else {
            VirtualEditionDto virtualEdition = this.visualRequiresInterface.getVirtualEdition(acronym);
            if (virtualEdition != null) {
                editionFragments.setCategories(virtualEdition.getSortedCategorySet());

                List<FragmentDto> fragments = new ArrayList<>();

                virtualEdition.getVirtualEditionInterDtoSet().stream().sorted(Comparator.comparing(VirtualEditionInterDto::getTitle))
                        .forEach(inter -> {
                            PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed().getXmlId());
                            writer.write(false);
                            FragmentDto fragment = new FragmentDto(inter, writer.getTranscription());

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
            @PathVariable String acronym, @PathVariable String interId) {
        logger.debug("getInterIdTFIDFTerms acronym:{}", acronym);

        ScholarInterDto scholarInterDto = this.visualRequiresInterface.getScholarInterByExternalIdOfInter(interId);

        List<Map.Entry<String, Double>> result = this.visualRequiresInterface.getScholarInterTermFrequency(scholarInterDto);

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


        List<InterIdDistancePairDto> interIdDistancePairDtos = this.visualRequiresInterface.getIntersByDistance(externalId, weights);

        if (interIdDistancePairDtos != null) {
            return new ResponseEntity<>(interIdDistancePairDtos.stream()
                    .toArray(size -> new InterIdDistancePairDto[size]), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
