package pt.ist.socialsoftware.edition.ldod.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @ModelAttribute("ldoDSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showHome(Model model) {
        return "home";
    }

}