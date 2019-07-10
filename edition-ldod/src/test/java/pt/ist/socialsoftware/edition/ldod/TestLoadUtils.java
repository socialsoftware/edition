package pt.ist.socialsoftware.edition.ldod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestLoadUtils {
    public static void loadCorpus() throws FileNotFoundException {
        if (TextModule.getInstance().getExpertEditionsSet().isEmpty()) {
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
        TextModule text = TextModule.getInstance();
        UserModule userModule = UserModule.getInstance();
        VirtualModule virtualModule = VirtualModule.getInstance();
        if (userModule != null) {
            userModule.getUsersSet().stream()
                    .filter(u -> !(u.getUsername().equals(User.USER_ARS) || u.getUsername().equals(User.USER_TWITTER)))
                    .forEach(u -> u.remove());
            virtualModule.getCitationSet().forEach(c -> c.remove());
            userModule.getUserConnectionSet().forEach(uc -> uc.remove());
            userModule.getTokenSet().forEach(t -> t.remove());
            virtualModule.getVirtualEditionsSet().stream().filter(ve -> !ve.getAcronym().equals(ExpertEdition.ARCHIVE_EDITION_ACRONYM))
                    .forEach(ve -> ve.remove());
            virtualModule.getTweetSet().forEach(t -> t.remove());
        }
        if (text != null) {
            text.getFragmentsSet().forEach(f -> f.remove());
        }
    }

    public static byte[] jsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static void loadTestVirtualEdition() throws FileNotFoundException {

        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));
        File corpus = new File(directory, "virtual-corpus.xml");
        FileInputStream fis1 = new FileInputStream(corpus);

        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();

        loader.importVirtualEditionsCorpus(fis1);

        File frag1 = new File(directory, "virtual-Fr001.xml");
        FileInputStream fisfrag = new FileInputStream(frag1);

        VirtualEditionFragmentsTEIImport fragloader = new VirtualEditionFragmentsTEIImport();

        fragloader.importFragmentFromTEI(fisfrag);

    }

    public static void deleteTestVirtualEdition() {
        VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").remove();
    }
}
