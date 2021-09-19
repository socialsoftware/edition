package pt.ist.socialsoftware.edition.api.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import pt.ist.socialsoftware.edition.api.VisualRequiresInterface;

import pt.ist.socialsoftware.edition.notification.dtos.recommendation.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.WeightsDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ExpertEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ExpertEditionInterListDto;
import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.visual.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.notification.dtos.visual.EditionInterListDto;
import pt.ist.socialsoftware.edition.notification.dtos.visual.Fragment4VisualDto;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/visual/editions")
public class VisualRemoteController {
    private static final Logger logger = LoggerFactory.getLogger(VisualRemoteController.class);

    private final VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();

    @GetMapping("/public")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getExpertEditionsAndPublicVirtualEditions() {
        logger.debug("getExpertEditionsAndPublicVirtualEditions");

        //ADD both
        List<ExpertEditionInterListDto> expertresult = new ArrayList<>(this.visualRequiresInterface.getEditionInterListDto());
        List<EditionInterListDto> result = this.visualRequiresInterface.getPublicVirtualEditionInterListDto().stream()
                .map(virtualEditionInterListDto -> new EditionInterListDto(virtualEditionInterListDto))
                .collect(Collectors.toList());

        result.addAll(this.visualRequiresInterface.getEditionInterListDto().stream()
                .map(expertEditionInterListDto -> new EditionInterListDto(expertEditionInterListDto))
                .collect(Collectors.toList()));


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

            List<Fragment4VisualDto> fragments = new ArrayList<>();

            this.visualRequiresInterface.getExpertEditionScholarInterDtoList(acronym).stream().sorted(Comparator.comparing(ScholarInterDto::getTitle))
                    .forEach(inter -> {
//                        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getXmlId());
//                        writer.write(false);
                        Fragment4VisualDto fragment = new Fragment4VisualDto(inter, this.visualRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(inter.getXmlId(), false));

                        fragments.add(fragment);
                    });

            editionFragments.setFragments(fragments);

            return new ResponseEntity<>(editionFragments, HttpStatus.OK);

        } else {
            VirtualEditionDto virtualEdition = this.visualRequiresInterface.getVirtualEdition(acronym);
            if (virtualEdition != null) {
                editionFragments.setCategories(virtualEdition.getSortedCategorySet());

                List<Fragment4VisualDto> fragments = new ArrayList<>();

                virtualEdition.getSortedVirtualEditionInterDtoList().stream().sorted(Comparator.comparing(VirtualEditionInterDto::getTitle))
                        .forEach(inter -> {
//                            PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed().getXmlId());
//                            writer.write(false);
                            Fragment4VisualDto fragment = new Fragment4VisualDto(inter, this.visualRequiresInterface.getWriteFromPlainHtmlWriter4OneInter(inter.getLastUsed().getXmlId(), false));

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
