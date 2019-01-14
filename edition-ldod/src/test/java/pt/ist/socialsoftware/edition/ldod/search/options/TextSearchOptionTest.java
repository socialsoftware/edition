package pt.ist.socialsoftware.edition.ldod.search.options;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;

public class TextSearchOptionTest extends TestWithFragmentsLoading {

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = { "001.xml" };

		return fragments;
	}

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
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
