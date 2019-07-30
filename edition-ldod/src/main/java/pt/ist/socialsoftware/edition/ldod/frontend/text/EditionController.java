package pt.ist.socialsoftware.edition.ldod.frontend.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.EditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.game.api.dto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.game.api.dto.PlayerDto;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/edition")
public class EditionController {
    private static final Logger logger = LoggerFactory.getLogger(EditionController.class);

    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();
    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    @ModelAttribute("frontendSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getFrontendSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String editionIntro(Model model) {
        return "edition/introduction-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public String getEditionTableOfContentsbyAcronym(Model model, @PathVariable String acronym) {
        ExpertEdition expertEdition = TextModule.getInstance().getExpertEdition(acronym);

        if (expertEdition != null) {
            model.addAttribute("heteronym", null);
            model.addAttribute("edition", expertEdition);
            model.addAttribute("editionDto", new EditionDto(expertEdition));
            model.addAttribute("uiInterface", new UiInterface());
            return "edition/tableOfContents";
        }

        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);

        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("heteronym", null);
            model.addAttribute("edition", virtualEdition);
            model.addAttribute("editionDto", new EditionDto(virtualEdition));
            model.addAttribute("uiInterface", new UiInterface());
            return "edition/tableOfContents";
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/internalid/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'edition.public')")
    public String getEditionTableOfContentsbyId(Model model, @PathVariable String externalId) {

        DomainObject edition = FenixFramework.getDomainObject(externalId);

        if (edition == null) {
            return "redirect:/error";
        } else {
            String acronym = (edition instanceof ScholarEdition) ? ((ScholarEdition) edition).getAcronym()
                    : ((VirtualEdition) edition).getAcronym();
            return "redirect:/edition/acronym/" + acronym;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/internalid/heteronym/{id1}/{id2}")
    public String getEditionTableOfContents4Heteronym(Model model, @PathVariable String id1, @PathVariable String id2) {

        ExpertEdition edition = FenixFramework.getDomainObject(id1);
        Heteronym heteronym = FenixFramework.getDomainObject(id2);

        if (edition == null) {
            return "redirect:/error";
        } else if (heteronym == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("heteronym", heteronym);
            model.addAttribute("edition", edition);
            model.addAttribute("editionDto", new EditionDto(edition));
            model.addAttribute("uiInterface", new UiInterface());

            return "edition/tableOfContents";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
    public String getUserContributions(Model model, @PathVariable String username) {
        UserDto userDto = this.userProvidesInterface.getUser(username);

        if (userDto != null) {
            model.addAttribute("userDto", userDto);
            model.addAttribute("publicVirtualEditionsOrUserIsParticipant", VirtualModule.getInstance().getPublicVirtualEditionsOrUserIsParticipant(username));
            model.addAttribute("virtualEditionIntersUserIsContributor", VirtualModule.getInstance().getVirtualEditionIntersUserIsContributor(username));
            PlayerDto player = this.gameProvidesInterface.getPlayerByUsername(username);
            if (player != null) {
                model.addAttribute("player", player);
                Set<ClassificationGameDto> games = this.gameProvidesInterface.getClassificationGamesForPlayer(username);
                model.addAttribute("games", games);
                model.addAttribute("position", this.gameProvidesInterface.getOverallUserPosition(username));
            }
            model.addAttribute("uiInterface", new UiInterface());
            model.addAttribute("virtualProvidesInterface", virtualProvidesInterface);
            return "edition/userContributions";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/taxonomy")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public String getTaxonomyTableOfContents(Model model, @PathVariable String acronym) {
        Taxonomy taxonomy = VirtualModule.getInstance().getVirtualEdition(acronym).getTaxonomy();
        if (taxonomy != null) {
            model.addAttribute("taxonomy", taxonomy);
            model.addAttribute("userInterface", this.userProvidesInterface);
            return "edition/taxonomyTableOfContents";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/category/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public String getCategoryTableOfContents(Model model, @PathVariable String acronym, @PathVariable String urlId) {

        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        Category category = virtualEdition.getTaxonomy().getCategoryByUrlId(urlId);
        if (category == null) {
            return "redirect:/error";
        }

        model.addAttribute("category", category);
        model.addAttribute("uiInterface", new UiInterface());

        return "edition/categoryTableOfContents";
    }

}
