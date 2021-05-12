package pt.ist.socialsoftware.edition.ldod.frontend.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.frontend.search.SearchController;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.SourceDto;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/source")
public class SourceController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

    @ModelAttribute("frontendSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getFrontendSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String getListOfSources(Model model) {
        logger.debug("getListOfSources");
        List<SourceDto> sources = new ArrayList<>();

        for (FragmentDto frag : this.feTextRequiresInterface.getFragmentDtosWithSourceDtos()) {
            sources.addAll(frag.getEmbeddedSourceDtos());
        }

        sources.sort(Comparator.comparing(SourceDto::getName));

        model.addAttribute("sources", sources);

        return "source/listSources";
    }
}
