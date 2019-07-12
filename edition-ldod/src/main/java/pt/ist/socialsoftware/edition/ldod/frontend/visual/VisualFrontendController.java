package pt.ist.socialsoftware.edition.ldod.frontend.visual;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

@Controller
public class VisualFrontendController {

    @ModelAttribute("ldoDSession")
    public LdoDSession getLdoDSession() {
        return LdoDSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/ldod-visual", "/ldod-visual/*"})
    public String bookVisual() {
        return "ldodVisual";
    }

}
