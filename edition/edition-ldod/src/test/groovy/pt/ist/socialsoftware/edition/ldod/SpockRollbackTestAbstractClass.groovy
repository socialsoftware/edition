package pt.ist.socialsoftware.edition.ldod;

import pt.ist.fenixframework.FenixFramework
import pt.ist.fenixframework.core.WriteOnReadError
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager
import spock.lang.Specification

import pt.ist.socialsoftware.edition.ldod.domain.*
import pt.ist.socialsoftware.edition.ldod.loaders.*
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap

import javax.transaction.NotSupportedException
import javax.transaction.SystemException


abstract class SpockRollbackTestAbstractClass extends Specification {

	def setup() throws Exception {
		try {
			FenixFramework.getTransactionManager().begin(false)
			setUpDatabaseWithCorpus()
			populate4Test()
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace()
		}
	}

	def cleanup() {
		try {
			FenixFramework.getTransactionManager().rollback()
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace()
		}
	}

	abstract def populate4Test()
	
	def setUpDatabaseWithCorpus() {
		cleanDatabaseButCorpus();
		Bootstrap.initializeSystem();
		loadCorpus();
	}
	
	def loadCorpus() {
		if (LdoD.getInstance().getExpertEditionsSet().isEmpty()) {
			String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
			File directory = new File(testFilesDirectory);
			String filename = "corpus.xml";
			File file = new File(directory, filename);
			LoadTEICorpus corpusLoader = new LoadTEICorpus();
			corpusLoader.loadTEICorpus(new FileInputStream(file));
		}
	}

	def cleanDatabaseButCorpus() {
		LdoD ldoD = LdoD.getInstance();
		if (ldoD != null) {
			for (def user: ldoD.getUsersSet()) {
				if (!(user.getUsername().equals("ars") || user.getUsername().equals("Twitter"))) {
					user.remove();
				}
			}
			for (def frag: ldoD.getFragmentsSet()) {
				frag.remove();
			}
			for (def cit: ldoD.getCitationSet())
				cit.remove();
			}
			if (ldoD.getUserConnectionSet() != null) {
				for (def uc: ldoD.getUserConnectionSet() ) {
					uc.remove()
				}
			}
			for (def t: ldoD.getTokenSet()) {
				t.remove();
			}
			for (def ve: ldoD.getVirtualEditionsSet()) {
				if (!ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM)) {
					 ve.remove();
			}
			for (def t: ldoD.getTweetSet()) {
				t.remove();
			}
		}

	}

	def loadFragments(def fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {
		def testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
		def directory = new File(testFilesDirectory);

		def fragmentFiles = fragmentsToLoad;

		def file;
		for (def i = 0; i < fragmentFiles.length; i++) {
			file = new File(directory, fragmentFiles[i]);
			def fragmentLoader = new LoadTEIFragments();
			fragmentLoader.loadFragmentsAtOnce(new FileInputStream(file));
		}
	}

}
