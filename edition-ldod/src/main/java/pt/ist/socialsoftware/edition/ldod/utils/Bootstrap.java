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
import pt.ist.socialsoftware.edition.ldod.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.*;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        if (TextModule.getInstance() == null) {
            new TextModule();
            cleanCorpusRepository();
            cleanIntersRepository();
        }
        if (UserModule.getInstance() == null) {
            new UserModule();
            UserModule.getInstance().setAdmin(true);
            createUsersAndRoles();
        }
        if (VirtualModule.getInstance() == null) {
            new VirtualModule();
            cleanTopicModeler();
            cleanLucene();
            createVirtualEditionsForTest();
            createLdoDArchiveVirtualEdition();
        } else {
            loadRecommendationCache();
        }

        //TODO : temporary way of loading module info into database. Should be loaded using a config file/s or another
        // more elegant way

        // clean existing module info.
        FenixFramework.getDomainRoot().getModuleSet().forEach(EditionModule::remove);

        loadModuleInfoFromFiles();

        //createEditionLdoDModuleInfo();
        /*createEditionTextModuleInfo();
        createEditionUserModuleInfo();
        createEditionSearchModuleInfo();
        createEditionVirtualModuleInfo();*/
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

                ArrayList<String> menuNames = new ArrayList<>();
                ArrayList<String[]> optionNames = new ArrayList<>();
                ArrayList<String[]> optionLinks = new ArrayList<>();

                String entry;
                while ((entry = br.readLine()) != null) {
                    String[] menuEntry = entry.split(" : ");
                    menuNames.add(menuEntry[0]);

                    String[] menuOptions = menuEntry[1].split(", ");

                    ArrayList<String> optionMenuNames = new ArrayList<>();
                    ArrayList<String> optionLinkNames = new ArrayList<>();

                    for (String option : menuOptions) {
                        String[] optionEntry = option.split("#");
                        optionMenuNames.add(optionEntry[0]);
                        optionLinkNames.add(optionEntry[1]);
                    }

                    optionNames.add(optionMenuNames.toArray(new String[0]));
                    optionLinks.add(optionLinkNames.toArray(new String[0]));
                }
                createModuleInfo(uiComponent, menuNames.toArray(new String[0]), optionNames.toArray(new String[0][]), optionLinks.toArray(new String[0][]));
            }
        } catch (IOException e) {
            throw new LdoDException("loadModuleInfoFromFiles could not read module config files");
        }
    }

    public static void createEditionLdoDModuleInfo() {
        // TODO: Only defined for main edition-ldod module. Should be decomposed into its submodules

        EditionModule module = new EditionModule("edition-ldod");
        UiComponent uiComponent = new UiComponent(module);

        String[] menuNames = {"topBar.about.title", "topBar.reading.title", "topBar.documents.title", "topBar.editions.title",
                "topBar.search.title", "topBar.virtual.title"};
        String[][] optionNames = {{"topBar.about.archive", "topBar.about.videos", "topBar.about.faq", "topBar.about.encoding",
                "topBar.about.articles", "topBar.about.conduct", "topBar.about.privacy", "topBar.about.team",
                "topBar.about.acknowledgements", "topBar.about.contact", "topBar.about.copyright"},
                {"topBar.reading.reading", "topBar.reading.visual", "topBar.reading.citations"},
                {"topBar.documents.witnesses", "topBar.documents.fragments"},
                {"Jacinto do Prado Coelho", "Teresa Sobral Cunha", "Richard Zenith", "Jerónimo Pizarro", "Arquivo VirtualModule",
                        "VirtualModule-JPC-ANOT", "VirtualModule-JOGO-CLASS", "VirtualModule-MALLET", "VirtualModule-TWITTER"},
                {"topBar.search.simple", "topBar.search.advanced"},
                {"topBar.virtual.editions", "topBar.virtual.game"}
        };

        String[][] optionLinks = {{"/about/archive", "/about/videos", "/about/faq", "/about/encoding", "/about/articles",
                "/about/conduct", "/about/privacy", "/about/team", "/about/acknowledgements", "/about/contact", "/about/copyright"},
                {"/reading", "/ldod-visual", "/citations"}, {"/source/list", "/fragments"},
                {"/edition/acronym/JPC", "/edition/acronym/TSC", "/edition/acronym/rz", "/edition/acronym/JP", "/edition/acronym/VirtualModule-Arquivo",
                        "/edition/acronym/VirtualModule-JPC-anot", "/edition/acronym/VirtualModule-Jogo-Class", "/edition/acronym/VirtualModule-Mallet", "/edition/acronym/VirtualModule-Twitter"},
                {"/search/simple", "/search/advanced"},
                {"/virtualeditions", "/classificationGames"}
        };

        createModuleInfo(uiComponent, menuNames, optionNames, optionLinks);

    }

    public static void createEditionTextModuleInfo() {
        EditionModule module = new EditionModule("edition-text");
        UiComponent uiComponent = new UiComponent(module);

        String[] menuNames = {"topBar.about.title", "topBar.reading.title", "topBar.documents.title", "topBar.editions.title"};
        String[][] optionNames = {{"topBar.about.archive", "topBar.about.videos", "topBar.about.faq", "topBar.about.encoding",
                "topBar.about.articles", "topBar.about.conduct", "topBar.about.privacy", "topBar.about.team",
                "topBar.about.acknowledgements", "topBar.about.contact", "topBar.about.copyright"},
                {"topBar.reading.reading", "topBar.reading.visual", "topBar.reading.citations"},
                {"topBar.documents.witnesses", "topBar.documents.fragments"},
                {"Jacinto do Prado Coelho", "Teresa Sobral Cunha", "Richard Zenith", "Jerónimo Pizarro"}
        };

        String[][] optionLinks = {{"/about/archive", "/about/videos", "/about/faq", "/about/encoding", "/about/articles",
                "/about/conduct", "/about/privacy", "/about/team", "/about/acknowledgements", "/about/contact", "/about/copyright"},
                {"/reading", "/ldod-visual", "/citations"}, {"/source/list", "/fragments"},
                {"/edition/acronym/JPC", "/edition/acronym/TSC", "/edition/acronym/RZ", "/edition/acronym/JP"}
        };

        createModuleInfo(uiComponent, menuNames, optionNames, optionLinks);
    }

    public static void createEditionUserModuleInfo() {

        EditionModule module = new EditionModule("edition-user");
        UiComponent uiComponent = new UiComponent(module);

        String[] menuNames = {};
        String[][] optionNames = {};

        String[][] optionLinks = {};

        createModuleInfo(uiComponent, menuNames, optionNames, optionLinks);

    }

    public static void createEditionSearchModuleInfo() {

        EditionModule module = new EditionModule("edition-search");
        UiComponent uiComponent = new UiComponent(module);

        String[] menuNames = {"topBar.search.title"};
        String[][] optionNames = {{"topBar.search.simple", "topBar.search.advanced"}};

        String[][] optionLinks = {{"/search/simple", "/search/advanced"}};

        createModuleInfo(uiComponent, menuNames, optionNames, optionLinks);

    }

    public static void createEditionVirtualModuleInfo() {
        EditionModule module = new EditionModule("edition-virtual");
        UiComponent uiComponent = new UiComponent(module);

        String[] menuNames = {"topBar.editions.title", "topBar.virtual.title"};
        String[][] optionNames = {{"Arquivo VirtualModule", "VirtualModule-JPC-ANOT", "VirtualModule-JOGO-CLASS", "VirtualModule-MALLET", "VirtualModule-TWITTER"},
                {"topBar.virtual.editions", "topBar.virtual.game"}
        };

        String[][] optionLinks = {{"/edition/acronym/VirtualModule-Arquivo", "/edition/acronym/VirtualModule-JPC-anot",
                "/edition/acronym/VirtualModule-Jogo-Class", "/edition/acronym/VirtualModule-Mallet", "/edition/acronym/VirtualModule-Twitter"},
                {"/virtualeditions", "/classificationGames"}
        };

        createModuleInfo(uiComponent, menuNames, optionNames, optionLinks);

    }

    private static void createModuleInfo(UiComponent uiComponent, String[] menuNames, String[][] optionNames, String[][] optionLinks) {
        for (int i = 0; i < menuNames.length; i++) {
            Menu menu = new Menu(uiComponent, menuNames[i], i);
            for (int j = 0; j < optionNames[i].length; j++) {
                new Option(menu, optionNames[i][j], optionLinks[i][j], j);
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
        VirtualEdition ldoDArchiveEdition = new VirtualEdition(VirtualModule.getInstance(), ARS, ExpertEdition.ARCHIVE_EDITION_ACRONYM,
                VirtualEdition.ARCHIVE_EDITION_NAME, new LocalDate(), true, null);

        ldoDArchiveEdition.addMember(ARS, Member.MemberRole.ADMIN, true);
    }

    @Atomic(mode = TxMode.WRITE)
    public static void loadRecommendationCache() {
        Set<Fragment> fragments = TextModule.getInstance().getFragmentsSet(); //TODO: should this use the interface?

        if (fragments.size() > 500) {
            List<Property> properties = new ArrayList<>();
            properties.add(new TextProperty(1.0));
            properties.add(new HeteronymProperty(1.0));
            properties.add(new DateProperty(1.0));
            properties.add(new TaxonomyProperty(1.0, VirtualModule.getInstance().getArchiveEdition().getTaxonomy(),
                    Property.PropertyCache.ON));

            VSMFragmentRecommender recommender = new VSMFragmentRecommender();
            for (Fragment fragment : fragments) {
                recommender.getMostSimilarItem(fragment, fragments, properties);
            }
        }

        Indexer.clearTermsTFIDFCache();
    }

}
