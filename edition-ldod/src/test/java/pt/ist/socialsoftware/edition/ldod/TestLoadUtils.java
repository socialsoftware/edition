package pt.ist.socialsoftware.edition.ldod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class TestLoadUtils {
	public static void loadCorpus() throws FileNotFoundException {
		String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
		File directory = new File(testFilesDirectory);
		String filename = "corpus.xml";
		File file = new File(directory, filename);
		LoadTEICorpus corpusLoader = new LoadTEICorpus();
		corpusLoader.loadTEICorpus(new FileInputStream(file));
	}

	public static void loadFragments(String[] fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {
		String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
		File directory = new File(testFilesDirectory);

		String[] fragmentFiles = fragmentsToLoad;

		File file;
		for (int i = 0; i < fragmentFiles.length; i++) {
			file = new File(directory, fragmentFiles[i]);
			LoadTEIFragments fragmentLoader = new LoadTEIFragments();
			fragmentLoader.loadFragmentsAtOnce(new FileInputStream(file));
		}
	}

	public static void cleanDatabase() {
		if (LdoD.getInstance() != null) {
			LdoD.getInstance().remove();
		}

	}
}
