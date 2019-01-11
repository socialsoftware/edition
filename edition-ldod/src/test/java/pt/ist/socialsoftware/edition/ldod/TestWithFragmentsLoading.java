package pt.ist.socialsoftware.edition.ldod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public abstract class TestWithFragmentsLoading {
	@BeforeEach
	public void setUp() throws FileNotFoundException {
		loadFiles();
	}

	@Atomic(mode = TxMode.WRITE)
	private void loadFiles() throws FileNotFoundException {
		// clean everything
		if (LdoD.getInstance() != null) {
			LdoD.getInstance().remove();
		}

		Bootstrap.initializeSystem();

		String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");

		// load corpus
		File directory = new File(testFilesDirectory);
		String filename = "corpus.xml";
		File file = new File(directory, filename);
		LoadTEICorpus corpusLoader = new LoadTEICorpus();
		corpusLoader.loadTEICorpus(new FileInputStream(file));

		// load fragments
		String[] fragmentFiles = fragmentsToLoad4Test();

		for (int i = 0; i < fragmentFiles.length; i++) {
			file = new File(directory, fragmentFiles[i]);
			LoadTEIFragments fragmentLoader = new LoadTEIFragments();
			fragmentLoader.loadFragmentsAtOnce(new FileInputStream(file));
		}
	}

	protected abstract String[] fragmentsToLoad4Test();

	@AfterEach
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		// clean everything
		if (LdoD.getInstance() != null) {
			LdoD.getInstance().remove();
		}
	}

}
