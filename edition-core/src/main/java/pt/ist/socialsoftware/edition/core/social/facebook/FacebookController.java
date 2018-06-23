package pt.ist.socialsoftware.edition.core.social.facebook;
import javax.inject.Inject;
import javax.security.auth.login.Configuration;

import facebook4j.*;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.domain.UserConnection;
import pt.ist.socialsoftware.edition.core.security.LdoDConnectionRepository;


@Controller
@RequestMapping(value="/social/facebook")
public class FacebookController {
    private static Logger logger = LoggerFactory.getLogger(FacebookController.class);
    private Facebook facebook ;
    private String accessToken;

    private void setUp(){
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        facebook = new FacebookFactory().getInstance();
        facebook.setOAuthPermissions("read_stream");
        for(UserConnection uc: LdoD.getInstance().getUserConnectionSet()){
            if(uc.getUserId().equals(user.getUsername())){
                accessToken = uc.getAccessToken();
            }
        }
        facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
        try {
            ResponseList<Post> feed = facebook.getPosts();
            for(Post p: feed){
                logger.debug("TESTE" + p.getMessage());
            }
        } catch (FacebookException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value="/user", method=RequestMethod.POST)
    public String getFacebookUser(Model model, @RequestParam("message") String message) {
        logger.debug("getFBUser");
        setUp();
        try {
            facebook.postStatusMessage(message);
            for(Post p : facebook.getFeed()){
                logger.debug("AQUI----" + p.getMessage());
            }
        } catch (FacebookException e) {
            e.printStackTrace();
        }

        //facebook.feedOperations().post(uID, message);
        return "/social/facebook/success";
    }


    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showFeed(Model model) throws FacebookException {
        setUp();
        //facebook.fee
        //model.addAttribute("feed", fb.feedOperations().getFeed());
        return "social/facebook/home";
    }


   /*@RequestMapping(value="/user", method=RequestMethod.POST)
    public String getFacebookUser(Model model, @RequestParam("message") String message) {
        logger.debug("getFBUser");
        user = facebook.userOperations().getUserProfile();
        String uID = user.getId();
        logger.debug(user.getName());
        //facebook.feedOperations().post(uID, message);
        return "/social/facebook/success";
    }*/

   /* @RequestMapping(value="/home", method=RequestMethod.GET)
    public String showFeed(Model model) {
        model.addAttribute("feed", fb.feedOperations().getFeed());
        return "social/facebook/home";
    }*/

   /* @RequestMapping(value="/feed", method=RequestMethod.POST)
    public String postUpdate(String message) {
        facebook.feedOperations().updateStatus(message);
        return "redirect:/facebook/home";
    }*/


}
