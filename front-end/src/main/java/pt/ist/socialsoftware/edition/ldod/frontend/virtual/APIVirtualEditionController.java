package pt.ist.socialsoftware.edition.ldod.frontend.virtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterGameDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionInterListDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class APIVirtualEditionController {
    private static final Logger logger = LoggerFactory.getLogger(APIVirtualEditionController.class);

    private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();


    @GetMapping("/edition/{acronym}/inter/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<VirtualEditionInterGameDto> getVirtualEditionInterText(
            @PathVariable(value = "acronym") String acronym, @PathVariable(value = "urlId") String urlId) {
        logger.debug("getVirtualEditionInterText acronym:{} urlId:{}", acronym, urlId);
//        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronym);
        VirtualEditionDto virtualEdition = this.feVirtualRequiresInterface.getVirtualEditionByAcronym(acronym);
        if (virtualEdition == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VirtualEditionInterDto inter =  virtualEdition.getFragInterByUrlId(urlId);
        if (inter == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VirtualEditionInterGameDto result = new VirtualEditionInterGameDto(inter);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    //New version using separeted DTO

    @GetMapping("/edition/{acronym}/index")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public @ResponseBody
    ResponseEntity<VirtualEditionInterListDto> getVirtualEditionIndex(
            @PathVariable(value = "acronym") String acronym) {
        logger.debug("getVirtualEditionIndex acronym:{}", acronym);
        VirtualEditionDto virtualEdition = this.feVirtualRequiresInterface.getVirtualEditionByAcronym(acronym);

        if (virtualEdition == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            VirtualEditionInterListDto result = this.feVirtualRequiresInterface.getVirtualEditionInterList(acronym, true);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

    @GetMapping("/{username}/restricted/virtualeditions")
    @PreAuthorize("hasPermission(#username, 'user.logged')")
    public @ResponseBody
    ResponseEntity<List<VirtualEditionInterListDto>> getVirtualEditions4User(
            @PathVariable(value = "username") String username) {
        FeUserRequiresInterface userProvidesInterface = new FeUserRequiresInterface();
        UserDto userDto = userProvidesInterface.getUser(username);

        if (userDto != null) {
            List<VirtualEditionDto> virtualEditionList = this.feVirtualRequiresInterface.getVirtualEditionsUserIsParticipantSelectedOrPublic(username);
            List<VirtualEditionInterListDto> result = virtualEditionList.stream()
                    .filter(virtualEditionDto -> virtualEditionDto.getParticipantSet().contains(username))
                    .map(virtualEditionDto -> this.feVirtualRequiresInterface.getVirtualEditionInterList(virtualEditionDto.getAcronym(), true))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{username}/public/virtualeditions")
    public @ResponseBody
    ResponseEntity<List<VirtualEditionInterListDto>> getPublicVirtualEditions4User(
            @PathVariable(value = "username") String username) {
        FeUserRequiresInterface userProvidesInterface = new FeUserRequiresInterface();
        UserDto userDto = userProvidesInterface.getUser(username);

        if (userDto != null) {

            List<VirtualEditionInterListDto> result = this.feVirtualRequiresInterface.getVirtualEditionsList().stream().filter(
                    virtualEdition -> virtualEdition.getParticipantSet().contains(username) && virtualEdition.getPub())
                    .map(ve -> this.feVirtualRequiresInterface.getVirtualEditionInterList(ve.getAcronym(), true)).collect(Collectors.toList());

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
