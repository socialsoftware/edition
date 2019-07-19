package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.game.api.dto.VirtualEditionInterGameDto;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class APIVirtualEditionController {
    private static final Logger logger = LoggerFactory.getLogger(APIVirtualEditionController.class);

    @GetMapping("/edition/{acronym}/index")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<EditionInterListDto> getVirtualEditionIndex(
            @PathVariable(value = "acronym") String acronym) {
        logger.debug("getVirtualEditionIndex acronym:{}", acronym);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);

        if (virtualEdition == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            EditionInterListDto result = new EditionInterListDto(virtualEdition, true);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

    @GetMapping("/edition/{acronym}/inter/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<VirtualEditionInterGameDto> getVirtualEditionInterText(
            @PathVariable(value = "acronym") String acronym, @PathVariable(value = "urlId") String urlId) {
        logger.debug("getVirtualEditionInterText acronym:{} urlId:{}", acronym, urlId);
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        if (virtualEdition == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VirtualEditionInter inter = (VirtualEditionInter) virtualEdition.getFragInterByUrlId(urlId);
        if (inter == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VirtualEditionInterGameDto result = new VirtualEditionInterGameDto(new VirtualEditionInterDto(inter));
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/{username}/restricted/virtualeditions")
    @PreAuthorize("hasPermission(#username, 'user.logged')")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getVirtualEditions4User(
            @PathVariable(value = "username") String username) {
        UserProvidesInterface userProvidesInterface = new UserProvidesInterface();
        UserDto userDto = userProvidesInterface.getUser(username);

        if (userDto != null) {
            List<VirtualEdition> virtualEditionList = VirtualModule.getInstance().getVirtualEditionsUserIsParticipant(username,
                    LdoDSession.getLdoDSession());
            List<EditionInterListDto> result = virtualEditionList.stream()
                    .filter(virtualEdition -> virtualEdition.getParticipantSet().contains(username))
                    .map(ve -> new EditionInterListDto(ve, true)).collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{username}/public/virtualeditions")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getPublicVirtualEditions4User(
            @PathVariable(value = "username") String username) {
        UserProvidesInterface userProvidesInterface = new UserProvidesInterface();
        UserDto userDto = userProvidesInterface.getUser(username);

        if (userDto != null) {
            List<EditionInterListDto> result = VirtualModule.getInstance().getVirtualEditionsSet().stream().filter(
                    virtualEdition -> virtualEdition.getParticipantSet().contains(username) && virtualEdition.getPub())
                    .map(ve -> new EditionInterListDto(ve, true)).collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
