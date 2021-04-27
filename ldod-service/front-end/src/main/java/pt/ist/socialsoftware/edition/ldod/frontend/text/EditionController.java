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


import pt.ist.socialsoftware.edition.game.api.dtoc.ClassificationGameDto;

import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.game.api.GameProvidesInterface;
import pt.ist.socialsoftware.edition.game.api.dtoc.PlayerDto;

import pt.ist.socialsoftware.edition.ldod.frontend.utils.dto.EditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.ui.UiInterface;

import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.CategoryDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.TaxonomyDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.HeteronymDto;


import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/edition")
public class EditionController {
    private static final Logger logger = LoggerFactory.getLogger(EditionController.class);

    private final FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();
    private final GameProvidesInterface gameProvidesInterface = new GameProvidesInterface();
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();
    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

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
       // ExpertEdition expertEdition = TextModule.getInstance().getExpertEdition(acronym);
        ExpertEditionDto expertEdition = this.feTextRequiresInterface.getExpertEditionByAcronym(acronym);
        if (expertEdition != null) {
            model.addAttribute("heteronym", null);
            model.addAttribute("edition", expertEdition);
            model.addAttribute("editionDto", expertEdition);
            model.addAttribute("uiInterface", new UiInterface());
            return "edition/tableOfContents";
        }

        VirtualEditionDto virtualEdition = this.virtualProvidesInterface.getVirtualEdition(acronym);

        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("heteronym", null);
            model.addAttribute("edition", virtualEdition);
            model.addAttribute("editionDto", virtualEdition);
            model.addAttribute("uiInterface", new UiInterface());
            return "edition/tableOfContents";
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/internalid/{externalId}")
    @PreAuthorize("hasPermission(#externalId, 'edition.public')")
    public String getEditionTableOfContentsbyId(Model model, @PathVariable String externalId) {

        String scholarEdition = this.feTextRequiresInterface.getScholarEditionAcronymbyExternal(externalId);
        String virtualEdition = null;

        if (scholarEdition == null) {
            virtualEdition = this.virtualProvidesInterface.getVirtualEditionByExternalId(externalId).getAcronym();
        }
        if (scholarEdition == null && virtualEdition == null) {
            return "redirect:/error";
        } else {
            String acronym = (this.feTextRequiresInterface.isDomainObjectScholarEdition(externalId)) ? scholarEdition
                    : virtualEdition;
            return "redirect:/edition/acronym/" + acronym;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/internalid/heteronym/{id1}/{id2}")
    public String getEditionTableOfContents4Heteronym(Model model, @PathVariable String id1, @PathVariable String id2) {

        ExpertEditionDto edition = this.feTextRequiresInterface.getExpertEditionByExternalId(id1);
        HeteronymDto heteronym = this.feTextRequiresInterface.getHeteronymByExternalId(id2);

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
        UserDto userDto = this.feUserRequiresInterface.getUser(username);
        if (userDto != null) {
            model.addAttribute("userDto", userDto);
            model.addAttribute("publicVirtualEditionsOrUserIsParticipant", this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(username));
            model.addAttribute("virtualEditionIntersUserIsContributor", this.virtualProvidesInterface.getVirtualEditionIntersUserIsContributor(username));
            PlayerDto player = this.gameProvidesInterface.getPlayerByUsername(username);
            if (player != null) {
                model.addAttribute("player", player);
                Set<ClassificationGameDto> games = this.gameProvidesInterface.getClassificationGamesForPlayer(username);
                model.addAttribute("games", games);
                model.addAttribute("position", this.gameProvidesInterface.getOverallUserPosition(username));
            }
            model.addAttribute("uiInterface", new UiInterface());
            model.addAttribute("virtualProvidesInterface", this.virtualProvidesInterface);
            return "edition/userContributions";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/taxonomy")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public String getTaxonomyTableOfContents(Model model, @PathVariable String acronym) {
        TaxonomyDto taxonomy = this.virtualProvidesInterface.getVirtualEditionTaxonomy(acronym);
        if (taxonomy != null) {
            model.addAttribute("taxonomy", taxonomy);
            model.addAttribute("userInterface", feUserRequiresInterface);
            return "edition/taxonomyTableOfContents";
        } else {
            return "redirect:/error";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}/category/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public String getCategoryTableOfContents(Model model, @PathVariable String acronym, @PathVariable String urlId) {

        VirtualEditionDto virtualEdition = this.virtualProvidesInterface.getVirtualEdition(acronym);
        if (virtualEdition == null) {
            return "redirect:/error";
        }

        CategoryDto category = virtualEdition.getTaxonomy().getCategoriesSet().stream().filter(categoryDto -> categoryDto.getUrlId().equals(urlId)).collect(Collectors.toList()).get(0);

        if (category == null) {
            return "redirect:/error";
        }

        model.addAttribute("category", category);
        model.addAttribute("userInterface", this.feUserRequiresInterface);
        model.addAttribute("uiInterface", new UiInterface());

        return "edition/categoryTableOfContents";
    }

}
