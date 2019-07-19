package pt.ist.socialsoftware.edition.ldod.frontend.reading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.util.Set;

@Controller
@SessionAttributes({"ldoDSession"})
@RequestMapping("/reading")
public class ReadingController {
    private static final Logger logger = LoggerFactory.getLogger(ReadingController.class);

    private final ReadingRequiresInterface readingRequiresInterface = new ReadingRequiresInterface();

    @ModelAttribute("ldoDSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String startReading(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession) {
        model.addAttribute("sortedExpertEditions", this.readingRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("inter", null);

        return "reading/readingMain";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}")
    public String readInterpretation(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                     @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.readingRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ScholarInterDto expertEditionInter = fragment.getScholarInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        // TODO: the interaction with recommendation
        Set<ScholarInterDto> recommendations = frontendSession.getRecommendation()
                .getNextRecommendations(expertEditionInter.getExternalId());
        ExpertEditionInter prevRecom = frontendSession.getRecommendation().getPrevRecommendation();

        model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("sortedExpertEditions", this.readingRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("inter", expertEditionInter);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("prevRecom", prevRecom);

        return "reading/readingMain";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edition/{acronym}/start")
    public String startReadingEdition(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                      @PathVariable String acronym) {
        ScholarInterDto expertEditionInter = this.readingRequiresInterface.getExpertEditionFirstInterpretation(acronym);

        frontendSession.getRecommendation().clean();
        frontendSession.getRecommendation().setTextWeight(1.0);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/start")
    public String startReadingFromInter(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                        @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.readingRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ScholarInterDto expertEditionInter = fragment.getScholarInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        frontendSession.getRecommendation().clean();
        frontendSession.getRecommendation().setTextWeight(1.0);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/next")
    public String readNextInterpretation(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                         @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.readingRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ScholarInterDto expertEditionInter = fragment.getScholarInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        ScholarInterDto nextExpertEditionInter = expertEditionInter.getNextNumberInter();

        return "redirect:/reading/fragment/" + nextExpertEditionInter.getFragmentXmlId() + "/inter/"
                + nextExpertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/prev")
    public String readPrevInterpretation(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                         @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.readingRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ScholarInterDto expertEditionInter = fragment.getScholarInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        ScholarInterDto prevExpertEditionInter = expertEditionInter.getPrevNumberInter();

        return "redirect:/reading/fragment/" + prevExpertEditionInter.getFragmentXmlId() + "/inter/"
                + prevExpertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom")
    public String readPreviousRecommendedFragment(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession) {
        String expertEditionInterId = frontendSession.getRecommendation().prevRecommendation();

        ScholarInterDto expertEditionInter = this.readingRequiresInterface.getScholarInterbyExternalId(expertEditionInterId);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom/reset")
    public String resetPreviousRecommendedFragments(Model model,
                                                    @ModelAttribute("ldoDSession") FrontendSession frontendSession) {

        frontendSession.getRecommendation().resetPrevRecommendations();

        String expertEditionInterId = frontendSession.getRecommendation().getCurrentInterpretation();
        ScholarInterDto expertEditionInter = this.readingRequiresInterface.getScholarInterbyExternalId(expertEditionInterId);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/weight", produces = "application/json")
    public ResponseEntity<String> changeWeight(Model model, @ModelAttribute("ldoDSession") FrontendSession frontendSession,
                                               @RequestParam String type, @RequestParam double value) {

        switch (type) {
            case "heteronym":
                frontendSession.getRecommendation().setHeteronymWeight(value);
                break;
            case "date":
                frontendSession.getRecommendation().setDateWeight(value);
                break;
            case "text":
                frontendSession.getRecommendation().setTextWeight(value);
                break;
            case "taxonomy":
                frontendSession.getRecommendation().setTaxonomyWeight(value);
                break;
            default:
                throw new LdoDException("ReadingController.changeWeight type does not exist " + type);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
