package pt.ist.socialsoftware.edition.text.utils;

import org.apache.commons.io.FileUtils;
import pt.ist.socialsoftware.edition.text.domain.TextModule;

import java.io.File;
import java.io.IOException;

public class TextBootstrap {

    public static boolean initializeTextModule() {
        boolean textCreate = false;
        if (TextModule.getInstance() == null) {
            new TextModule();
            cleanCorpusRepository();
            cleanIntersRepository();
            textCreate = true;
        }
        return textCreate;
    }

    public static void cleanCorpusRepository() {
        String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
        File directory = new File(corpusFilesPath);
        if (directory.exists()) {
            try {
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
            } catch (IOException e) {
                throw new LdoDException(
                        "Bootstrap.populateDatabaseUsersAndRoles cannot delete directory for inters.dir");
            }
        }
        directory.mkdirs();
    }

}
