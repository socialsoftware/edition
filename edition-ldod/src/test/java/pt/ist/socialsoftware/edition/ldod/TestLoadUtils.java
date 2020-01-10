package pt.ist.socialsoftware.edition.ldod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.feature.inout.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.text.feature.inout.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionsTEICorpusImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestLoadUtils {
    private static final Logger logger = LoggerFactory.getLogger(TestLoadUtils.class);

    public static void loadCorpus() throws FileNotFoundException {
        if (TextModule.getInstance().getExpertEditionsSet().isEmpty()) {
            String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
            File directory = new File(testFilesDirectory);
            final String filename = "corpus.xml";
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
        TextProvidesInterface.cleanFragmentMapCache();
        TextProvidesInterface.cleanScholarInterMapCache();
        VirtualProvidesInterface.cleanVirtualEditionInterMapByUrlIdCache();
        VirtualProvidesInterface.cleanVirtualEditionInterMapByXmlIdCache();
        VirtualProvidesInterface.cleanVirtualEditionMapCache();

        TextModule text = TextModule.getInstance();
        UserModule userModule = UserModule.getInstance();
        VirtualModule virtualModule = VirtualModule.getInstance();
        ClassificationModule classificationModule = ClassificationModule.getInstance();
        if (classificationModule != null) {
            classificationModule.getClassificationGameSet().forEach(classificationGame -> classificationGame.remove());
            classificationModule.getPlayerSet().stream().forEach(player -> player.remove());
        }
        if (userModule != null) {
            userModule.getUsersSet().stream()
                    .filter(u -> !(u.getUsername().equals(User.USER_ARS) || u.getUsername().equals(User.USER_TWITTER)))
                    .forEach(u -> u.remove());
            virtualModule.getCitationSet().forEach(c -> c.remove());
            userModule.getUserConnectionSet().forEach(uc -> uc.remove());
            userModule.getTokenSet().forEach(t -> t.remove());
            virtualModule.getVirtualEditionsSet().stream().filter(ve -> !ve.getAcronym().equals(VirtualEdition.ARCHIVE_EDITION_ACRONYM))
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
