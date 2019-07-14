package pt.ist.socialsoftware.edition.ldod.virtual.api.remote.visual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import pt.ist.socialsoftware.edition.ldod.domain.TextModule;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/virtual/visual/editions")
public class VirtualProvidesVisualController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesVisualController.class);

    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    // SHOULD ALSO INCLUDE return this.axios.post('/recommendation/' + interId + '/intersByDistance',


    @GetMapping("/public")
    public @ResponseBody
    ResponseEntity<List<EditionInterListDto>> getPublicVirtualEditions() {
        logger.debug("getPublicVirtualEditions");


        List<EditionInterListDto> result = this.virtualProvidesInterface.getPublicVirtualEditionInterListDto();

        Stream.concat(TextModule.getInstance().getExpertEditionsSet().stream().map(expertEdition -> new EditionInterListDto(expertEdition)),
                VirtualModule.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub())
                        .map(virtualEdition -> new EditionInterListDto(virtualEdition, false)))
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
