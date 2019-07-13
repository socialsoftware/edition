package pt.ist.socialsoftware.edition.ldod

import pt.ist.fenixframework.FenixFramework
import pt.ist.fenixframework.core.WriteOnReadError
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition
import pt.ist.socialsoftware.edition.ldod.domain.TextModule
import pt.ist.socialsoftware.edition.ldod.domain.UserModule
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule
import pt.ist.socialsoftware.edition.ldod.text.feature.inout.LoadTEICorpus
import pt.ist.socialsoftware.edition.ldod.text.feature.inout.LoadTEIFragments
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDLoadException
import spock.lang.Specification

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
        if (TextModule.getInstance().getExpertEditionsSet().isEmpty()) {
            String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
            File directory = new File(testFilesDirectory);
            String filename = "corpus.xml";
            File file = new File(directory, filename);
            LoadTEICorpus corpusLoader = new LoadTEICorpus();
            corpusLoader.loadTEICorpus(new FileInputStream(file));
        }
    }

    def cleanDatabaseButCorpus() {
        TextModule text = TextModule.getInstance()
        UserModule userModule = UserModule.getInstance();
        VirtualModule ldoD = VirtualModule.getInstance();
        if (ldoD != null) {
            for (def user : userModule.getUsersSet()) {
                if (!(user.getUsername().equals("ars") || user.getUsername().equals("Twitter"))) {
                    user.remove();
                }
            }
            for (def frag : text.getFragmentsSet()) {
                frag.remove();
            }
            for (def cit : ldoD.getCitationSet())
                cit.remove();
        }
        for (def uc :
                userModule.getUserConnectionSet()) {
            uc.remove()
        }
        for (def t : userModule.getTokenSet()) {
            t.remove();
        }
        for (def ve : ldoD.getVirtualEditionsSet()) {
            if (!ve.getAcronym().equals(ExpertEdition.ARCHIVE_EDITION_ACRONYM)) {
                ve.remove();
            }
            for (def t : ldoD.getTweetSet()) {
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
