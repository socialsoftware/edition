package pt.ist.socialsoftware.edition.ldod.frontend.citation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;

@Controller
@RequestMapping("/citations")
public class CitationController {
    private static final Logger logger = LoggerFactory.getLogger(CitationController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String listCitations(Model model) {
        logger.debug("listCitations");
        model.addAttribute("citations", VirtualModule.getInstance().getCitationsWithInfoRanges());
        return "reading/citations";
    }

}
