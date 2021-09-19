package pt.ist.socialsoftware.edition.ldod.visual;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;


import javax.servlet.http.HttpServletRequest;

//@EnableWebMvc
@ControllerAdvice
public class LdoDExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(LdoDExceptionHandler.class);


    @ExceptionHandler({LdoDLoadException.class})
    public ModelAndView handleLdoDLoadException(LdoDLoadException ex) {

        logger.debug("LdoDLoadException: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/notOkMessage");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }


    @ExceptionHandler({AccessDeniedException.class})
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {

        logger.debug("AccessDeniedException: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/errorPage");
        modelAndView.addObject("message", "general.access.denied");
        return modelAndView;
    }

    @ExceptionHandler({LdoDException.class})
    public ModelAndView handleLdoException(LdoDException ex) {

        logger.debug("LdoDException: {}", ExceptionUtils.getStackTrace(ex));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/notOkMessage");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception ex) {

        logger.error("Exception: {}", ExceptionUtils.getStackTrace(ex));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/notOkMessage");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleAnyException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/notOkMessage");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

}
