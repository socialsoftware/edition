package pt.ist.socialsoftware.edition.ldod.frontend.utils.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LdoDErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public ModelAndView error() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/errorPage");
        modelAndView.addObject("message", "pagenotfound.message");
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}