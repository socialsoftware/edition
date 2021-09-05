package pt.ist.socialsoftware.edition.text.utils;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import org.apache.commons.io.FileUtils;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.text.domain.TextModule;
import pt.ist.socialsoftware.edition.text.feature.inout.LoadTEICorpus;
import pt.ist.socialsoftware.edition.text.feature.inout.LoadTEIFragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TextBootstrap {

    @Atomic(mode = TxMode.WRITE)
    public static boolean initializeTextModule() {
        boolean textCreate = false;
        if (TextModule.getInstance() == null) {
            new TextModule();
            cleanCorpusRepository();
            cleanIntersRepository();
            textCreate = true;
        }
        if (textCreate) {
        //    loadTextFromFile();
        }

        return textCreate;
    }

    public static void cleanCorpusRepository() {
        String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
        File directory = new File(corpusFilesPath);
        if (directory.exists()) {
            try {
//                FileUtils.deleteDirectory(directory);
                FileUtils.cleanDirectory(directory);
            } catch (IOException e) {
                throw new LdoDException(
                        "Bootstrap.populateDatabaseUsersAndRoles cannot delete directory for corpus.files.dir");
            }
        }
        directory.mkdirs();
    }

    public static void cleanIntersRepository() {
        String intersFilesPath = PropertiesManager.getProperties().getProperty("inters.dir");
        File directory = new File(intersFilesPath);
        if (directory.exists()) {
            try {
                FileUtils.cleanDirectory(directory);
//                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                throw new LdoDException(
                        "Bootstrap.populateDatabaseUsersAndRoles cannot delete directory for inters.dir");
            }
        }
        directory.mkdirs();
    }

    private static void loadTextFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "text");

        File corpus = new File(directory, "001.xml");

        if (!corpus.exists()) {
            return; // File does not exist but that is not a problem. Just move on
        }

        LoadTEICorpus loadTEICorpus = new LoadTEICorpus();
        try {
            loadTEICorpus.loadTEICorpus(new FileInputStream(corpus));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load text from file");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        LoadTEIFragments loadTEIFragments = new LoadTEIFragments();
        for (File file : files) {
            try {
                loadTEIFragments.loadFragmentsStepByStep(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new LdoDException("Failed to load virtual fragment");
            }
        }
    }

}
