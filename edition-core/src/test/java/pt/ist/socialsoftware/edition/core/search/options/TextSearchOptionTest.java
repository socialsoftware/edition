package pt.ist.socialsoftware.edition.core.search.options;

import static org.junit.Assert.assertEquals;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

public class TextSearchOptionTest {

	@Before
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	@Test
	@Atomic()
	public void test() {
		String[] words = { "arte", "vida", "terra", "alma", "traz" };

		for (int i = 0; i < words.length; i++) {
			int size = 0;
			int oldSize = 0;
			for (int j = 0; j < 5; j++) {
				TextSearchOption search = new TextSearchOption(words[i]);
				size = search.search().size();
				if (j != 0) {
					assertEquals(oldSize, size);
				}
				oldSize = size;
			}
		}
	}

	@After
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

}
