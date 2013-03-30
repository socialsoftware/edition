package pt.ist.socialsoftware.edition.controller;

import jvstm.Transaction;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

@ControllerAdvice
public class LdoDExceptionHandler {

	@ExceptionHandler({ LdoDException.class })
	public ModelAndView handleLdoDException(LdoDException ex) {

		// the transaction is open in TransactionFilter
		Transaction.abort();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView handleException(Exception ex) {

		// the transaction is open in TransactionFilter
		Transaction.abort();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

}
