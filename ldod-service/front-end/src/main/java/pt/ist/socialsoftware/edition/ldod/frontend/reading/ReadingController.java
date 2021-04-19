package pt.ist.socialsoftware.edition.ldod.frontend.reading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pt.ist.socialsoftware.edition.ldod.frontend.user.session.FrontendSession;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDException;
import pt.ist.socialsoftware.edition.virtual.api.textdto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textdto.ScholarInterDto;


import java.util.Set;

@Controller
@SessionAttributes({"frontendSession"})
@RequestMapping("/reading")
public class ReadingController {
    private static final Logger logger = LoggerFactory.getLogger(ReadingController.class);

    private final FeReadingRequiresInterface FEReadingRequiresInterface = new FeReadingRequiresInterface();

    @ModelAttribute("frontendSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getFrontendSession();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String startReading(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession) {
        model.addAttribute("sortedExpertEditions", this.FEReadingRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("inter", null);

        return "reading/readingMain";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}")
    public String readInterpretation(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                     @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.FEReadingRequiresInterface.getFragmentByXmlId(xmlId);
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
        ScholarInterDto prevRecom = frontendSession.getRecommendation().getPrevRecommendation();

        //model.addAttribute("ldoD", VirtualModule.getInstance());
        model.addAttribute("sortedExpertEditions", this.FEReadingRequiresInterface.getSortedExpertEditionsDto());
        model.addAttribute("inter", expertEditionInter);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("prevRecom", prevRecom);

        return "reading/readingMain";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edition/{acronym}/start")
    public String startReadingEdition(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                      @PathVariable String acronym) {
        ScholarInterDto expertEditionInter = this.FEReadingRequiresInterface.getExpertEditionFirstInterpretation(acronym);

        frontendSession.getRecommendation().clean();
        frontendSession.getRecommendation().setTextWeight(1);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/start")
    public String startReadingFromInter(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                        @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.FEReadingRequiresInterface.getFragmentByXmlId(xmlId);
        if (fragment == null) {
            return "redirect:/error";
        }

        ScholarInterDto expertEditionInter = fragment.getScholarInterByUrlId(urlId);
        if (expertEditionInter == null) {
            return "redirect:/error";
        }

        frontendSession.getRecommendation().clean();
        frontendSession.getRecommendation().setTextWeight(1);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/next")
    public String readNextInterpretation(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                         @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.FEReadingRequiresInterface.getFragmentByXmlId(xmlId);
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
    public String readPrevInterpretation(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                         @PathVariable String xmlId, @PathVariable String urlId) {
        FragmentDto fragment = this.FEReadingRequiresInterface.getFragmentByXmlId(xmlId);
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
    public String readPreviousRecommendedFragment(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession) {
        String expertEditionInterId = frontendSession.getRecommendation().prevRecommendation();

        ScholarInterDto expertEditionInter = this.FEReadingRequiresInterface.getScholarInterbyExternalId(expertEditionInterId);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/inter/prev/recom/reset")
    public String resetPreviousRecommendedFragments(Model model,
                                                    @ModelAttribute("frontendSession") FrontendSession frontendSession) {

        frontendSession.getRecommendation().resetPrevRecommendations();

        String expertEditionInterId = frontendSession.getRecommendation().getCurrentInterpretation();
        ScholarInterDto expertEditionInter = this.FEReadingRequiresInterface.getScholarInterbyExternalId(expertEditionInterId);

        return "redirect:/reading/fragment/" + expertEditionInter.getFragmentXmlId() + "/inter/"
                + expertEditionInter.getUrlId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/weight", produces = "application/json")
    public ResponseEntity<String> changeWeight(Model model, @ModelAttribute("frontendSession") FrontendSession frontendSession,
                                               @RequestParam String type, @RequestParam float value) {

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
