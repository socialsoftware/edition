package pt.ist.socialsoftware.edition.ldod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.Set;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/reading")
public class ReadingController {
    private static Logger logger = LoggerFactory.getLogger(ReadingController.class);

    @ModelAttribute("ldoDSession")
    public LdoDSession getLdoDSession() {
        return LdoDSession.getLdoDSession();
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
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
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

    @RequestMapping(method = RequestMethod.GET, value = "/edition/{acronym}/start")
    public String startReadingEdition(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
                                      @PathVariable String acronym) {
        ExpertEdition expertEdition = (ExpertEdition) LdoD.getInstance().getEdition(acronym);
        ExpertEditionInter expertEditionInter = expertEdition.getFirstInterpretation();

        ldoDSession.getRecommendation().clean();
        ldoDSession.getRecommendation().setTextWeight(1.0);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/start")
    public String startReadingFromInter(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
                                        @PathVariable String xmlId, @PathVariable String urlId) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        ldoDSession.getRecommendation().clean();
        ldoDSession.getRecommendation().setTextWeight(1.0);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/next")
    public String readNextInterpretation(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
                                         @PathVariable String xmlId, @PathVariable String urlId) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        FragInter nextExpertEditionInter = expertEditionInter.getEdition().getNextNumberInter(expertEditionInter,
                expertEditionInter.getNumber());

        return "redirect:/reading/fragment/" + nextExpertEditionInter.getFragment().getXmlId() + "/inter/"
                + nextExpertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/prev")
    public String readPrevInterpretation(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
                                         @PathVariable String xmlId, @PathVariable String urlId) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        FragInter prevExpertEditionInter = expertEditionInter.getEdition().getPrevNumberInter(expertEditionInter,
                expertEditionInter.getNumber());

        return "redirect:/reading/fragment/" + prevExpertEditionInter.getFragment().getXmlId() + "/inter/"
                + prevExpertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom")
    public String readPreviousRecommendedFragment(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession) {

        String expertEditionInterId = ldoDSession.getRecommendation().prevRecommendation();
        ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragment().getXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom/reset")
    public String resetPreviousRecommendedFragments(Model model,
                                                    @ModelAttribute("ldoDSession") LdoDSession ldoDSession) {

        ldoDSession.getRecommendation().resetPrevRecommendations();

        String expertEditionInterId = ldoDSession.getRecommendation().getCurrentInterpretation();
        ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);

        return expertEditionInter != null
                ? String.format("redirect:/reading/fragment/%s/inter/%s", expertEditionInter.getFragment().getXmlId(), expertEditionInter.getUrlId())
                : "reading/readingMain";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/weight", produces = "application/json")
    public ResponseEntity<String> changeWeight(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
                                               @RequestParam String type, @RequestParam double value) {

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
