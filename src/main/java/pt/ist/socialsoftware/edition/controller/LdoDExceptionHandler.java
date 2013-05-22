package pt.ist.socialsoftware.edition.controller;

import jvstm.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

@ControllerAdvice
public class LdoDExceptionHandler {
	private final static Logger logger = LoggerFactory
			.getLogger(LdoDExceptionHandler.class);

	@ExceptionHandler({ LdoDLoadException.class })
	public ModelAndView handleLdoDLoadException(LdoDLoadException ex) {

		if (logger.isDebugEnabled()) {
			logger.debug("LdoDLoadException: {}", ex.getMessage());
		}

		Transaction.abort();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView handleException(Exception ex) {

		if (logger.isDebugEnabled()) {
			logger.error("Exception: {}", ex.getMessage(), ex);
		}

		Transaction.abort();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

}
