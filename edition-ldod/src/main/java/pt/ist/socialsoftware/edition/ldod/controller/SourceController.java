package pt.ist.socialsoftware.edition.ldod.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.domain.Source;

@Controller
@RequestMapping("/source")
public class SourceController {
	private static Logger logger = LoggerFactory.getLogger(SearchController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		return LdoDSession.getLdoDSession();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list")
	public String getListOfSources(Model model) {
		logger.debug("getListOfSources");

		LdoD ldoD = LdoD.getInstance();

		List<Source> sources = new ArrayList<>();
		for (Fragment frag : ldoD.getFragmentsSet()) {
			sources.addAll(frag.getSourcesSet());
		}

		Collections.sort(sources);

		model.addAttribute("sources", sources);

		return "source/listSources";
	}
}
