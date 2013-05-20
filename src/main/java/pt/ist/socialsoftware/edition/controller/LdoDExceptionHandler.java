package pt.ist.socialsoftware.edition.controller;

import jvstm.Transaction;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateValueException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

@ControllerAdvice
public class LdoDExceptionHandler {

	@ExceptionHandler({ LdoDLoadException.class })
	public ModelAndView handleLdoDLoadException(LdoDLoadException ex) {

		// the transaction is open in TransactionFilter
		Transaction.abort();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	@ExceptionHandler({ LdoDDuplicateValueException.class })
	public ModelAndView handleLdoDDuplicateValueException(
			LdoDDuplicateValueException ex) {
		System.out.println("handleLdoDDuplicateValueException");
		// the transaction is open in TransactionFilter
		Transaction.abort();

		throw ex;
	}

	// @ExceptionHandler({ Exception.class })
	// public ModelAndView handleException(Exception ex) {
	//
	// // the transaction is open in TransactionFilter
	// Transaction.abort();
	//
	// ModelAndView modelAndView = new ModelAndView();
	// modelAndView.setViewName("ldoDExceptionPage");
	// modelAndView.addObject("message", ex.getMessage());
	// return modelAndView;
	// }

}
