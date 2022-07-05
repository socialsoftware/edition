package pt.ist.socialsoftware.edition.ldod.search.options;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;

public class TextSearchOptionTest {

	@BeforeAll
	@Atomic(mode = TxMode.WRITE)
	public static void setUpAll() throws FileNotFoundException {
		TestLoadUtils.setUpDatabaseWithCorpus();

		String[] fragments = { "001.xml" };
		TestLoadUtils.loadFragments(fragments);
	}

	@AfterAll
	@Atomic(mode = TxMode.WRITE)
	public static void tearDownAll() throws FileNotFoundException {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Test
	@Atomic()
	public void test() {
		String[] words = { "artista", "arte", "sonho" };

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

}
