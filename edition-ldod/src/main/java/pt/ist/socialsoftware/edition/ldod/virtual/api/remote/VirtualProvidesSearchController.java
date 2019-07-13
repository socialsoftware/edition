package pt.ist.socialsoftware.edition.ldod.virtual.api.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class VirtualProvidesSearchController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesSearchController.class);

    VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();
    VirtualRequiresInterface virtualRequiresInterface = new VirtualRequiresInterface();

    @RequestMapping(value = "/getVirtualEditions")
    @ResponseBody
    public Map<String, String> getVirtualEditions(Model model) {
        return this.virtualProvidesInterface.getPublicVirtualEditionsOrUserIsParticipant(this.virtualRequiresInterface.getAuthenticatedUser()).stream()
                .collect(Collectors.toMap(VirtualEditionDto::getAcronym, VirtualEditionDto::getTitle));
    }

}