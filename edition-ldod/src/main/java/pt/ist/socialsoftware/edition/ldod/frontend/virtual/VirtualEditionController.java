package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Member.MemberRole;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.validator.VirtualEditionValidator;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.ldod.utils.exception.*;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.socialaware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.topicmodeling.TopicModeler;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/virtualeditions")
public class VirtualEditionController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEditionController.class);

    private final FEVirtualRequiresInterface FEVirtualRequiresInterface = new FEVirtualRequiresInterface();

    @ModelAttribute("frontendSession")
    public FrontendSession getFrontendSession() {
        return FrontendSession.getFrontendSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession) {

        model.addAttribute("ldod", VirtualModule.getInstance());
        model.addAttribute("expertEditions", TextModule.getInstance().getSortedExpertEdition());
        model.addAttribute("virtualEditions",
                this.FEVirtualRequiresInterface.getPublicVirtualEditionsOrUserIsParticipant(this.FEVirtualRequiresInterface.getAuthenticatedUser()));
        model.addAttribute("user", this.FEVirtualRequiresInterface.getAuthenticatedUser());

        return "virtual/editions";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/create")
    public String createVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                       @RequestParam("acronym") String acronym, @RequestParam("title") String title,
                                       @RequestParam("pub") boolean pub, @RequestParam("use") String editionID) {

        DomainObject usedEdition = null;
        if (!editionID.equals("no")) {
            usedEdition = FenixFramework.getDomainObject(editionID);
        }

        LocalDate date = new LocalDate();

        title = title.trim();
        acronym = acronym.trim();

        VirtualEdition virtualEdition = null;

        VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
        validator.validate();

        List<String> errors = validator.getErrors();

        if (errors.size() > 0) {
            throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
                    new ArrayList<>(this.FEVirtualRequiresInterface.getPublicVirtualEditionsOrUserIsParticipant(this.FEVirtualRequiresInterface.getAuthenticatedUser())),
                    this.FEVirtualRequiresInterface.getAuthenticatedUser());
        }

        try {
            String usedAcronym = usedEdition == null ? null : ((usedEdition instanceof ScholarEdition) ? ((ScholarEdition) usedEdition).getAcronym()
                    : ((VirtualEdition) usedEdition).getAcronym());
            virtualEdition = VirtualModule.getInstance().createVirtualEdition(this.FEVirtualRequiresInterface.getAuthenticatedUser(),
                    VirtualEdition.ACRONYM_PREFIX + acronym, title, date, pub, usedAcronym);

        } catch (LdoDDuplicateAcronymException ex) {
            errors.add("virtualedition.acronym.duplicate");
            throw new LdoDCreateVirtualEditionException(errors, acronym, title, pub,
                    new ArrayList<>(this.FEVirtualRequiresInterface.getPublicVirtualEditionsOrUserIsParticipant(this.FEVirtualRequiresInterface.getAuthenticatedUser())),
                    this.FEVirtualRequiresInterface.getAuthenticatedUser());
        }

        return "redirect:/virtualeditions";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String deleteVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                       @RequestParam("externalId") String externalId) {
        logger.debug("deleteVirtualEdition externalId:{}", externalId);
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {

            String acronym = virtualEdition.getAcronym();

            virtualEdition.remove();

            if (frontendSession.hasSelectedVE(acronym)) {
                frontendSession.removeSelectedVE(acronym);
            }

            return "redirect:/virtualeditions";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/manage/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String manageVirtualEdition(Model model, @PathVariable String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        logger.debug("manageVirtualEdition externalId:{}", externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("user", this.FEVirtualRequiresInterface.getAuthenticatedUser());

            List<String> countriesList = new ArrayList<>();
            countriesList.add("Portugal");
            countriesList.add("Brazil");
            countriesList.add("Spain");
            countriesList.add("United Kingdom");
            countriesList.add("United States");
            countriesList.add("Lebanon");
            countriesList.add("Angola");
            countriesList.add("Mozambique");
            model.addAttribute("countriesList", countriesList);
            return "virtual/manage";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/editForm/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String showEditVirtualEdition(Model model, @PathVariable String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        }
        logger.debug("showEditVirtualEditionn externalId:{}, acronym:{}, title:{}, pub:{}, fraginters.size:{}",
                externalId, virtualEdition.getAcronym(), virtualEdition.getTitle(), virtualEdition.getPub(),
                virtualEdition.getIntersSet().size());
        model.addAttribute("virtualEdition", virtualEdition);
        model.addAttribute("externalId", virtualEdition.getExternalId());
        model.addAttribute("acronym", virtualEdition.getShortAcronym());
        model.addAttribute("title", virtualEdition.getTitle());
        model.addAttribute("date", virtualEdition.getDate().toString("dd-MM-yyyy"));
        model.addAttribute("pub", virtualEdition.getPub());
        model.addAttribute("uiInterface", new UiInterface());
        return "virtual/edition";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String editVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                     @PathVariable String externalId, @RequestParam("acronym") String acronym,
                                     @RequestParam("title") String title, @RequestParam("synopsis") String synopsis,
                                     @RequestParam("pub") boolean pub, @RequestParam("management") boolean management,
                                     @RequestParam("vocabulary") boolean vocabulary, @RequestParam("annotation") boolean annotation,
                                     @RequestParam("mediasource") String mediaSource, @RequestParam("begindate") String beginDate,
                                     @RequestParam("enddate") String endDate, @RequestParam("geolocation") String geoLocation,
                                     @RequestParam("frequency") String frequency) {

        logger.info("mediaSource:{}", mediaSource);
        logger.info("beginDate:{}", beginDate);
        logger.info("endDate:{}", endDate);
        logger.info("geoLocation:{}", geoLocation);
        logger.info("frequency:{}", frequency);

        logger.debug(
                "editVirtualEdition externalId:{}, acronym:{}, title:{}, pub:{}, management:{}, vocabulary:{}, annotation:{}",
                externalId, acronym, title, pub, management, vocabulary, annotation);

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        title = title.trim();
        acronym = acronym.trim();

        VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
        validator.validate();

        List<String> errors = validator.getErrors();

        if (errors.size() > 0) {
            throw new LdoDEditVirtualEditionException(errors, virtualEdition, acronym, title, pub);
        }

        try {
            virtualEdition.edit(VirtualEdition.ACRONYM_PREFIX + acronym, title, synopsis, pub, management, vocabulary,
                    annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
        } catch (LdoDDuplicateAcronymException ex) {
            errors.add("virtualedition.acronym.duplicate");
            throw new LdoDEditVirtualEditionException(errors, virtualEdition, acronym, title, pub);
        }

        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        if (virtualEdition.isSAVE()) {
            awareFactory.searchForAwareAnnotations(virtualEdition);
        }
        // this virtual edition is not SAVE anymore, therefore we have to remove all the
        // aware annotations
        else {
            for (VirtualEditionInter inter : virtualEdition.getAllDepthVirtualEditionInters()) {
                awareFactory.removeAllAwareAnnotationsFromVEInter(inter);
            }
        }

        return "redirect:/virtualeditions/restricted/manage/" + externalId;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/reorder/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String reorderVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                        @PathVariable String externalId, @RequestParam("fraginters") String fraginters) {
        logger.debug("reorderVirtualEdition externalId:{}, fraginters:{}", externalId, fraginters);

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        List<String> fragIntersXmlIds = new ArrayList<>();
        for (String interExternalId : Arrays.stream(fraginters.trim().split(";")).map(item -> item.trim())
                .filter(item -> !item.equals("")).collect(Collectors.toList())) {
            DomainObject domainObject = FenixFramework.getDomainObject(interExternalId);
            if (domainObject instanceof ScholarInter) {
                fragIntersXmlIds.add(((ScholarInter) domainObject).getXmlId());
            } else {
                fragIntersXmlIds.add(((VirtualEditionInter) domainObject).getXmlId());
            }
        }

        virtualEdition.updateVirtualEditionInters(fragIntersXmlIds);

        return "redirect:/virtualeditions";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/toggleselection")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
    public String toggleSelectedVirtualEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                               @RequestParam("externalId") String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);

        if (virtualEdition == null) {
            return "redirect:/error";
        }

        String user = this.FEVirtualRequiresInterface.getAuthenticatedUser();

        frontendSession.toggleSelectedVirtualEdition(user, virtualEdition.getAcronym());

        return "redirect:/virtualeditions";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/participants")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String showParticipants(Model model, @PathVariable String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            List<String> errors = (List<String>) model.asMap().get("errors");
            String username = (String) model.asMap().get("username");
            model.addAttribute("errors", errors);
            model.addAttribute("username", username);
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("userInterface", this.FEVirtualRequiresInterface);
            return "virtual/participants";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/submit")
    public String submitParticipation(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                      @PathVariable String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        String user = this.FEVirtualRequiresInterface.getAuthenticatedUser();

        if (virtualEdition == null || user == null) {
            return "redirect:/error";
        } else {
            virtualEdition.addMember(user, MemberRole.MEMBER, false);
            return "redirect:/virtualeditions";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/cancel")
    public String cancelParticipationSubmission(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                                @PathVariable String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        String user = this.FEVirtualRequiresInterface.getAuthenticatedUser();

        if (virtualEdition == null || user == null) {
            return "redirect:/error";
        } else {
            virtualEdition.cancelParticipationSubmission(user);
            return "redirect:/virtualeditions";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/approve")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String approveParticipant(RedirectAttributes redirectAttributes,
                                     @PathVariable("externalId") String externalId, @RequestParam("username") String username) {

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        UserDto userDto = this.FEVirtualRequiresInterface.getUser(username);
        if (userDto == null) {
            List<String> errors = new ArrayList<>();
            errors.add("user.unknown");
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("username", username);
            return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
        } else {
            virtualEdition.addApprove(userDto.getUsername());
            return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/add")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String addParticipant(RedirectAttributes redirectAttributes, @PathVariable("externalId") String externalId,
                                 @RequestParam("username") String username) {

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        UserDto userDto = this.FEVirtualRequiresInterface.getUser(username);
        if (userDto == null) {
            List<String> errors = new ArrayList<>();
            errors.add("user.unknown");
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("username", username);
            return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
        } else {
            virtualEdition.addMember(userDto.getUsername(), MemberRole.MEMBER, true);
            return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/role")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String switchRole(Model model, @PathVariable("externalId") String externalId,
                             @RequestParam("username") String username) {

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        if (!virtualEdition.canSwitchRole(this.FEVirtualRequiresInterface.getAuthenticatedUser(), username)) {
            throw new LdoDExceptionNonAuthorized();
        }

        virtualEdition.switchRole(username);

        return "redirect:/virtualeditions/restricted/" + externalId + "/participants";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/remove")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String removeParticipant(RedirectAttributes redirectAttributes,
                                    @ModelAttribute("frontendSession") FrontendSession frontendSession, @PathVariable("externalId") String externalId,
                                    @RequestParam("user") String user) {
        logger.debug("removeParticipant user:{}", user);

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);

        if (virtualEdition == null || this.FEVirtualRequiresInterface.getUser(user) == null) {
            return "redirect:/error";
        }

        if (!virtualEdition.canRemoveMember(this.FEVirtualRequiresInterface.getAuthenticatedUser(), user)) {
            throw new LdoDExceptionNonAuthorized();
        }

        String admin = null;
        if (virtualEdition.getAdminSet().size() == 1) {
            admin = virtualEdition.getAdminSet().iterator().next();
        }

        if (this.FEVirtualRequiresInterface.getUser(admin) != null && admin.equals(user)) {
            List<String> errors = new ArrayList<>();
            errors.add("user.one");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/virtualeditions/restricted/" + externalId + "/participants";

        } else {
            virtualEdition.removeMember(user);

            if (user == this.FEVirtualRequiresInterface.getAuthenticatedUser()) {
                return "redirect:/virtualeditions";
            } else {
                return "redirect:/virtualeditions/restricted/" + externalId + "/participants";
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/addinter/{veId}/{interId}")
    @PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
    public String addInter(Model model, @PathVariable String veId, @PathVariable String interId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
        DomainObject inter = FenixFramework.getDomainObject(interId);
        if (virtualEdition == null || inter == null) {
            return "redirect:/error";
        }

        VirtualEditionInter addInter;

        if (inter instanceof ScholarInter) {
            addInter = virtualEdition.createVirtualEditionInter(new ScholarInterDto(((ScholarInter) inter).getXmlId()),
                    virtualEdition.getMaxFragNumber() + 1);
        } else {
            addInter = virtualEdition.createVirtualEditionInter((VirtualEditionInter) inter,
                    virtualEdition.getMaxFragNumber() + 1);
        }

        if (addInter == null) {
            return "redirect:/error";
        } else {
            return "redirect:/fragments/fragment/inter/" + interId;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String taxonomy(Model model, @PathVariable String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            List<String> errors = (List<String>) model.asMap().get("categoryErrors");
            model.addAttribute("categoryErrors", errors);
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("userInterface", this.FEVirtualRequiresInterface);
            return "virtual/taxonomy";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/edit")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String editTaxonomy(Model model, @PathVariable("externalId") String externalId,
                               @RequestParam("management") boolean management, @RequestParam("vocabulary") boolean vocabulary,
                               @RequestParam("annotation") boolean annotation) {
        logger.debug("editTaxonomy externalId:{}, management:{}, vocabulary:{}, annotation:{}", externalId, management,
                vocabulary, annotation);

        VirtualEdition edition = FenixFramework.getDomainObject(externalId);
        if (edition == null) {
            return "redirect:/error";
        } else {
            edition.getTaxonomy().edit(management, vocabulary, annotation);
            return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy/generateTopics")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public String generateTopicModelling(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                         @PathVariable String externalId, @RequestParam("numTopics") int numTopics,
                                         @RequestParam("numWords") int numWords, @RequestParam("thresholdCategories") int thresholdCategories,
                                         @RequestParam("numIterations") int numIterations) throws IOException {
        logger.debug(
                "generateTopicModelling externalId:{}, numTopics:{}, numWords:{}, thresholdCategories:{}, numIterations:{}",
                externalId, numTopics, numWords, thresholdCategories, numIterations);

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            String username = this.FEVirtualRequiresInterface.getAuthenticatedUser();

            List<String> topicErrors = new ArrayList<>();
            TopicListDTO topicListDTO = null;
            TopicModeler modeler = new TopicModeler();
            try {
                topicListDTO = modeler.generate(username, virtualEdition, numTopics, numWords,
                        thresholdCategories, numIterations);
            } catch (LdoDException ex) {
                topicErrors.add("Não existe nenhum fragmento associado a esta edição ou é necessário gerar o Corpus");
                model.addAttribute("topicErrors", topicErrors);
                model.addAttribute("topicList", topicListDTO);
                return "virtual/generatedTopics";
            }

            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("topicList", topicListDTO);
            return "virtual/generatedTopics";
        }
    }

    // necessary to allow a deep @ModelAttribute("topicList") in
    // createTopicModelling
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setAutoGrowCollectionLimit(1000);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/createTopics")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public String createTopicModelling(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                       @PathVariable String externalId, @ModelAttribute("topicList") TopicListDTO topicList) throws IOException {
        logger.debug("createTopicModelling externalId:{}, username:{}", externalId, topicList.getUsername());

        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            Taxonomy taxonomy = virtualEdition.getTaxonomy();
            taxonomy.createGeneratedCategories(topicList);

            return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/clean")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public String deleteTaxonomy(Model model, @PathVariable("externalId") String externalId,
                                 @RequestParam("taxonomyExternalId") String taxonomyExternalId) {
        Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyExternalId);
        if (taxonomy == null) {
            return "redirect:/error";
        } else {
            VirtualEdition edition = taxonomy.getEdition();
            taxonomy.remove();
            edition.setTaxonomy(new Taxonomy());

            return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/category/create")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
    public String createCategory(RedirectAttributes redirectAttributes, @RequestParam("externalId") String externalId,
                                 @RequestParam("name") String name) {
        VirtualEdition edition = FenixFramework.getDomainObject(externalId);
        List<String> errors = new ArrayList<>();
        if (edition == null) {
            return "redirect:/error";
        } else {
            try {
                edition.getTaxonomy().createCategory(name);
            } catch (LdoDDuplicateNameException ex) {
                errors.add("general.category.exists");
            }

            if (errors.isEmpty()) {
                return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
            } else {
                redirectAttributes.addFlashAttribute("categoryErrors", errors);
                return "redirect:/virtualeditions/restricted/" + externalId + "/taxonomy";
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/category/update")
    @PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
    public String updateCategoryName(RedirectAttributes redirectAttributes,
                                     @RequestParam("categoryId") String categoryId, @RequestParam("name") String name) {
        Category category = FenixFramework.getDomainObject(categoryId);
        List<String> errors = new ArrayList<>();
        if (category == null) {
            return "redirect:/error";
        } else {
            try {
                category.setName(name);
            } catch (LdoDDuplicateNameException ex) {
                errors.add("general.category.exists");
            }
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/virtualeditions/restricted/category/" + categoryId;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/category/delete")
    @PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
    public String deleteCategory(Model model, @RequestParam("categoryId") String categoryId) {
        Category category = FenixFramework.getDomainObject(categoryId);
        if (category == null) {
            return "redirect:/error";
        }
        VirtualEdition virtualEdition = category.getTaxonomy().getEdition();
        category.remove();

        return "redirect:/virtualeditions/restricted/" + virtualEdition.getExternalId() + "/taxonomy";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/category/{categoryId}")
    @PreAuthorize("hasPermission(#categoryId, 'category.participant')")
    public String showCategory(Model model, @PathVariable String categoryId) {
        Category category = FenixFramework.getDomainObject(categoryId);
        if (category == null) {
            return "redirect:/error";
        } else {
            List<String> errors = (List<String>) model.asMap().get("errors");
            model.addAttribute("errors", errors);
            model.addAttribute("category", category);
            model.addAttribute("userInterface", this.FEVirtualRequiresInterface);
            return "virtual/category";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/category/mulop")
    @PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.taxonomy')")
    public String mergeCategories(Model model, @RequestParam("taxonomyId") String taxonomyId,
                                  @RequestParam("type") String type,
                                  @RequestParam(value = "categories[]", required = false) String[] categoriesIds) {
        Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
        if (taxonomy == null) {
            return "redirect:/error";
        }

        if (categoriesIds == null) {
            return "redirect:/virtualeditions/restricted/" + taxonomy.getEdition().getExternalId() + "/taxonomy";
        }

        List<Category> categories = new ArrayList<>();
        for (String categoryId : categoriesIds) {
            Category category = FenixFramework.getDomainObject(categoryId);
            categories.add(category);
        }

        if (type.equals("merge") && categories.size() > 1) {
            Category category = taxonomy.merge(categories);
            return "redirect:/virtualeditions/restricted/category/" + category.getExternalId();
        }

        if (type.equals("delete") && categories.size() >= 1) {
            taxonomy.delete(categories);
            return "redirect:/virtualeditions/restricted/" + taxonomy.getEdition().getExternalId() + "/taxonomy";
        }

        return "redirect:/virtualeditions/restricted/" + taxonomy.getEdition().getExternalId() + "/taxonomy";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/category/extractForm")
    @PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
    public String extractForm(Model model, @RequestParam("categoryId") String categoryId) {
        Category category = FenixFramework.getDomainObject(categoryId);
        if (category == null) {
            return "redirect:/error";
        }

        model.addAttribute("category", category);
        return "virtual/extractForm";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/category/extract")
    @PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
    public String extractCategory(Model model, @RequestParam("categoryId") String categoryId,
                                  @RequestParam(value = "inters[]", required = false) String[] interIds) {
        Category category = FenixFramework.getDomainObject(categoryId);
        if (category == null) {
            return "redirect:/error";
        }

        if (interIds == null || interIds.length == 0) {
            return "redirect:/virtualeditions/restricted/category/" + category.getExternalId();
        }

        Set<VirtualEditionInter> inters = new HashSet<>();
        for (String interId : interIds) {
            VirtualEditionInter inter = FenixFramework.getDomainObject(interId);
            inters.add(inter);
        }

        Category extractedCategory = category.getTaxonomy().extract(category, inters);

        return "redirect:/virtualeditions/restricted/category/" + extractedCategory.getExternalId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}")
    @PreAuthorize("hasPermission(#fragInterId, 'fragInter.participant')")
    public String showFragmentInterpretation(Model model, @PathVariable String fragInterId) {
        VirtualEditionInter virtualEditionInter = FenixFramework.getDomainObject(fragInterId);
        if (virtualEditionInter == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("fragInter", virtualEditionInter);
            return "virtual/fragInter";
        }
    }

    // no access control because the only tags removed are from the logged user
    @RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}/tag/dissociate/{categoryId}")
    public String dissociate(Model model, @PathVariable String fragInterId, @PathVariable String categoryId) {
        VirtualEditionInter inter = FenixFramework.getDomainObject(fragInterId);

        Category category = FenixFramework.getDomainObject(categoryId);
        if (inter == null || category == null) {
            return "redirect:/error";
        }

        inter.dissociate(this.FEVirtualRequiresInterface.getAuthenticatedUser(), category);

        return "redirect:/fragments/fragment/inter/" + inter.getExternalId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/tag/associate")
    @PreAuthorize("hasPermission(#fragInterId, 'fragInter.annotation')")
    public String associateCategory(Model model, @RequestParam("fragInterId") String fragInterId,
                                    @RequestParam(value = "categories[]", required = false) String[] categories) {
        logger.debug("associateCategory categories[]:{}",
                categories != null ? Arrays.stream(categories).collect(Collectors.joining(",")) : "null");

        VirtualEditionInter inter = FenixFramework.getDomainObject(fragInterId);

        if (inter == null) {
            return "redirect:/error";
        }

        if (categories != null && categories.length > 0) {
            inter.associate(this.FEVirtualRequiresInterface.getAuthenticatedUser(), Arrays.stream(categories).collect(Collectors.toSet()));
        }

        return "redirect:/fragments/fragment/inter/" + inter.getExternalId();

    }
}
