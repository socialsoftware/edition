package pt.ist.socialsoftware.edition.ldod.frontend.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.socialsoftware.edition.ldod.frontend.session.FrontendSession;

@Controller
public class GameFrontendController {
    private static final Logger logger = LoggerFactory.getLogger(GameFrontendController.class);

    @ModelAttribute("ldoDSession")
    public FrontendSession getLdoDSession() {
        return FrontendSession.getLdoDSession();
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/classification-game", "/classification-game/*"})
    public String classificationGameClient(Model model) {
        return "classificationGame";
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/classificationGames"})
    public String classicationGamePage(Model model) {
        return "virtual/classificationGames";
    }

}