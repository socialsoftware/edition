package pt.ist.socialsoftware.edition.ldod.controller;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.api.user.UserInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.ldod.validator.VirtualEditionValidator;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recommendation")
public class RecommendationController {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    private final UserInterface userInterface = new UserInterface();

    /*
     * Sets all the empty boxes to null instead of the empty string ""
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String presentEditionWithRecommendation(Model model, @PathVariable String externalId) {
        // logger.debug("presentEditionWithRecommendation");

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            // logger.debug("presentEditionWithRecommendation sections: {}",
            // virtualEdition.getSectionsSet().stream().map(s ->
            // s.print(1)).collect(Collectors.joining()));

            RecommendationWeights recommendationWeights = virtualEdition.getRecommendationWeightsForUser(this.userInterface.getAuthenticatedUser());

            recommendationWeights.setWeightsZero();

            // logger.debug("presentEditionWithRecommendation sections: {}",
            // virtualEdition.getSectionsSet().stream().map(s ->
            // s.print(1)).collect(Collectors.joining()));

            model.addAttribute("edition", virtualEdition);
            model.addAttribute("taxonomyWeight", 0.0);
            model.addAttribute("heteronymWeight", 0.0);
            model.addAttribute("dateWeight", 0.0);
            model.addAttribute("textWeight", 0.0);

            if (!virtualEdition.getAllDepthVirtualEditionInters().isEmpty()) {
                VirtualEditionInter inter = virtualEdition.getAllDepthVirtualEditionInters().get(0);

                List<VirtualEditionInter> recommendedEdition = virtualEdition.generateRecommendation(inter,
                        recommendationWeights);

                model.addAttribute("inters", recommendedEdition);
                model.addAttribute("selected", inter.getExternalId());
            }

            return "recommendation/tableOfContents";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{externalId}/intersByDistance")
    @PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
    public @ResponseBody
    ResponseEntity<InterIdDistancePairDto[]> getIntersByDistance(Model model,
                                                                 @PathVariable String externalId, @RequestBody WeightsDto weights) {
        logger.debug("getIntersByDistance externalId: {}, weights: {}", externalId,
                "(" + weights.getHeteronymWeight() + "," + weights.getTextWeight() + "," + weights.getDateWeight() + ","
                        + weights.getTaxonomyWeight() + ")");

        DomainObject object = FenixFramework.getDomainObject(externalId);
        if (object == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<InterIdDistancePairDto> intersByDistance;

        if (object instanceof VirtualEditionInter) {
            VirtualEditionInter virtualEditionInter = (VirtualEditionInter) object;
            intersByDistance = virtualEditionInter.getVirtualEdition()
                    .getIntersByDistance(virtualEditionInter, weights);
        } else if (object instanceof ExpertEditionInter) {
            ExpertEditionInter expertEditionInter = (ExpertEditionInter) object;
            intersByDistance = expertEditionInter.getExpertEdition()
                    .getIntersByDistance(expertEditionInter, weights);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(intersByDistance.stream()
                .toArray(size -> new InterIdDistancePairDto[size]), HttpStatus.OK);
    }

    @RequestMapping(value = "/linear", method = RequestMethod.POST, headers = {
            "Content-type=application/json;charset=UTF-8"})
    public String setLinearVirtualEdition(Model model, @RequestBody RecommendVirtualEditionParam params) {
        logger.debug("setLinearVirtualEdition acronym:{}, id:{}, properties:{}", params.getAcronym(), params.getId(),
                params.getProperties().stream()
                        .map(p -> p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1) + " "
                                + p.getWeight())
                        .collect(Collectors.joining(";")));

        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(params.getAcronym());

        RecommendationWeights recommendationWeights = virtualEdition.getRecommendationWeightsForUser(this.userInterface.getAuthenticatedUser());
        recommendationWeights.setWeights(params.getProperties());

        if (params.getId() != null && !params.getId().equals("")) {
            VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());

            List<VirtualEditionInter> recommendedEdition = virtualEdition.generateRecommendation(inter,
                    recommendationWeights);

            model.addAttribute("inters", recommendedEdition);
            model.addAttribute("selected", params.getId());
        }
        model.addAttribute("edition", virtualEdition);
        model.addAttribute("uiInterface", new UiInterface());

        return "recommendation/virtualTable";
    }

    @RequestMapping(value = "/linear/save", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
    public String saveLinearVirtualEdition(Model model, @RequestParam("acronym") String acronym,
                                           @RequestParam(value = "inter[]", required = false) String[] inters) {
        // logger.debug("saveLinearVirtualEdition");

        LdoD ldod = LdoD.getInstance();

        VirtualEdition virtualEdition = ldod.getVirtualEdition(acronym);
        if (inters != null) {
            Section section = virtualEdition.createSection(Section.DEFAULT);
            VirtualEditionInter VirtualEditionInter;
            int i = 0;
            for (String externalId : inters) {
                VirtualEditionInter = FenixFramework.getDomainObject(externalId);
                section.addVirtualEditionInter(VirtualEditionInter, ++i);
            }
            virtualEdition.clearEmptySections();
        }

        return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
    }

    @RequestMapping(value = "/linear/create", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
    public String createLinearVirtualEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
                                             @RequestParam("acronym") String acronym, @RequestParam("title") String title,
                                             @RequestParam("pub") boolean pub, @RequestParam("inter[]") String[] inters) {

        title = title == null ? "" : title.trim();
        acronym = acronym == null ? "" : acronym.trim();

        VirtualEditionValidator validator = new VirtualEditionValidator(null, acronym, title);
        validator.validate();

        List<String> errors = validator.getErrors();

        if (errors.size() > 0) {
            throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
                    LdoD.getInstance().getVirtualEditionsUserIsParticipant(this.userInterface.getAuthenticatedUser(), ldoDSession),
                    User.getAuthenticatedUser());
        }

        VirtualEdition virtualEdition = LdoD.getInstance().createVirtualEdition(this.userInterface.getAuthenticatedUser(),
                VirtualEdition.ACRONYM_PREFIX + acronym, title, new LocalDate(), pub, null);
        VirtualEditionInter virtualInter;
        for (int i = 0; i < inters.length; i++) {
            virtualInter = FenixFramework.getDomainObject(inters[i]);
            virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
        }
        return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
    }

}
