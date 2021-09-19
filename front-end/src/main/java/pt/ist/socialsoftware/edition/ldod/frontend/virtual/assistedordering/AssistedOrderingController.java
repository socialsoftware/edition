package pt.ist.socialsoftware.edition.ldod.frontend.virtual.assistedordering;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.validator.VirtualEditionValidator;
import pt.ist.socialsoftware.edition.notification.dtos.recommendation.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.utils.LdoDCreateVirtualEditionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recommendation")
public class AssistedOrderingController {
    private static final Logger logger = LoggerFactory.getLogger(AssistedOrderingController.class);

    private final FEAssistedOrderingRequiresInterface requiresInterface = new FEAssistedOrderingRequiresInterface();

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

        VirtualEditionDto virtualEdition = this.requiresInterface.getVirtualEditionByAcronym(acronym);



        if (virtualEdition == null) {
            return "redirect:/error";
        }
        System.out.println(virtualEdition.getExternalId());
        List<VirtualEditionInterDto> recommendedEdition =
                this.requiresInterface.generateRecommendationFromVirtualEditionInter(
                        null, this.requiresInterface.getAuthenticatedUser(), virtualEdition, new ArrayList<>());

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


        System.out.println(params.getId());

        VirtualEditionDto virtualEdition = this.requiresInterface.getVirtualEditionByAcronym(params.getAcronym());

        if (virtualEdition == null) {
            return "redirect:/error";
        }

        if (params.getId() != null && !params.getId().equals("")) {
            VirtualEditionInterDto inter = this.requiresInterface.getVirtualEditionInterByExternalId(params.getId());

            List<VirtualEditionInterDto> recommendedEdition =
                    this.requiresInterface.generateRecommendationFromVirtualEditionInter(
                            inter, this.requiresInterface.getAuthenticatedUser(), virtualEdition, params.getProperties());

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

        this.requiresInterface.saveVirtualEdition(acronym, inters);

        return "redirect:/recommendation/restricted/" + acronym;
    }

    @RequestMapping(value = "/linear/create", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
    public String createLinearVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                             @RequestParam("acronym") String acronym, @RequestParam("title") String title,
                                             @RequestParam("pub") boolean pub, @RequestParam("inter[]") String[] inters) {

        title = title == null ? "" : title.trim();
        acronym = acronym == null ? "" : acronym.trim();

        VirtualEditionValidator validator = new VirtualEditionValidator(null, acronym, title);
        validator.validate();

        List<String> errors = validator.getErrors();

        if (errors.size() > 0) {
            String username = this.requiresInterface.getAuthenticatedUser();
            throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
                    this.requiresInterface.getVirtualEditionsUserIsParticipant(username),
                    username);
        }

        this.requiresInterface.createVirtualEdition(this.requiresInterface.getAuthenticatedUser(), acronym, title, new LocalDate(), pub, inters);

        return "redirect:/recommendation/restricted/" + acronym;
    }

}
