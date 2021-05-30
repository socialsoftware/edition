package pt.ist.socialsoftware.edition.ldod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.frontend.game.FeGameRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.reading.FeReadingRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.SessionRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.Bootstrap;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;




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
            new FeVirtualRequiresInterface().loadTEICorpusVirtual(new FileInputStream(file));

        }
    }

	public static void loadVirtualEditionsCorpus() throws FileNotFoundException {
		File directory = new File(PropertiesManager.getProperties().getProperty("test.files.dir"));

		File corpus = new File(directory, "virtual-corpus.xml");
		FileInputStream fis1 = new FileInputStream(corpus);

        new FeVirtualRequiresInterface().importVirtualEditionCorupus(fis1);
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

	public static void loadVirtualEditionFragments(String[] fragmentsToLoad) throws LdoDLoadException, IOException {
		String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
		File directory = new File(testFilesDirectory);

		String[] fragmentFiles = fragmentsToLoad;

		File file;
		for (int i = 0; i < fragmentFiles.length; i++) {
			file = new File(directory, fragmentFiles[i]);
			new FeVirtualRequiresInterface().importVirtualEditionFragmentFromTEI((file));
			new FeGameRequiresInterface().importGamesFromTEI(new FileInputStream(file));
		}
	}

    public static void setUpDatabaseWithCorpus() throws FileNotFoundException {
        TestLoadUtils.cleanDatabase();
        Bootstrap.initializeSystem();
        TestLoadUtils.loadCorpus();
    }

    public static void cleanDatabase() {
        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
        FeUserRequiresInterface feUserRequiresInterface = new FeUserRequiresInterface();
        FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

        feTextRequiresInterface.cleanFragmentMapCache();
        feTextRequiresInterface.cleanScholarInterMapCache();
        feVirtualRequiresInterface.cleanVirtualEditionInterMapByUrlIdCache();
        feVirtualRequiresInterface.cleanVirtualEditionInterMapByXmlIdCache();
        feVirtualRequiresInterface.cleanVirtualEditionMapCache();
        

        EventInterface.getInstance();
        SessionRequiresInterface.getInstance();

       feTextRequiresInterface.removeTextModule();

       feUserRequiresInterface.removeUserModule();

        feVirtualRequiresInterface.removeVirtualModule();

        new FeReadingRequiresInterface().removeRecommendationModule();

        new FeGameRequiresInterface().removeGameModule();
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

        FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();

        feVirtualRequiresInterface.importVirtualEditionCorupus(fis1);

        File frag1 = new File(directory, "virtual-Fr001.xml");
        FileInputStream fisfrag = new FileInputStream(frag1);



        feVirtualRequiresInterface.importFragmentFromTEI(fisfrag);

        new FeGameRequiresInterface().importGamesFromTEI(new FileInputStream(frag1));

    }

    public static void deleteTestVirtualEdition() {
        new FeVirtualRequiresInterface().getVirtualEditionByAcronym("LdoD-Teste").removeByExternalId();
    }

}
