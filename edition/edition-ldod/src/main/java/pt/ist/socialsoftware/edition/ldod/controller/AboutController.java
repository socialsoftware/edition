package pt.ist.socialsoftware.edition.ldod.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

@Controller
@RequestMapping("/about")
public class AboutController {
    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

    @ModelAttribute("ldoDSession")
    public LdoDSession getLdoDSession() {
        return LdoDSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/archive")
    public String showArchive() {
        return "about/archive-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/videos")
    public String showVideos() {
        return "about/videos-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tutorials")
    public String tutorials() {
        return "about/tutorials-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/faq")
    public String showFAQ() {
        return "about/faq-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/encoding")
    public String showEncoding() {
        return "about/encoding-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/articles")
    public String showArticles() {
        return "about/articles-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/book")
    public String showBook() {
        return "about/book-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/conduct")
    public String showConduct() {
        return "about/conduct-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/privacy")
    public String showPrivacy() {
        return "about/privacy-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/team")
    public String showTeam() {
        return "about/team-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/acknowledgements")
    public String showAcknowledgements() {
        return "about/acknowledgements-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/contact")
    public String showContact() {
        return "about/contact-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/copyright")
    public String showCopyright() {
        return "about/copyright-main";
    }

}
