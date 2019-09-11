package pt.ist.socialsoftware.edition.ldod.utils;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.game.feature.classification.inout.GameXMLImport;
import pt.ist.socialsoftware.edition.ldod.recommendation.api.RecommendationRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.*;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.text.feature.inout.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.text.feature.inout.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.user.feature.inout.UsersXMLImport;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionFragmentsTEIExport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionsTEICorpusExport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.topicmodeling.TopicModeler;

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
        loadRecommendationCache();
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

        if (TextModule.getInstance() == null) {
            new TextModule();
            cleanCorpusRepository();
            cleanIntersRepository();
            textCreate = true;
        }
        if (UserModule.getInstance() == null) {
            new UserModule();
            UserModule.getInstance().setAdmin(true);
            createUsersAndRoles();
            userCreate = true;
        }
        if (VirtualModule.getInstance() == null) {
            new VirtualModule();
            cleanTopicModeler();
            cleanLucene();
            createVirtualEditionsForTest();
            createLdoDArchiveVirtualEdition();
            virtualCreate = true;
        }
        if (RecommendationModule.getInstance() == null) {
            new RecommendationModule();
            recommendationCreate = true;
        } else {
            loadRecommendationCache();
        }

        if (ClassificationModule.getInstance() == null) {
            new ClassificationModule();
            classificationCreate = true;
        }

        String profile = PropertiesManager.getProperties().getProperty("spring.profiles.active");

        if(!profile.equals("test")) {
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
    }

    private static void loadGamesFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "game");

        File gameFile = new File(directory, "games.xml");

        if (!gameFile.exists())
            return; // File does not exist but that is not a problem. Just move on

        GameXMLImport gameXMLImport = new GameXMLImport();

        try {
            gameXMLImport.importGamesFromTEI(new FileInputStream(gameFile));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load games from file");
        }
    }

    private static void loadUsersFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "user");

        File userFile = new File(directory, "users.xml");

        if (!userFile.exists())
            return; // File does not exist but that is not a problem. Just move on

        UsersXMLImport usersXMLImport = new UsersXMLImport();

        try {
            usersXMLImport.importUsers(new FileInputStream(userFile));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load games from file");
        }
    }

    private static void loadTextFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "text");

        File corpus = new File(directory, "001.xml");

        if (!corpus.exists())
            return; // File does not exist but that is not a problem. Just move on

        LoadTEICorpus loadTEICorpus = new LoadTEICorpus();

        try {
            loadTEICorpus.loadTEICorpus(new FileInputStream(corpus));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load games from file");
        }

        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files){
            LoadTEIFragments teiImport = new LoadTEIFragments();
            try {
                teiImport.loadFragmentsStepByStep(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new LdoDException("Failed to load virtual fragment");
            }
        }
    }

    private static void loadVirtualFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "virtual");

        File virtualCorpus = new File(directory, "corpus.xml");

        if (!virtualCorpus.exists())
            return; // File does not exist but that is not a problem. Just move on

        VirtualEditionsTEICorpusImport corpusImport = new VirtualEditionsTEICorpusImport();

        try {
            corpusImport.importVirtualEditionsCorpus(new FileInputStream(virtualCorpus));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load virtual corpus");
        }

        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files){
            if (file.getName().contains("corpus"))
                continue;
            VirtualEditionFragmentsTEIImport teiImport = new VirtualEditionFragmentsTEIImport();
            try {
                teiImport.importFragmentFromTEI(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new LdoDException("Failed to load virtual fragment");
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

    public static void cleanCorpusRepository() {
        String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
        File directory = new File(corpusFilesPath);
        if (directory.exists()) {
            try {
                FileUtils.deleteDirectory(directory);
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
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                throw new LdoDException(
                        "Bootstrap.populateDatabaseUsersAndRoles cannot delete directory for inters.dir");
            }
        }
        directory.mkdirs();
    }

    private static void cleanTopicModeler() {
        TopicModeler topicModeler = new TopicModeler();
        topicModeler.cleanDirectory();
    }

    private static void cleanLucene() {
        Indexer indexer = Indexer.getIndexer();
        indexer.cleanLucene();
    }

    private static void createUsersAndRoles() {
        UserModule userModule = UserModule.getInstance();

        Role user = Role.getRole(RoleType.ROLE_USER);
        Role admin = Role.getRole(RoleType.ROLE_ADMIN);

        // the bcrypt generator
        // https://www.dailycred.com/blog/12/bcrypt-calculator
        User ars = new User(userModule, User.USER_ARS, "$2a$11$Y0PQlyE20CXaI9RGhtjZJeTM/0.RUyp2kO/YAJI2P2FeINDEUxd2m",
                "António", "Rito Silva", "rito.silva@tecnico.ulisboa.pt");

        User twitter = new User(userModule, "Twitter", null, User.USER_TWITTER, "Social Media", "");

        // User diego = new User(ldod, "diego",
        // "$2a$11$b3rI6cl/GOzVqOKUOWSQQ.nTJFn.s8a/oALV.YOWoUZu6HZGvyCXu",
        // "Diego", "Giménez", "dgimenezdm@gmail.com");
        // User mp = new User(ldod, "mp",
        // "$2a$11$Nd6tuFTBZV3ej02xJcJhUOZtHKsc888UOBXFz9jDYDBs/EHQIIP26", "Manuel",
        // "Portela", "mportela@fl.uc.pt");
        // User tiago = new User(ldod, "tiago",
        // "$2a$11$GEa2gLrEweOV5b.fzTi5ueg.s9h2wP/SmRUt2mCvU.Ra7BxgkPVci",
        // "Tiago", "Santos", "tiago@tiagosantos.me");
        // User nuno = new User(ldod, "nuno",
        // "$2a$11$ICywhcOlcgbkWmi2zxYRi./AjLrz4Vieb25TBUeK3FsMwYmSPTcMu",
        // "Nuno", "Pinto", "nuno.mribeiro.pinto@gmail.com");
        // User luis = new User(ldod, "luis",
        // "$2a$11$c0Xrwz/gw0tBoMo3o1AG3.boCszoGOXyDWZ5z2vSY259/RDLK4ZDi",
        // "Luís", "Lucas Pereira", "lmlbpereira@gmail.com");
        // User andre = new User(ldod, "afs",
        // "$2a$11$na24dttCBjjT5uVT0mBCb.MlDdCGHwu3w6tRTqf5OD9QAsIPYJzfu",
        // "André", "Santos", "andrefilipebrazsantos@gmail.com");
        // User daniela = new User(ldod, "daniela",
        // "$2a$04$QiGbDnmoyrvyFnJdfsHhSeJoWJkjVkegrIpIADcIBVziVYWPHnPpC",
        // "Daniela", "Maduro", "cortesmaduro@hotmail.com");
        // User joana = new User(ldod, "joana",
        // "$2a$12$tdXO4XfyDP0BdrvGyScv9uRHjDPitbwKzpU1eepeJxgzZFnXZczLq",
        // "Joana", "Malta", "joanavmalta@gmail.com");
        // User bernardosoares = new User(ldod, "bernardosoares",
        // "$2a$04$2romaiXNBOFcVpDrcg0Miepy7AeeBGJq4jc4EdRA/EFekYxSFxTsC", "Bernardo",
        // "Soares",
        // "bernardosoares@pessoa.pt");
        // User rita = new User(ldod, "rita",
        // "$2a$12$6UbQBZNy0s2LQnQjaPe2au645FF.gEC7/RF5Xv9P8bdAhJo.fugoa",
        // "Rita", "Marrone", "bernardosoares@pessoa.pt");
        // User osvaldo = new User(ldod, "osvaldo",
        // "$2a$12$5WFTqOwTFfhPEeJ.L2Dbk.qvbCArQCSkcp7DdeUkrxj3dX2XT827e",
        // "Osvaldo", "Silvestre", "omsilvestre@gmail.com");
        // User jose = new User(ldod, "jose",
        // "$2a$12$gqbtKFUkIS8hqALVuc/h3eETWIWeQgxmPiWK9fm3joROsZRYHDkiW",
        // "José Maria", "Cunha", "z@josemariacunha.com");

        ars.setEnabled(true);
        ars.addRoles(user);
        ars.addRoles(admin);

        twitter.setActive(false);
        twitter.setEnabled(true);
        // diego.setEnabled(true);
        // diego.addRoles(user);
        // diego.addRoles(admin);
        //
        // mp.setEnabled(true);
        // mp.addRoles(user);
        // mp.addRoles(admin);
        //
        // tiago.setEnabled(true);
        // tiago.addRoles(user);
        // tiago.addRoles(admin);
        //
        // nuno.setEnabled(true);
        // nuno.addRoles(user);
        // nuno.addRoles(admin);
        //
        // luis.setEnabled(true);
        // luis.addRoles(user);
        // luis.addRoles(admin);
        //
        // andre.setEnabled(true);
        // andre.addRoles(user);
        // andre.addRoles(admin);
        //
        // daniela.setEnabled(true);
        // daniela.addRoles(user);
        // daniela.addRoles(admin);
        //
        // joana.setEnabled(true);
        // joana.addRoles(user);
        //
        // bernardosoares.setEnabled(true);
        // bernardosoares.addRoles(user);
        //
        // rita.setEnabled(true);
        // rita.addRoles(user);
        // rita.addRoles(admin);
        //
        // osvaldo.setEnabled(true);
        // osvaldo.addRoles(user);
        // osvaldo.addRoles(admin);
        //
        // jose.setEnabled(true);
        // jose.addRoles(user);
        // jose.addRoles(admin);
    }

    public static void createVirtualEditionsForTest() {
        logger.debug("createVirtualEditionsForTest size{}", UserModule.getInstance().getUsersSet().size());

        // User ars = ldod.getUser("ars");
        // User diego = ldod.getUser("diego");
        // User mp = ldod.getUser("mp");
        // User tiago = ldod.getUser("tiago");
        // User nuno = ldod.getUser("nuno");
        // User luis = ldod.getUser("luis");
        // User andre = ldod.getUser("afs");
        // User daniela = ldod.getUser("daniela");
        // User joana = ldod.getUser("joana");
        // User bernardosoares = ldod.getUser("bernardosoares");
        // User rita = ldod.getUser("rita");
        // User osvaldo = ldod.getUser("osvaldo");
        // User jose = ldod.getUser("jose");
        //
        // VirtualEdition classX = new VirtualEdition(ldod, ars, "VirtualModule-ClassX", "VirtualModule
        // Edition of Class X", new LocalDate(),
        // false, null);
        // classX.addMember(luis, MemberRole.ADMIN, true);
        // classX.addMember(mp, MemberRole.ADMIN, true);
        // classX.addMember(diego, MemberRole.ADMIN, true);
        // classX.addMember(tiago, MemberRole.ADMIN, true);
        // classX.addMember(ars, MemberRole.ADMIN, true);
        // classX.addMember(andre, MemberRole.ADMIN, true);
        // classX.addMember(daniela, MemberRole.ADMIN, true);
        // classX.addMember(joana, MemberRole.ADMIN, true);
        // classX.addMember(bernardosoares, MemberRole.ADMIN, true);
        // classX.addMember(rita, MemberRole.ADMIN, true);
        // classX.addMember(osvaldo, MemberRole.ADMIN, true);
        // classX.addMember(jose, MemberRole.ADMIN, true);
        // luis.addSelectedVirtualEditions(classX);
        // mp.addSelectedVirtualEditions(classX);
        // ars.addSelectedVirtualEditions(classX);
        // diego.addSelectedVirtualEditions(classX);
        // tiago.addSelectedVirtualEditions(classX);
        // nuno.addSelectedVirtualEditions(classX);
        // andre.addSelectedVirtualEditions(classX);
        // bernardosoares.addSelectedVirtualEditions(classX);
        // rita.addSelectedVirtualEditions(classX);
        // osvaldo.addSelectedVirtualEditions(classX);
        // jose.addSelectedVirtualEditions(classX);
        //
        // VirtualEdition classY = new VirtualEdition(ldod, ars, "VirtualModule-ClassY", "VirtualModule
        // Edition of Class Y", new LocalDate(),
        // false, null);
        // classY.addMember(luis, MemberRole.ADMIN, true);
        // classY.addMember(mp, MemberRole.ADMIN, true);
        // classY.addMember(diego, MemberRole.ADMIN, true);
        // classY.addMember(tiago, MemberRole.ADMIN, true);
        // classY.addMember(ars, MemberRole.ADMIN, true);
        // luis.addSelectedVirtualEditions(classY);
        // mp.addSelectedVirtualEditions(classY);
        // ars.addSelectedVirtualEditions(classY);
        // diego.addSelectedVirtualEditions(classY);
        // tiago.addSelectedVirtualEditions(classY);
        // nuno.addSelectedVirtualEditions(classY);
        //
        // VirtualEdition classW = new VirtualEdition(ldod, ars, "VirtualModule-ClassW", "VirtualModule
        // Edition of Class W", new LocalDate(),
        // false, null);
        // classW.addMember(diego, MemberRole.ADMIN, true);
        // classW.addMember(mp, MemberRole.ADMIN, true);
        // classW.addMember(luis, MemberRole.ADMIN, true);
        // classW.addMember(andre, MemberRole.ADMIN, true);
        // classW.addMember(tiago, MemberRole.ADMIN, true);
        // classW.addMember(nuno, MemberRole.ADMIN, true);
        // mp.addSelectedVirtualEditions(classW);
        // ars.addSelectedVirtualEditions(classW);
        // diego.addSelectedVirtualEditions(classW);
        // tiago.addSelectedVirtualEditions(classW);
        // nuno.addSelectedVirtualEditions(classW);
    }

    private static void createLdoDArchiveVirtualEdition() {
        VirtualEdition ldoDArchiveEdition = new VirtualEdition(VirtualModule.getInstance(), ARS, VirtualEdition.ARCHIVE_EDITION_ACRONYM,
                VirtualEdition.ARCHIVE_EDITION_NAME, new LocalDate(), true, null);

        ldoDArchiveEdition.addMember(ARS, Member.MemberRole.ADMIN, true);
    }

    @Atomic(mode = TxMode.WRITE)
    public static void loadRecommendationCache() {
        RecommendationRequiresInterface recommendationRequiresInterface = new RecommendationRequiresInterface();
        Set<FragmentDto> fragments = recommendationRequiresInterface.getFragments();

        if (fragments.size() > 800) {
            List<Property> properties = new ArrayList<>();
            properties.add(new TextProperty(1.0));
            properties.add(new HeteronymProperty(1.0));
            properties.add(new DateProperty(1.0));
            properties.add(new TaxonomyProperty(1.0, VirtualEdition.ARCHIVE_EDITION_ACRONYM,
                    Property.PropertyCache.ON));

            VSMFragmentRecommender recommender = new VSMFragmentRecommender();
            for (FragmentDto fragment : fragments) {
                logger.debug("loadRecommendationCache xmlId:{}", fragment.getXmlId());

                recommender.getMostSimilarItem(fragment, fragments, properties);
            }
        }

        Indexer.clearTermsTFIDFCache();
    }

}
