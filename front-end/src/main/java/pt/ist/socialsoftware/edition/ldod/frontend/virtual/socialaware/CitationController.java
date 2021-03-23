package pt.ist.socialsoftware.edition.ldod.frontend.virtual.socialaware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;

@Controller
@RequestMapping("/citations")
public class CitationController {
    private static final Logger logger = LoggerFactory.getLogger(CitationController.class);

    private final FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

    @RequestMapping(method = RequestMethod.GET)
    public String listCitations(Model model) {
        logger.debug("listCitations");
        model.addAttribute("citations", feVirtualRequiresInterface.getCitationsWithInfoRanges());
        return "reading/citations";
    }

}
