package pt.ist.socialsoftware.edition.ldod.visual;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.api.VisualRequiresInterface;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class TestLoadUtils {
    private static final Logger logger = LoggerFactory.getLogger(TestLoadUtils.class);

    public static void loadCorpus() throws FileNotFoundException {
        VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();
        if (visualRequiresInterface.getSortedHeteronyms().isEmpty()) {
            String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
            File directory = new File(testFilesDirectory);
            final String filename = "corpus.xml";
            File file = new File(directory, filename);
            visualRequiresInterface.getLoaderTEICorpus(new FileInputStream(file));
            visualRequiresInterface.loadTEICorpusVirtual(new FileInputStream(file));

        }
    }


    public static void loadFragments(String[] fragmentsToLoad) throws LdoDLoadException, FileNotFoundException {

        String testFilesDirectory = PropertiesManager.getProperties().getProperty("test.files.dir");
        File directory = new File(testFilesDirectory);


        String[] fragmentFiles = fragmentsToLoad;

        VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();

        File file;
        for (int i = 0; i < fragmentFiles.length; i++) {
            file = new File(directory, fragmentFiles[i]);
            visualRequiresInterface.getLoadTEIFragmentsAtOnce(new FileInputStream(file));
        }
        if (fragmentFiles.length > 0){  visualRequiresInterface.getFragmentCorpusGenerator(); }
    }


    public static void setUpDatabaseWithCorpus() throws FileNotFoundException {
        TestLoadUtils.cleanDatabase();
        initializeSystem();
        TestLoadUtils.loadCorpus();
    }

    public static void cleanDatabase() {

        VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();

        visualRequiresInterface.cleanFragmentMapCache();
        visualRequiresInterface.cleanScholarInterMapCache();
        visualRequiresInterface.cleanVirtualEditionInterMapByUrlIdCache();
        visualRequiresInterface.cleanVirtualEditionInterMapByXmlIdCache();
        visualRequiresInterface.cleanVirtualEditionMapCache();


       visualRequiresInterface.removeTextModule();

//       feUserRequiresInterface.removeUserModule();

        visualRequiresInterface.removeVirtualModule();

//        new FeReadingRequiresInterface().removeRecommendationModule();
//
//        new FeGameRequiresInterface().removeGameModule();
    }

    public static byte[] jsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static void initializeSystem() {

        VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();

        visualRequiresInterface.initializeTextModule();

//        userCreate = new FeUserRequiresInterface().initializeUserModule();

        visualRequiresInterface.initializeVirtualModule();

//        new FeReadingRequiresInterface().initializeRecommendationModule();

//        new FeGameRequiresInterface().initializeGameModule();

    }

}
