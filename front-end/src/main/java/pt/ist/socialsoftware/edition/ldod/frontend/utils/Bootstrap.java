package pt.ist.socialsoftware.edition.ldod.frontend.utils;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.game.api.GameRequiresInterface;

import pt.ist.socialsoftware.edition.ldod.frontend.domain.EditionModule;
import pt.ist.socialsoftware.edition.ldod.frontend.domain.Menu;
import pt.ist.socialsoftware.edition.ldod.frontend.domain.Option;
import pt.ist.socialsoftware.edition.ldod.frontend.domain.UiComponent;
import pt.ist.socialsoftware.edition.ldod.frontend.game.FeGameRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.session.SessionRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationProvidesInterface;
import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;

import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;

import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;



import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ars
 */
public class Bootstrap implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private static final String ARS = "ars";

    /**
     * It is invoked from spring mvc user interface
     */
    @Override
    public void onStartup(ServletContext arg0) throws ServletException {
        initializeSystem();
        new RecommendationProvidesInterface().loadRecommendationCache();
    }

    @Atomic(mode = TxMode.WRITE)
    public static void initializeSystem() {
        // clean existing module info.
        FenixFramework.getDomainRoot().getModuleSet().forEach(EditionModule::remove);
        loadModuleInfoFromFiles();

        Set<String> moduleNames = FenixFramework.getDomainRoot().getModuleSet().stream()
                .map(EditionModule::getName).collect(Collectors.toSet());

        boolean textCreate = false;
        boolean userCreate = false;
        boolean virtualCreate = false;
        boolean recommendationCreate = false;
        boolean classificationCreate = false;

        textCreate =  new FeTextRequiresInterface().initializeTextModule();

        userCreate = new FeUserRequiresInterface().initializeUserModule();

        virtualCreate = new FeVirtualRequiresInterface().initializeVirtualModule();

        new RecommendationProvidesInterface().initializeRecommendationModule();

        new FeGameRequiresInterface().initializeGameModule();

        String profile = PropertiesManager.getProperties().getProperty("spring.profiles.active");

        if (!profile.equals("test")) {
            if (moduleNames.stream().anyMatch(s -> s.equals("edition-text")) && textCreate) {
                loadTextFromFile();
            }
            if (moduleNames.stream().anyMatch(s -> s.equals("edition-user")) && userCreate) {
                loadUsersFromFile();
            }
            if (moduleNames.stream().anyMatch(s -> s.equals("edition-virtual")) && virtualCreate) {
                loadVirtualFromFile();
                loadGamesFromFile();
            }
        }
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
        textProvidesInterface.getFragmentByXmlId(null);
        textProvidesInterface.getScholarInter(null);

        EventInterface.getInstance();
        VirtualRequiresInterface.getInstance();
        SessionRequiresInterface.getInstance();
        GameRequiresInterface.getInstance();
        RecommendationRequiresInterface.getInstance();
    }

    private static void loadGamesFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "game");

        File gameFile = new File(directory, "games.xml");

        if (!gameFile.exists()) {
            return; // File does not exist but that is not a problem. Just move on
        }

        try {
            new FeGameRequiresInterface().importGamesFromTEI(new FileInputStream(gameFile));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load games from file");
        }
    }

    private static void loadUsersFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "user");

        File userFile = new File(directory, "users.xml");

        if (!userFile.exists()) {
            return; // File does not exist but that is not a problem. Just move on
        }

        try {
            new FeUserRequiresInterface().importUsersFromXML(new FileInputStream(userFile));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load games from file");
        }
    }

    private static void loadTextFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "text");

        File corpus = new File(directory, "001.xml");

        if (!corpus.exists()) {
            return; // File does not exist but that is not a problem. Just move on
        }

        FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
        try {
            feTextRequiresInterface.getLoaderTEICorpus(new FileInputStream(corpus));
            feTextRequiresInterface.getLoaderTEICorpusVirtual(new FileInputStream(corpus));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load text from file");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            try {
                feTextRequiresInterface.getLoadTEIFragmentsStepByStep(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new LdoDException("Failed to load virtual fragment");
            }
        }
        new FeTextRequiresInterface().getFragmentCorpusGenerator();
    }

    private static void loadVirtualFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "virtual");

        File virtualCorpus = new File(directory, "corpus.xml");

        if (!virtualCorpus.exists()) {
            return; // File does not exist but that is not a problem. Just move on
        }

        try {
            new FeVirtualRequiresInterface().importVirtualEditionCorupus(new FileInputStream(virtualCorpus));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load virtual corpus");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.getName().contains("corpus")) {
                continue;
            }

            try {
                new FeVirtualRequiresInterface().importVirtualEditionFragmentFromTEI(file);
            } catch (FileNotFoundException e) {
                throw new LdoDException("Failed to load virtual fragment");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadModuleInfoFromFiles() {
        String moduleConfigFilePath = PropertiesManager.getProperties().getProperty("module.files.dir");

        File directory = new File(moduleConfigFilePath);

        File configFile = new File(directory, "modules.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            String st;
            while ((st = reader.readLine()) != null) {

                EditionModule module = new EditionModule(st.replace(".txt", ""));
                UiComponent uiComponent = new UiComponent(module);

                File file = new File(directory, st);

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                readModuleConfig(uiComponent, br);
            }

            String customName = FenixFramework.getDomainRoot().getModuleSet().stream().map(EditionModule::getName)
                    .map(s -> s.replace("edition-", "")).collect(Collectors.joining("-"));

            if (Arrays.asList(directory.list()).contains(customName + ".txt")) { //custom config specified.
                EditionModule module = new EditionModule("Custom");
                UiComponent uiComponent = new UiComponent(module);

                File file = new File(directory, customName + ".txt");

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                readModuleConfig(uiComponent, br);
            }
        } catch (IOException e) {
            throw new LdoDException("loadModuleInfoFromFiles could not read module config files");
        }
    }

    private static void readModuleConfig(UiComponent uiComponent, BufferedReader br) throws IOException {
        ArrayList<String> menuNames = new ArrayList<>();
        ArrayList<String[]> optionNames = new ArrayList<>();
        ArrayList<String[]> optionLinks = new ArrayList<>();
        ArrayList<Integer[]> optionNumbers = new ArrayList<>();

        String entry;
        while ((entry = br.readLine()) != null) {
            String[] menuEntry = entry.split(" : ");
            menuNames.add(menuEntry[0]);

            String[] menuOptions = menuEntry[1].split(", ");

            ArrayList<String> optionMenuNames = new ArrayList<>();
            ArrayList<String> optionLinkNames = new ArrayList<>();
            ArrayList<Integer> optionOrder = new ArrayList<>();

            for (String option : menuOptions) {
                String[] optionEntry = option.split("#");
                optionMenuNames.add(optionEntry[0]);
                optionLinkNames.add(optionEntry[1]);
                optionOrder.add(Integer.valueOf(optionEntry[2]));
            }

            optionNames.add(optionMenuNames.toArray(new String[0]));
            optionLinks.add(optionLinkNames.toArray(new String[0]));
            optionNumbers.add(optionOrder.toArray(new Integer[0]));
        }

        createModuleInfo(uiComponent, menuNames.toArray(new String[0]), optionNames.toArray(new String[0][]),
                optionLinks.toArray(new String[0][]), optionNumbers.toArray(new Integer[0][]));
    }

    private static void createModuleInfo(UiComponent uiComponent, String[] menuNames, String[][] optionNames, String[][] optionLinks,
                                         Integer[][] optionNumbers) {
        for (int i = 0; i < menuNames.length; i++) {
            Menu menu = new Menu(uiComponent, menuNames[i], i);
            for (int j = 0; j < optionNames[i].length; j++) {
                new Option(menu, optionNames[i][j], optionLinks[i][j], optionNumbers[i][j]);
            }
        }
    }

}
