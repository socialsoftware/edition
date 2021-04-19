package pt.ist.socialsoftware.edition.ldod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;
import pt.ist.socialsoftware.edition.game.domain.ClassificationModule;
import pt.ist.socialsoftware.edition.game.feature.classification.inout.GameXMLImport;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.SessionRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.recommendation.domain.RecommendationModule;

import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;

import pt.ist.socialsoftware.edition.user.domain.UserModule;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionsTEICorpusImport;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Collectors;

public class TestLoadUtils {
    private static final Logger logger = LoggerFactory.getLogger(TestLoadUtils.class);

    public static void loadCorpus() throws FileNotFoundException {
        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
        if (feTextRequiresInterface.getSortedHeteronyms().isEmpty()) {
            String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
            File directory = new File(testFilesDirectory);
            final String filename = "corpus.xml";
            File file = new File(directory, filename);
//            LoadTEICorpus corpusLoader1 = new LoadTEICorpus();
//            corpusLoader1.loadTEICorpus(new FileInputStream(file));
            feTextRequiresInterface.getLoaderTEICorpus(new FileInputStream(file));
            VirtualEditionsTEICorpusImport corpusLoader = new VirtualEditionsTEICorpusImport();
            corpusLoader.loadTEICorpusVirtual(new FileInputStream(file));

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

//        LoadTEIFragments fragmentLoader = new LoadTEIFragments();
        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

        File file;
        for (int i = 0; i < fragmentFiles.length; i++) {
            file = new File(directory, fragmentFiles[i]);
            feTextRequiresInterface.getLoadTEIFragmentsAtOnce(new FileInputStream(file));
        }
        if (fragmentFiles.length > 0){  new FeTextRequiresInterface().getFragmentCorpusGenerator(); }
    }

	public static void loadVirtualEditionFragments(String[] fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {
		String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
		File directory = new File(testFilesDirectory);

		String[] fragmentFiles = fragmentsToLoad;

		VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();
        GameXMLImport gameloader = new GameXMLImport();

		File file;
		for (int i = 0; i < fragmentFiles.length; i++) {
			file = new File(directory, fragmentFiles[i]);
			loader.importFragmentFromTEI(new FileInputStream(file));
			gameloader.importGamesFromTEI(new FileInputStream(file));
		}
	}

    public static void setUpDatabaseWithCorpus() throws FileNotFoundException {
        TestLoadUtils.cleanDatabase();
        Bootstrap.initializeSystem();
        TestLoadUtils.loadCorpus();
    }

    public static void cleanDatabase() {
        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

        feTextRequiresInterface.cleanFragmentMapCache();
        feTextRequiresInterface.cleanScholarInterMapCache();
        VirtualProvidesInterface.cleanVirtualEditionInterMapByUrlIdCache();
        VirtualProvidesInterface.cleanVirtualEditionInterMapByXmlIdCache();
        VirtualProvidesInterface.cleanVirtualEditionMapCache();
        

        EventInterface.getInstance();
        VirtualRequiresInterface.getInstance();
        SessionRequiresInterface.getInstance();
        GameRequiresInterface.getInstance();
        RecommendationRequiresInterface.getInstance();

       feTextRequiresInterface.removeTextModule();

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

        GameXMLImport gameloader = new GameXMLImport();
        gameloader.importGamesFromTEI(new FileInputStream(frag1));

    }

    public static void deleteTestVirtualEdition() {
        VirtualModule.getInstance().getVirtualEdition("LdoD-Teste").remove();
    }

}
