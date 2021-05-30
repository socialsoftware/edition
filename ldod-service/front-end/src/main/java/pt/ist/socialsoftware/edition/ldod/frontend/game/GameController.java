package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import pt.ist.socialsoftware.edition.ldod.frontend.game.gameDto.ClassificationGameDto;
import pt.ist.socialsoftware.edition.ldod.frontend.game.validator.ClassificationGameValidator;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDCreateClassificationGameException;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDException;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;


import java.util.List;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/virtualeditions")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final FeGameRequiresInterface FEGameRequiresInterface = new FeGameRequiresInterface();


    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/classificationGame")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String classificationGame(Model model, @PathVariable String externalId) {
        VirtualEditionDto virtualEdition = this.FEGameRequiresInterface.getVirtualEditionByExternalId(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("games", this.FEGameRequiresInterface.getClassificationGamesForEdition(virtualEdition.getAcronym()));
            model.addAttribute("userInterface", this.FEGameRequiresInterface);
            model.addAttribute("virtualInterface", this.FEGameRequiresInterface);
            return "virtual/classificationGame";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/classificationGame/create")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String createClassificationGameForm(Model model, @PathVariable String externalId) {
        VirtualEditionDto virtualEdition = this.FEGameRequiresInterface.getVirtualEditionByExternalId(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("userInterface", this.FEGameRequiresInterface);
            model.addAttribute("virtualInterface", this.FEGameRequiresInterface);
            return "virtual/createClassificationGame";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/classificationGame/create")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String createClassificationGame(Model model, @PathVariable String externalId,
                                           @RequestParam("description") String description, @RequestParam("date") String date,
                                           @RequestParam("interExternalId") String interExternalId) {
        logger.debug("createClassificationGame description: {}, date: {}, inter:{}", description, date,
                interExternalId);
        VirtualEditionDto virtualEdition = this.FEGameRequiresInterface.getVirtualEditionByExternalId(externalId);
        VirtualEditionInterDto inter = this.FEGameRequiresInterface.getVirtualEditionInterByExternalId(interExternalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            ClassificationGameValidator validator = new ClassificationGameValidator(description, interExternalId);
            validator.validate();

            List<String> errors = validator.getErrors();
            if (errors.size() > 0) {
                throw new LdoDCreateClassificationGameException(errors, description, date, interExternalId,
                        virtualEdition);
            }
            this.FEGameRequiresInterface.createClassificationGame(virtualEdition, description,
                    DateTime.parse(date, DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")), inter,
                    this.FEGameRequiresInterface.getAuthenticatedUser());

            return "redirect:/virtualeditions/restricted/" + externalId + "/classificationGame";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/classificationGame/{gameExternalId}/remove")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
    public String removeClassificationGame(Model model, @PathVariable String externalId,
                                           @PathVariable String gameExternalId) {
        logger.debug("removeClassificationGame externalId: {}, gameExternalId: {}", externalId, gameExternalId);
        VirtualEditionDto virtualEdition = this.FEGameRequiresInterface.getVirtualEditionByExternalId(externalId);
        ClassificationGameDto game = this.FEGameRequiresInterface.getClassificationGameByExternalId(gameExternalId);
        if (virtualEdition == null || game == null) {
            return "redirect:/error";
        } else {
            System.out.println(game.canBeRemoved());
            if (game.canBeRemoved()) {
                game.remove();
            } else {
                throw new LdoDException("Cannot remove finished game");
            }

            return "redirect:/virtualeditions/restricted/" + externalId + "/classificationGame";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{externalId}/classificationGame/{gameId}")
    public String getClassificationGameContent(Model model, @PathVariable String externalId,
                                               @PathVariable String gameId) {
        VirtualEditionDto virtualEdition = this.FEGameRequiresInterface.getVirtualEditionByExternalId(externalId);
        ClassificationGameDto game = this.FEGameRequiresInterface.getClassificationGameByExternalId(gameId);
        if (virtualEdition == null || game == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("game", game);
            model.addAttribute("participants",
                    game.getClassificationGameParticipantSet());
            model.addAttribute("virtualInterface", this.FEGameRequiresInterface);
            return "virtual/classificationGameContent";
        }
    }
}
