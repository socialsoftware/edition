package pt.ist.socialsoftware.edition.ldod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class TestLoadUtils {
	public static void loadCorpus() throws FileNotFoundException {
		if (LdoD.getInstance().getExpertEditionsSet().isEmpty()) {
			String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
			File directory = new File(testFilesDirectory);
			String filename = "corpus.xml";
			File file = new File(directory, filename);
			LoadTEICorpus corpusLoader = new LoadTEICorpus();
			corpusLoader.loadTEICorpus(new FileInputStream(file));
		}
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

	public static void setUpDatabaseWithCorpus() throws FileNotFoundException {
		TestLoadUtils.cleanDatabaseButCorpus();
		Bootstrap.initializeSystem();
		TestLoadUtils.loadCorpus();
	}

	public static void cleanDatabaseButCorpus() {
		LdoD ldoD = LdoD.getInstance();
		if (ldoD != null) {
			ldoD.getUsersSet().stream()
					.filter(u -> !(u.getUsername().equals("ars") || u.getUsername().equals("Twitter")))
					.forEach(u -> u.remove());
			ldoD.getFragmentsSet().forEach(f -> f.remove());
			ldoD.getCitationSet().forEach(c -> c.remove());
			ldoD.getUserConnectionSet().forEach(uc -> uc.remove());
			ldoD.getTokenSet().forEach(t -> t.remove());
			ldoD.getVirtualEditionsSet().stream().filter(ve -> !ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
					.forEach(ve -> ve.remove());
			ldoD.getTweetSet().forEach(t -> t.remove());
		}

	}
}
