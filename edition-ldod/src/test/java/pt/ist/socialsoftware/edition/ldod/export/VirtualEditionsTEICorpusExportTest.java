package pt.ist.socialsoftware.edition.ldod.export;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.Frequency;
import pt.ist.socialsoftware.edition.ldod.domain.GeographicLocation;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.MediaSource;
import pt.ist.socialsoftware.edition.ldod.domain.TimeWindow;
import pt.ist.socialsoftware.edition.ldod.domain.Tweet;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionsTEICorpusImport;

public class VirtualEditionsTEICorpusExportTest extends TestWithFragmentsLoading {
	private static Logger logger = LoggerFactory.getLogger(VirtualEditionsTEICorpusExportTest.class);

	private VirtualEditionsTEICorpusExport export;
	private VirtualEdition virtualEdition;
	private LdoD ldoD;
	private LdoDUser user;

	public static void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	@Override
	protected String[] fragmentsToLoad4Test() {
		return new String[0];
	}

	@Override
	protected void populate4Test() {
		this.ldoD = LdoD.getInstance();
		this.user = new LdoDUser(this.ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		LocalDate localDate = LocalDate.parse("20018-07-20");
		this.virtualEdition = new VirtualEdition(this.ldoD, this.user, "acronym", "title", localDate, true,
				this.ldoD.getRZEdition());
	}

	@Override
	protected void unpopulate4Test() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	// Original test that exports and imports everything
	@Test
	@Atomic(mode = TxMode.WRITE)
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		logger.debug("test");

		VirtualEditionsTEICorpusExport export = new VirtualEditionsTEICorpusExport();
		String virtualEditionsCorpus = export.export();
		System.out.println(virtualEditionsCorpus);

		int numOfVirtualEditions = LdoD.getInstance().getVirtualEditionsSet().size();

		this.virtualEdition.remove();
		this.user.remove();
		this.user = new LdoDUser(this.ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		LocalDate localDate = LocalDate.parse("20018-07-20");
		this.virtualEdition = new VirtualEdition(this.ldoD, this.user, "acronym", "title", localDate, true,
				this.ldoD.getRZEdition());

		VirtualEditionsTEICorpusImport im = new VirtualEditionsTEICorpusImport();
		im.importVirtualEditionsCorpus(virtualEditionsCorpus);

		assertEquals(numOfVirtualEditions, LdoD.getInstance().getVirtualEditionsSet().size());

		System.out.println(export.export());

		// descomentar
		assertEquals(Arrays.stream(virtualEditionsCorpus.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	// aux method
	private String exportPrintCleanAndImport() {
		this.export = new VirtualEditionsTEICorpusExport();
		String result = this.export.export();
		System.out.println(result);

		// Saving value for assert
		int numOfCriteria = this.virtualEdition.getCriteriaSet().size();
		int numOfTweets = this.ldoD.getTweetSet().size();

		// Clean
		LdoD.getInstance().getTweetSet().forEach(t -> t.remove());
		this.virtualEdition.remove();
		this.user.remove();
		this.user = new LdoDUser(this.ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		LocalDate localDate = LocalDate.parse("20018-07-20");
		this.virtualEdition = new VirtualEdition(this.ldoD, this.user, "acronym", "title", localDate, true,
				this.ldoD.getRZEdition());

		// Import
		VirtualEditionsTEICorpusImport imp = new VirtualEditionsTEICorpusImport();
		imp.importVirtualEditionsCorpus(result);

		System.out.println(this.export.export());

		assertEquals(numOfCriteria, this.ldoD.getVirtualEdition("acronym").getCriteriaSet().size());
		//assertEquals(numOfTweets, this.ldoD.getTweetSet().size());

		return result;
	}

	@Test
	@Atomic
	public void exportTweetsTest() {
		new Tweet(this.ldoD, "sourceLink", "date", "tweetText", 1111l, "location", "country", "username", "profURL",
				"profImgURL", 9999l, true, null);
		new Tweet(this.ldoD, "sourceLink", "date", "tweetText", 1111l, "location", "country", "username", "profURL",
				"profImgURL", -1l, false, null);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportEmptyCriteriaTest() {
		logger.debug("exportEmptyCriteriaTest");

		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportMediaSourceTest() {
		logger.debug("exportMediaSourceTest");

		new MediaSource(this.virtualEdition, "Twitter");
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportTimeWindowTest() {
		logger.debug("exportTimeWindowTest");

		new TimeWindow(this.virtualEdition, new LocalDate("2018-03-06"), new LocalDate("2018-06-24"));
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportGeographicLocationTest() {
		logger.debug("exportGeographicLocationTest");

		new GeographicLocation(this.virtualEdition, "Portugal", "Lisboa");
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportFrequencyTest() {
		logger.debug("exportFrequencyTest");

		new Frequency(this.virtualEdition, 10);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportSeveralCriteriaTest() {
		logger.debug("exportSeveralCriteriaTest");

		new GeographicLocation(this.virtualEdition, "Portugal", "Lisboa");
		new Frequency(this.virtualEdition, 10);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportAllCriteriaTest() {
		logger.debug("exportAllCriteriaTest");

		new MediaSource(this.virtualEdition, "Twitter");
		new TimeWindow(this.virtualEdition, new LocalDate("2018-03-06"), new LocalDate("2018-06-24"));
		new GeographicLocation(this.virtualEdition, "Portugal", "Lisboa");
		new Frequency(this.virtualEdition, 10);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(this.export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

}
