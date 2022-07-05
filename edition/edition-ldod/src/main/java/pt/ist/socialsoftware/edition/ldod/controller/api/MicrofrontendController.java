package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

@Controller
public class MicrofrontendController {

    @ModelAttribute("ldoDSession")
    public LdoDSession getLdoDSession() {
        return LdoDSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/microfrontend", "/microfrontend/*" })
    public String microfrontend() {
        return "microfrontend";
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/microfrontend-vite", "/microfrontend-vite/*" })
    public String microfrontendVite() {
        return "microfrontendVite";
    }



}
