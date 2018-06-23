package pt.ist.socialsoftware.edition.ldod.services;

import jvstm.Atomic;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

public abstract class LdoDService {
	/**
	 * Guarantees the atomic execution of execution() method
	 * 
	 * @throws LdoDException
	 *             expresses domain specific errors
	 */
	@Atomic
	public void atomicExecution() throws LdoDException {
		execution();
	}

	/**
	 * Contains the service code
	 * 
	 * @throws LdoDException
	 */
	abstract void execution() throws LdoDException;

}
