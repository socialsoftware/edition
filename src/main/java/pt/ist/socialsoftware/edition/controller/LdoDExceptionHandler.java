package pt.ist.socialsoftware.edition.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
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

		// try {
		// System.out.println("Vai fazer roolback");
		// FenixFramework.getTransactionManager().getTransaction().rollback();
		// System.out.println("Fez roolback");
		// } catch (IllegalStateException | SecurityException | SystemException
		// e) {
		// System.out.println("handleLdoDLoadException");
		// e.printStackTrace();
		// }

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {

		if (logger.isDebugEnabled()) {
			logger.debug("AccessDeniedException: {}", ex.getMessage());
		}

		// try {
		// FenixFramework.getTransactionManager().rollback();
		// } catch (IllegalStateException | SecurityException | SystemException
		// e) {
		// System.out.println("handleAccessDeniedException");
		// e.printStackTrace();
		// }

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/ldoDExceptionPage");
		modelAndView.addObject("i18n", true);
		modelAndView.addObject("message", "general.access.denied");
		return modelAndView;
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView handleException(Exception ex) {

		if (logger.isDebugEnabled()) {
			logger.error("Exception: {}", ex.getMessage(), ex);
		}

		// try {
		// FenixFramework.getTransactionManager().rollback();
		// } catch (IllegalStateException | SecurityException | SystemException
		// e) {
		// System.out.println("handleException");
		// e.printStackTrace();
		// }

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

}
