package pt.ist.socialsoftware.edition.ldod.text.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.TextModule;
import pt.ist.socialsoftware.edition.ldod.frontend.serverside.search.SearchController;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/source")
public class SourceController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @ModelAttribute("ldoDSession")
    public LdoDSession getLdoDSession() {
        return LdoDSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public String getListOfSources(Model model) {
        logger.debug("getListOfSources");
        List<Source> sources = new ArrayList<>();
        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
            sources.addAll(frag.getSourcesSet());
        }

        Collections.sort(sources);

        model.addAttribute("sources", sources);

        return "source/listSources";
    }
}
