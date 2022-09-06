package pt.ist.socialsoftware.edition.ldod.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.*;
import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ars
 */
public class Bootstrap implements WebApplicationInitializer {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

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
        if (LdoD.getInstance() == null) {
            LdoD ldoD = new LdoD();
            ldoD.setAdmin(true);
            cleanCorpusRepository();
            cleanIntersRepository();
            cleanTopicModeler();
            cleanLucene();
            createUsersAndRoles();
            createVirtualEditionsForTest();
            createLdoDArchiveVirtualEdition();

            String profile = PropertiesManager.getProperties().getProperty("spring.profiles.active");
            if (profile != null && profile.equals("jmeter")) {
                loadFragsFromFile();
            }

        } else {
            loadRecommendationCache();
        }
        LdoD.getInstance().getVirtualEditionsSet().stream()
                .flatMap(virtualEdition -> virtualEdition.getAnnotationList().stream())
                .forEach(humanAnnotation -> {
                    if (humanAnnotation.getText() != null) {
                        String text = humanAnnotation.getText().replace("amp;", "");
                        text = StringEscapeUtils.unescapeHtml(text);
                        System.out.println(text);
                        humanAnnotation.setText(text);
                    }
                });
    }

    private static void loadFragsFromFile() {
        String loadDirPath = PropertiesManager.getProperties().getProperty("load.files.dir");

        File directory = new File(loadDirPath, "text");

        File corpus = new File(directory, "001.xml");

        if (!corpus.exists()) {
            return; // File does not exist but that is not a problem. Just move on
        }

        LoadTEICorpus loadTEICorpus = new LoadTEICorpus();

        try {
            loadTEICorpus.loadTEICorpus(new FileInputStream(corpus));
        } catch (FileNotFoundException e) {
            throw new LdoDException("Failed to load text from file");
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            LoadTEIFragments teiImport = new LoadTEIFragments();
            try {
                teiImport.loadFragmentsStepByStep(new FileInputStream(file), new ArrayList<>());
            } catch (FileNotFoundException e) {
                throw new LdoDException("Failed to load virtual fragment");
            }
        }
    }

    public static void cleanCorpusRepository() {
        String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
        File directory = new File(corpusFilesPath);
        if (directory.exists()) {
            try {
//                FileUtils.deleteDirectory(directory);
                FileUtils.cleanDirectory(directory);
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
//                FileUtils.deleteDirectory(directory);
                FileUtils.cleanDirectory(directory);
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
        LdoD ldod = LdoD.getInstance();

        Role user = Role.getRole(RoleType.ROLE_USER);
        Role admin = Role.getRole(RoleType.ROLE_ADMIN);

        // the bcrypt generator
        // https://www.dailycred.com/blog/12/bcrypt-calculator
        LdoDUser ars = new LdoDUser(ldod, "ars", "$2a$11$Y0PQlyE20CXaI9RGhtjZJeTM/0.RUyp2kO/YAJI2P2FeINDEUxd2m",
                "António", "Rito Silva", "rito.silva@tecnico.ulisboa.pt");

        LdoDUser twitter = new LdoDUser(ldod, "Twitter", null, "Twitter", "Social Media", "");

        // LdoDUser diego = new LdoDUser(ldod, "diego",
        // "$2a$11$b3rI6cl/GOzVqOKUOWSQQ.nTJFn.s8a/oALV.YOWoUZu6HZGvyCXu",
        // "Diego", "Giménez", "dgimenezdm@gmail.com");
        // LdoDUser mp = new LdoDUser(ldod, "mp",
        // "$2a$11$Nd6tuFTBZV3ej02xJcJhUOZtHKsc888UOBXFz9jDYDBs/EHQIIP26", "Manuel",
        // "Portela", "mportela@fl.uc.pt");
        // LdoDUser tiago = new LdoDUser(ldod, "tiago",
        // "$2a$11$GEa2gLrEweOV5b.fzTi5ueg.s9h2wP/SmRUt2mCvU.Ra7BxgkPVci",
        // "Tiago", "Santos", "tiago@tiagosantos.me");
        // LdoDUser nuno = new LdoDUser(ldod, "nuno",
        // "$2a$11$ICywhcOlcgbkWmi2zxYRi./AjLrz4Vieb25TBUeK3FsMwYmSPTcMu",
        // "Nuno", "Pinto", "nuno.mribeiro.pinto@gmail.com");
        // LdoDUser luis = new LdoDUser(ldod, "luis",
        // "$2a$11$c0Xrwz/gw0tBoMo3o1AG3.boCszoGOXyDWZ5z2vSY259/RDLK4ZDi",
        // "Luís", "Lucas Pereira", "lmlbpereira@gmail.com");
        // LdoDUser andre = new LdoDUser(ldod, "afs",
        // "$2a$11$na24dttCBjjT5uVT0mBCb.MlDdCGHwu3w6tRTqf5OD9QAsIPYJzfu",
        // "André", "Santos", "andrefilipebrazsantos@gmail.com");
        // LdoDUser daniela = new LdoDUser(ldod, "daniela",
        // "$2a$04$QiGbDnmoyrvyFnJdfsHhSeJoWJkjVkegrIpIADcIBVziVYWPHnPpC",
        // "Daniela", "Maduro", "cortesmaduro@hotmail.com");
        // LdoDUser joana = new LdoDUser(ldod, "joana",
        // "$2a$12$tdXO4XfyDP0BdrvGyScv9uRHjDPitbwKzpU1eepeJxgzZFnXZczLq",
        // "Joana", "Malta", "joanavmalta@gmail.com");
        // LdoDUser bernardosoares = new LdoDUser(ldod, "bernardosoares",
        // "$2a$04$2romaiXNBOFcVpDrcg0Miepy7AeeBGJq4jc4EdRA/EFekYxSFxTsC", "Bernardo",
        // "Soares",
        // "bernardosoares@pessoa.pt");
        // LdoDUser rita = new LdoDUser(ldod, "rita",
        // "$2a$12$6UbQBZNy0s2LQnQjaPe2au645FF.gEC7/RF5Xv9P8bdAhJo.fugoa",
        // "Rita", "Marrone", "bernardosoares@pessoa.pt");
        // LdoDUser osvaldo = new LdoDUser(ldod, "osvaldo",
        // "$2a$12$5WFTqOwTFfhPEeJ.L2Dbk.qvbCArQCSkcp7DdeUkrxj3dX2XT827e",
        // "Osvaldo", "Silvestre", "omsilvestre@gmail.com");
        // LdoDUser jose = new LdoDUser(ldod, "jose",
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
        LdoD ldod = LdoD.getInstance();

        logger.debug("createVirtualEditionsForTest size{}", ldod.getUsersSet().size());

        // LdoDUser ars = ldod.getUser("ars");
        // LdoDUser diego = ldod.getUser("diego");
        // LdoDUser mp = ldod.getUser("mp");
        // LdoDUser tiago = ldod.getUser("tiago");
        // LdoDUser nuno = ldod.getUser("nuno");
        // LdoDUser luis = ldod.getUser("luis");
        // LdoDUser andre = ldod.getUser("afs");
        // LdoDUser daniela = ldod.getUser("daniela");
        // LdoDUser joana = ldod.getUser("joana");
        // LdoDUser bernardosoares = ldod.getUser("bernardosoares");
        // LdoDUser rita = ldod.getUser("rita");
        // LdoDUser osvaldo = ldod.getUser("osvaldo");
        // LdoDUser jose = ldod.getUser("jose");
        //
        // VirtualEdition classX = new VirtualEdition(ldod, ars, "LdoD-ClassX", "LdoD
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
        // VirtualEdition classY = new VirtualEdition(ldod, ars, "LdoD-ClassY", "LdoD
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
        // VirtualEdition classW = new VirtualEdition(ldod, ars, "LdoD-ClassW", "LdoD
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
        LdoD ldod = LdoD.getInstance();

        LdoDUser ars = ldod.getUser("ars");
        // LdoDUser mp = ldod.getUser("mp");

        VirtualEdition ldoDArchiveEdition = new VirtualEdition(ldod, ars, Edition.ARCHIVE_EDITION_ACRONYM,
                Edition.ARCHIVE_EDITION_NAME, new LocalDate(), true, null);

        // ldoDArchiveEdition.addMember(mp, MemberRole.ADMIN, true);
        ldoDArchiveEdition.addMember(ars, Member.MemberRole.ADMIN, true);
    }

    @Atomic(mode = TxMode.WRITE)
    public static void loadRecommendationCache() {
        Set<Fragment> fragments = LdoD.getInstance().getFragmentsSet();

        if (fragments.size() > 500) {
            List<Property> properties = new ArrayList<>();
            properties.add(new TextProperty(1.0));
            properties.add(new HeteronymProperty(1.0));
            properties.add(new DateProperty(1.0));
            properties.add(new TaxonomyProperty(1.0, LdoD.getInstance().getArchiveEdition().getTaxonomy(),
                    Property.PropertyCache.ON));

            VSMFragmentRecommender recommender = new VSMFragmentRecommender();
            for (Fragment fragment : fragments) {
                recommender.getMostSimilarItem(fragment, fragments, properties);
            }
        }

        Indexer.clearTermsTFIDFCache();
    }

}
