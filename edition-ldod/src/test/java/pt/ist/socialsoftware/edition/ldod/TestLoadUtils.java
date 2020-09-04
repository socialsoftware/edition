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

        String[] fragmentFiles = fragmentsToLoad;

        LoadTEIFragments fragmentLoader = new LoadTEIFragments();

        File file;
        for (int i = 0; i < fragmentFiles.length; i++) {
            file = new File(directory, fragmentFiles[i]);
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
        TestLoadUtils.cleanDatabase();
        Bootstrap.initializeSystem();
        TestLoadUtils.loadCorpus();
    }

    public static void cleanDatabase() {
        TextProvidesInterface.cleanFragmentMapCache();
        TextProvidesInterface.cleanScholarInterMapCache();
        VirtualProvidesInterface.cleanVirtualEditionInterMapByUrlIdCache();
        VirtualProvidesInterface.cleanVirtualEditionInterMapByXmlIdCache();
        VirtualProvidesInterface.cleanVirtualEditionMapCache();

        TextModule textModule = TextModule.getInstance();
        if (textModule != null) {
            textModule.remove();
        }

        UserModule userModule = UserModule.getInstance();
        VirtualModule virtualModule = VirtualModule.getInstance();
        if (userModule != null) {
            userModule.remove();

            virtualModule.remove();
        }

        RecommendationModule recommendationModule = RecommendationModule.getInstance();
        if (recommendationModule != null) {
            recommendationModule.remove();
        }

        ClassificationModule classificationModule = ClassificationModule.getInstance();
        if (classificationModule != null) {
            classificationModule.remove();
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
