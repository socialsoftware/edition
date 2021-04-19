package pt.ist.socialsoftware.edition.ldod;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

public abstract class RollbackCaseTest {

	@BeforeEach
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
		populate4Test();
	}

	@AfterEach
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

	public abstract void populate4Test();

}
