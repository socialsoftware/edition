package pt.ist.socialsoftware.edition.social.facebook;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ist.socialsoftware.edition.controller.AdminController;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.session.LdoDSession;

@Controller
@RequestMapping(value="/social/facebook")
public class FacebookController {
    private static Logger logger = LoggerFactory.getLogger(FacebookController.class);

    private static final String FACEBOOK = "facebook";
    private static final String REDIRECT_LOGIN = "redirect:/signin";

    @Inject
    private Facebook fb;
    private User user;

    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showHome(Model model) {
        logger.debug("FB home");
        return "social/facebook/home";
    }

    @RequestMapping(value="/user", method=RequestMethod.POST)
    public String getFacebookUser(Model model, @RequestParam("id") String id) {
        logger.debug("getFBUser");
        logger.debug(id);
        //user = fb.userOperations().getUserProfile();
        return "redirect:/social/facebook/home";
    }

   /* @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showFeed(Model model) {
        model.addAttribute("feed", fb.feedOperations().getFeed());
        return "social/facebook/home";
    }*/

    @RequestMapping(value="/feed", method=RequestMethod.POST)
    public String postUpdate(String message) {
        fb.feedOperations().updateStatus(message);
        return "redirect:/facebook/home";
    }


}
