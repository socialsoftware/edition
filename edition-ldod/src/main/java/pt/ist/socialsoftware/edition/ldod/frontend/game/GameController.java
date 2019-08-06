package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.ldod.frontend.game.validator.ClassificationGameValidator;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDCreateClassificationGameException;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/virtualeditions")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final FEGameRequiresInterface FEGameRequiresInterface = new FEGameRequiresInterface();

    @RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/classificationGame")
    @PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
    public String classificationGame(Model model, @PathVariable String externalId) {
        VirtualEditionDto virtualEdition = this.FEGameRequiresInterface.getVirtualEditionByExternalId(externalId);
        if (virtualEdition == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("games", ClassificationModule.getInstance().getClassificationGamesForEdition(virtualEdition.getAcronym())
                    .stream().sorted(Comparator.comparing(ClassificationGame::getDateTime)).collect(Collectors.toList()));
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
            ClassificationModule.createClassificationGame(virtualEdition, description,
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
        ClassificationGame game = FenixFramework.getDomainObject(gameExternalId);
        if (virtualEdition == null || game == null) {
            return "redirect:/error";
        } else {
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
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        if (virtualEdition == null || game == null) {
            return "redirect:/error";
        } else {
            model.addAttribute("virtualEdition", virtualEdition);
            model.addAttribute("game", game);
            model.addAttribute("participants",
                    game.getClassificationGameParticipantSet().stream()
                            .sorted(Comparator.comparing(ClassificationGameParticipant::getScore).reversed())
                            .collect(Collectors.toList()));
            model.addAttribute("virtualInterface", this.FEGameRequiresInterface);
            return "virtual/classificationGameContent";
        }
    }
}
