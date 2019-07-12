package pt.ist.socialsoftware.edition.ldod.virtual.api.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.ist.socialsoftware.edition.ldod.domain.User;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.frontend.search.SearchFrontendRequiresInterface;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/search")
public class VirtualProvidesSearchController {
    private static final Logger logger = LoggerFactory.getLogger(VirtualProvidesSearchController.class);

    SearchFrontendRequiresInterface frontendRequiresInterface = new SearchFrontendRequiresInterface();


    /*
     * EditionController Sets all the empty boxes to null instead of the empty string ""
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/getVirtualEditions")
    @ResponseBody
    public Map<String, String> getVirtualEditions(Model model) {
        Stream<VirtualEdition> virtualEditionStream = VirtualModule.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub());

        User user = User.getAuthenticatedUser();
        if (user != null) {
            virtualEditionStream = Stream.concat(virtualEditionStream,
                    VirtualModule.getInstance().getSelectedVirtualEditionsByUser(user.getUsername()).stream()).distinct();
        }
        return virtualEditionStream.collect(Collectors.toMap(VirtualEdition::getAcronym, VirtualEdition::getTitle));
    }

}