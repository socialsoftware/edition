package pt.ist.socialsoftware.edition.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.session.LdoDSession;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/reading")
public class ReadingController {
	private static Logger logger = LoggerFactory.getLogger(ReadingController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		LdoDSession ldoDSession = new LdoDSession();

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		if (user != null) {
			for (VirtualEdition virtualEdition : user.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
		}
		return ldoDSession;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String startReading(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession) {
		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("inter", null);

		return "reading/readingMain";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}")
	public String readInterpretation(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String xmlId, @PathVariable String urlId) {
		logger.debug("readInterpretation xmlId:{}, urlId:{}", xmlId, urlId);
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return "utils/pageNotFound";
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return "utils/pageNotFound";
		}

		Set<ExpertEditionInter> recommendations = ldoDSession.getRecommendation()
				.getNextRecommendations(expertEditionInter.getExternalId());
		ExpertEditionInter prevRecom = ldoDSession.getRecommendation().getPrevRecommendation();

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(expertEditionInter);
		writer.write(false);

		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("inter", expertEditionInter);
		model.addAttribute("recommendations", recommendations);
		model.addAttribute("prevRecom", prevRecom);
		model.addAttribute("writer", writer);

		return "reading/readingMain";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/first/edition/{expertEditionId}")
	public String readFirstInterpretationFromEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession, @PathVariable String expertEditionId) {
		ExpertEdition expertEdition = FenixFramework.getDomainObject(expertEditionId);
		ExpertEditionInter expertEditionInter = expertEdition.getFirstInterpretation();

		ldoDSession.getRecommendation().clean();
		ldoDSession.getRecommendation().setTextWeight(1.0);

		return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
				+ expertEditionInter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/first/inter/{expertEditionInterId}")
	public String readFirstInterpretationFromInter(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String expertEditionInterId) {
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);
		ldoDSession.getRecommendation().clean();
		ldoDSession.getRecommendation().setTextWeight(1.0);

		return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
				+ expertEditionInter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/next/number/{expertEditionInterId}")
	public String readNextInterpretation(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String expertEditionInterId) {
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

		FragInter nextExpertEditionInter = expertEditionInter.getEdition().getNextNumberInter(expertEditionInter,
				expertEditionInter.getNumber());

		return "redirect:/reading/fragment/" + nextExpertEditionInter.getFragment().getXmlId() + "/inter/"
				+ nextExpertEditionInter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/prev/number/{expertEditionInterId}")
	public String readPrevInterpretation(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String expertEditionInterId) {
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

		FragInter prevExpertEditionInter = expertEditionInter.getEdition().getPrevNumberInter(expertEditionInter,
				expertEditionInter.getNumber());

		return "redirect:/reading/fragment/" + prevExpertEditionInter.getFragment().getXmlId() + "/inter/"
				+ prevExpertEditionInter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom")
	public String readPreviousRecommendedFragment(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession) {
		// logger.debug("readPreviousRecommendedFragment");

		String expertEditionInterId = ldoDSession.getRecommendation().prevRecommendation();
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

		return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
				+ expertEditionInter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom/reset")
	public String resetPreviousRecommendedFragments(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession) {
		// logger.debug("readPreviousRecommendedFragment");

		ldoDSession.getRecommendation().resetPrevRecommendations();

		String expertEditionInterId = ldoDSession.getRecommendation().getCurrentInterpretation();
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

		return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
				+ expertEditionInter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/weight", produces = "application/json")
	public ResponseEntity<String> changeWeight(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam String type, @RequestParam double value) {
		logger.debug("changeWeight type:{}, value:{}", type, value);

		switch (type) {
		case "heteronym":
			ldoDSession.getRecommendation().setHeteronymWeight(value);
			break;
		case "date":
			ldoDSession.getRecommendation().setDateWeight(value);
			break;
		case "text":
			ldoDSession.getRecommendation().setTextWeight(value);
			break;
		case "taxonomy":
			ldoDSession.getRecommendation().setTaxonomyWeight(value);
			break;
		default:
			throw new LdoDException("ReadingController.changeWeight type does not exist " + type);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
