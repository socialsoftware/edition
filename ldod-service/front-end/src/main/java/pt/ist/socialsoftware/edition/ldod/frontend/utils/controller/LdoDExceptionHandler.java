package pt.ist.socialsoftware.edition.ldod.frontend.utils.controller;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.security.UserModuleUserDetails;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.*;


import javax.servlet.http.HttpServletRequest;

//@EnableWebMvc
@ControllerAdvice
public class LdoDExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(LdoDExceptionHandler.class);

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

    @ExceptionHandler({LdoDLoadException.class})
    public ModelAndView handleLdoDLoadException(LdoDLoadException ex) {

        logger.debug("LdoDLoadException: {}", ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("utils/notOkMessage");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({LdoDEditVirtualEditionException.class})
    public ModelAndView handleLdoDEditVirtualEditionException(LdoDEditVirtualEditionException ex) {

        logger.debug("LdoDEditVirtualEditionException: {}", ex.getErrors());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errors", ex.getErrors());
        modelAndView.addObject("virtualEdition", ex.getVirtualEdition());
        modelAndView.addObject("user", UserModuleUserDetails.getAuthenticatedUser().getUsername());
        modelAndView.setViewName("virtual/manage");
        return modelAndView;
    }

    @ExceptionHandler({LdoDCreateVirtualEditionException.class})
    public ModelAndView handleLdoDCreateVirtualEditionException(LdoDCreateVirtualEditionException ex) {

        logger.debug("LdoDCreateVirtualEditionException: {}", ex.getErrors());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errors", ex.getErrors());
        modelAndView.addObject("acronym", ex.getAcronym());
        modelAndView.addObject("title", ex.getTitle());
        modelAndView.addObject("pub", ex.isPub());
        modelAndView.addObject("virtualEditions", ex.getVirtualEditions());
        modelAndView.addObject("expertEditions", this.feTextRequiresInterface.getSortedExpertEditionsDto());
        modelAndView.addObject("user", ex.getUser());
        modelAndView.setViewName("virtual/editions");

        return modelAndView;
    }

    @ExceptionHandler({LdoDCreateClassificationGameException.class})
    public ModelAndView handleLdoDCreateClassificationGameException(LdoDCreateClassificationGameException ex) {

        logger.debug("LdoDCreateClassificationGameException: {}", ex.getErrors());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errors", ex.getErrors());
        modelAndView.addObject("description", ex.getDescription());
        modelAndView.addObject("interExternalId", ex.getInterExternalId());
        modelAndView.addObject("date", ex.getDate());
        modelAndView.addObject("virtualEdition", ex.getVirtualEdition());
        modelAndView.setViewName("virtual/createClassificationGame");

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
