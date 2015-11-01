package pt.ist.socialsoftware.edition.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.socialsoftware.edition.security.UserDetailsServiceImpl;

@Controller
public class LoginController {
    private static Logger log = LoggerFactory
            .getLogger(UserDetailsServiceImpl.class);

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String displayLoginPage() {
        log.debug("displayLoginPage");
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login", params = "errorLogin")
    public String directToLoginPageWithError(Model model) {
        // Adding an attribute to flag that an error happened at login
        model.addAttribute("loginFailed", true);
        return "login";
    }

}
