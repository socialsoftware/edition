package pt.ist.socialsoftware.edition.ldod.frontend.recommendation;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.Section;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.validator.VirtualEditionValidator;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recommendation")
public class VirtualInterRecommendationSortingController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualInterRecommendationSortingController.class);

    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();
    private final RecommendationProvidesInterface recommendationProvidesInterface = new RecommendationProvidesInterface();

    /*
     * Sets all the empty boxes to null instead of the empty string ""
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'virtualedition.participant')")
    public String presentEditionWithRecommendation(Model model, @PathVariable String acronym) {
        // logger.debug("presentEditionWithRecommendation");

        VirtualEditionDto virtualEdition = this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);

        if (virtualEdition == null) {
            return "redirect:/error";
        }

        List<VirtualEditionInterDto> recommendedEdition =
                this.recommendationProvidesInterface.generateRecommendationFromVirtualEditionInter(
                        null, this.userProvidesInterface.getAuthenticatedUser(), virtualEdition, new ArrayList<>());

        model.addAttribute("edition", virtualEdition);
        model.addAttribute("taxonomyWeight", 0.0);
        model.addAttribute("heteronymWeight", 0.0);
        model.addAttribute("dateWeight", 0.0);
        model.addAttribute("textWeight", 0.0);


        if (!recommendedEdition.isEmpty()) {
            model.addAttribute("inters", recommendedEdition);
            model.addAttribute("selected", recommendedEdition.get(0).getExternalId());
        }

        return "recommendation/tableOfContents";
    }

    @RequestMapping(value = "/linear", method = RequestMethod.POST, headers = {
            "Content-type=application/json;charset=UTF-8"})
    public String setLinearVirtualEdition(Model model, @RequestBody RecommendVirtualEditionParam params) {
        logger.debug("setLinearVirtualEdition acronym:{}, id:{}, properties:{}", params.getAcronym(), params.getId(),
                params.getProperties().stream()
                        .map(p -> p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1) + " "
                                + p.getWeight())
                        .collect(Collectors.joining(";")));

        VirtualEditionDto virtualEdition = this.virtualProvidesInterface.getVirtualEditionByAcronym(params.getAcronym());

        if (virtualEdition == null) {
            return "redirect:/error";
        }

        if (params.getId() != null && !params.getId().equals("")) {
            VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(params.getId());

            List<VirtualEditionInterDto> recommendedEdition =
                    this.recommendationProvidesInterface.generateRecommendationFromVirtualEditionInter(
                            inter, this.userProvidesInterface.getAuthenticatedUser(), virtualEdition, params.getProperties());

            model.addAttribute("inters", recommendedEdition);
            model.addAttribute("selected", params.getId());
        }
        model.addAttribute("edition", virtualEdition);

        return "recommendation/virtualTable";
    }

    @RequestMapping(value = "/linear/save", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
    public String saveLinearVirtualEdition(Model model, @RequestParam("acronym") String acronym,
                                           @RequestParam(value = "inter[]", required = false) String[] inters) {
        VirtualModule ldod = VirtualModule.getInstance();

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

        return "redirect:/recommendation/restricted/" + virtualEdition.getAcronym();
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
                    VirtualModule.getInstance().getVirtualEditionsUserIsParticipant(this.userProvidesInterface.getAuthenticatedUser(), ldoDSession),
                    this.userProvidesInterface.getAuthenticatedUser());
        }

        VirtualEdition virtualEdition = VirtualModule.getInstance().createVirtualEdition(this.userProvidesInterface.getAuthenticatedUser(),
                VirtualEdition.ACRONYM_PREFIX + acronym, title, new LocalDate(), pub, null);
        VirtualEditionInter virtualInter;
        for (int i = 0; i < inters.length; i++) {
            virtualInter = FenixFramework.getDomainObject(inters[i]);
            virtualEdition.createVirtualEditionInter(virtualInter, i + 1);
        }
        return "redirect:/recommendation/restricted/" + virtualEdition.getExternalId();
    }

}
