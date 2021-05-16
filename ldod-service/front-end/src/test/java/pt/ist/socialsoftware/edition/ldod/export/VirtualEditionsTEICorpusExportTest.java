package pt.ist.socialsoftware.edition.ldod.export;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionDto;


import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class VirtualEditionsTEICorpusExportTest {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEditionsTEICorpusExportTest.class);

    private VirtualEditionDto virtualEdition;
//    private TextModule text;
    private FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();
    private FeTextRequiresInterface feTextRequiresInterface;
    private FeUserRequiresInterface feUserRequiresInterface;
    private UserDto userDto;

    public static void logger(Object toPrint) {
        System.out.println(toPrint);
    }

    @BeforeEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUp() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

//        this.text = TextModule.getInstance();
        this.feTextRequiresInterface = new FeTextRequiresInterface();
        this.feUserRequiresInterface = new FeUserRequiresInterface();
        this.feVirtualRequiresInterface = new FeVirtualRequiresInterface();
        this.userDto = feUserRequiresInterface.createTestUser("ars1", "ars", "Antonio", "Silva", "a@a.a");
        LocalDate localDate = LocalDate.parse("20018-07-20");
        feVirtualRequiresInterface.createVirtualEdition(this.userDto.getUsername(), "acronym1", "title", localDate, true,
//                this.text.getRZEdition().getAcronym());
                "RZ");
        this.virtualEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym("LdoD-acronym1");
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {
        TestLoadUtils.cleanDatabase();
    }

    // Original test that exports and imports everything
    @Test
    @Atomic(mode = TxMode.WRITE)
    public void test() throws WriteOnReadError, NotSupportedException, SystemException, FileNotFoundException {
        logger.debug("test");

        String virtualEditionsCorpus = feVirtualRequiresInterface.exportVirtualEditionsTEICorpus();
        System.out.println(virtualEditionsCorpus);

        int numOfVirtualEditions = feVirtualRequiresInterface.getVirtualEditions().size();

        this.virtualEdition.removeByExternalId();
        this.userDto.removeUser();
        this.userDto = feUserRequiresInterface.createTestUser("ars1", "ars", "Antonio", "Silva", "a@a.a");
        LocalDate localDate = LocalDate.parse("20018-07-20");
        feVirtualRequiresInterface.createVirtualEdition(this.userDto.getUsername(), "acronym1", "title", localDate, true,
//                this.text.getRZEdition().getAcronym());
                "RZ");
        this.virtualEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym("acronym1");


        feVirtualRequiresInterface.importVirtualEditionCorupus((virtualEditionsCorpus));

        assertEquals(numOfVirtualEditions, feVirtualRequiresInterface.getVirtualEditions().size());

        System.out.println(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus());

        // descomentar
        assertEquals(Arrays.stream(virtualEditionsCorpus.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    // aux method
    private String exportPrintCleanAndImport() throws FileNotFoundException {

        String result = feVirtualRequiresInterface.exportVirtualEditionsTEICorpus();
        System.out.println(result);

        // Saving value for assert
        int numOfCriteria = this.virtualEdition.getNumberOfCriterias();
        int numOfTweets = feVirtualRequiresInterface.getAllTweets().size();

        // Clean
        feVirtualRequiresInterface.getAllTweets().forEach(t -> t.remove());
        this.virtualEdition.removeByExternalId();
        this.userDto.removeUser();
        this.userDto = feUserRequiresInterface.createTestUser("ars1", "ars", "Antonio", "Silva", "a@a.a");
        LocalDate localDate = LocalDate.parse("20018-07-20");
        feVirtualRequiresInterface.createVirtualEdition(this.userDto.getUsername(), "acronym1", "title", localDate, true,
//                this.text.getRZEdition().getAcronym());
                "RZ");
        this.virtualEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym("acronym1");
//                this.text.getRZEdition().getAcronym());

        // Import
        feVirtualRequiresInterface.importVirtualEditionCorupus((result));

        System.out.println(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus());

        assertEquals(numOfCriteria, feVirtualRequiresInterface.getVirtualEditionByAcronym("LdoD-acronym1").getNumberOfCriterias());
        //TODO: add way to load citations independently from frag
        //assertEquals(numOfTweets, this.virtualModule.getTweetSet().size());

        return result;
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportTweetsTest() throws FileNotFoundException {
        feVirtualRequiresInterface.createTweet( "sourceLink", "date", "tweetText", 1111l, "location", "country", "username", "profURL",
                "profImgURL", 9999l, true);
        feVirtualRequiresInterface.createTweet( "sourceLink", "date", "tweetText", 1111l, "location", "country", "username", "profURL",
                "profImgURL", -1l, false);
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportEmptyCriteriaTest() throws FileNotFoundException {
        logger.debug("exportEmptyCriteriaTest");

        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportMediaSourceTest() throws FileNotFoundException {
        logger.debug("exportMediaSourceTest");

//        new MediaSource(this.virtualEdition, "Twitter");
        feVirtualRequiresInterface.createMediaSource(virtualEdition.getAcronym(), "Twitter");
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportTimeWindowTest() throws FileNotFoundException {
        logger.debug("exportTimeWindowTest");

//        new TimeWindow(this.virtualEdition, new LocalDate("2018-03-06"), new LocalDate("2018-06-24"));
        feVirtualRequiresInterface.createTimeWindow(virtualEdition.getAcronym(), new LocalDate("2018-03-06"), new LocalDate("2018-06-24") );
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportGeographicLocationTest() throws FileNotFoundException {
        logger.debug("exportGeographicLocationTest");

//        new GeographicLocation(this.virtualEdition, "Portugal", "Lisboa");
        feVirtualRequiresInterface.createGeographicLocation(this.virtualEdition.getAcronym(), "Portugal", "Lisboa");
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportFrequencyTest() throws FileNotFoundException {
        logger.debug("exportFrequencyTest");

//        new Frequency(this.virtualEdition, 10);
        feVirtualRequiresInterface.createFrequency(this.virtualEdition.getAcronym(), 10);
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportSeveralCriteriaTest() throws FileNotFoundException {
        logger.debug("exportSeveralCriteriaTest");

        feVirtualRequiresInterface.createGeographicLocation(this.virtualEdition.getAcronym(), "Portugal", "Lisboa");
        feVirtualRequiresInterface.createFrequency(this.virtualEdition.getAcronym(), 10);
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void exportAllCriteriaTest() throws FileNotFoundException {
        logger.debug("exportAllCriteriaTest");

        feVirtualRequiresInterface.createMediaSource(virtualEdition.getAcronym(), "Twitter");
        feVirtualRequiresInterface.createTimeWindow(virtualEdition.getAcronym(), new LocalDate("2018-03-06"), new LocalDate("2018-06-24") );
        feVirtualRequiresInterface.createGeographicLocation(this.virtualEdition.getAcronym(), "Portugal", "Lisboa");
        feVirtualRequiresInterface.createFrequency(this.virtualEdition.getAcronym(), 10);
        String result = exportPrintCleanAndImport();
        // Check if it was well exported
        assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                Arrays.stream(feVirtualRequiresInterface.exportVirtualEditionsTEICorpus().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
    }

}
