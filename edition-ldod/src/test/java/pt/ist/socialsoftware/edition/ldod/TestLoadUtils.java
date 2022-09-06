package pt.ist.socialsoftware.edition.ldod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
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
        if (LdoD.getInstance().getExpertEditionsSet().isEmpty()) {
            String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
            File directory = new File(testFilesDirectory);
            String filename = "corpus.xml";
            File file = new File(directory, filename);
            LoadTEICorpus corpusLoader = new LoadTEICorpus();
            corpusLoader.loadTEICorpus(new FileInputStream(file));
        }
    }

    public static void loadVirtualEditionsCorpus() throws FileNotFoundException {
        File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

        File corpus = new File(directory, "virtual-corpus.xml");
        FileInputStream fis1 = new FileInputStream(corpus);

        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();

        loader.importVirtualEditionsCorpus(fis1);
    }

    public static void loadFragments(String[] fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {
        String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
        File directory = new File(testFilesDirectory);
        LoadTEIFragments fragmentLoader = new LoadTEIFragments();

        File file;
        for (String s : fragmentsToLoad) {
            file = new File(directory, s);
            fragmentLoader.loadFragmentsAtOnce(new FileInputStream(file));
        }
    }

    public static void loadVirtualEditionFragments(String[] fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {
        String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
        File directory = new File(testFilesDirectory);

        String[] fragmentFiles = fragmentsToLoad;

        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();

        File file;
        for (int i = 0; i < fragmentFiles.length; i++) {
            file = new File(directory, fragmentFiles[i]);
            loader.importFragmentFromTEI(new FileInputStream(file));
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
            ldoD.getVirtualEditionsSet().stream()
                    .filter(ve -> !ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
                    .forEach(VirtualEdition::remove);
            ldoD.getVirtualEditionsSet().stream()
                    .filter(ve -> ve.getAcronym().equals(Edition.ARCHIVE_EDITION_ACRONYM))
                    .flatMap(ve -> ve.getTaxonomy().getCategoriesSet().stream())
                    .forEach(Category::remove);
            ldoD.getTweetSet().forEach(t -> t.remove());
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
}
