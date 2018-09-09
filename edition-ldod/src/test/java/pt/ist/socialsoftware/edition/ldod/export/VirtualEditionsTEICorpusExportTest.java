package pt.ist.socialsoftware.edition.ldod.export;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualManager;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionsTEICorpusImport;

public class VirtualEditionsTEICorpusExportTest {

	private VirtualEditionsTEICorpusExport export;
	private VirtualEdition virtualEdition;
	private VirtualManager virtualManager;

	public static void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	@BeforeEach
	public void setUp() throws WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);
	}

	// Original test that exports and imports everything
	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		VirtualEditionsTEICorpusExport export = new VirtualEditionsTEICorpusExport();
		String virtualEditionsCorpus = export.export();
		System.out.println(virtualEditionsCorpus);

		int numOfVirtualEditions = VirtualManager.getInstance().getVirtualEditionsSet().size();

		VirtualManager.getInstance().getVirtualEditionsSet().forEach(ve -> ve.remove());
		VirtualManager.getInstance().getTweetSet().forEach(tweet -> tweet.remove());

		VirtualEditionsTEICorpusImport im = new VirtualEditionsTEICorpusImport();
		im.importVirtualEditionsCorpus(virtualEditionsCorpus);

		assertEquals(numOfVirtualEditions, VirtualManager.getInstance().getVirtualEditionsSet().size());

		System.out.println(export.export());

		// descomentar
		assertEquals(Arrays.stream(virtualEditionsCorpus.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	// aux setup method
	private void setUpDomain() {
		this.virtualManager = new VirtualManager();
		LdoDUser user = new LdoDUser(virtualManager, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		LocalDate localDate = LocalDate.parse("20018-07-20");
		ExpertEdition expertEdition = virtualManager.getRZEdition();
		this.virtualEdition = new VirtualEdition(virtualManager, user, "acronym", "title", localDate, true, expertEdition);
	}

	// aux method
	private String exportPrintCleanAndImport() {
		this.export = new VirtualEditionsTEICorpusExport();
		String result = this.export.export();
		System.out.println(result);

		// Saving value for assert
		int numOfCriteria = virtualEdition.getCriteriaSet().size();
		int numOfTweets = virtualManager.getTweetSet().size();

		// Clean
		this.virtualManager.getVirtualEditionsSet().forEach(ve -> ve.remove());
		this.virtualManager.getTweetSet().forEach(tweet -> tweet.remove());

		// Import
		VirtualEditionsTEICorpusImport im = new VirtualEditionsTEICorpusImport();
		im.importVirtualEditionsCorpus(result);

		System.out.println(export.export());

		assertEquals(numOfCriteria, virtualManager.getVirtualEdition("acronym").getCriteriaSet().size());
		assertEquals(numOfTweets, virtualManager.getTweetSet().size());

		return result;
	}

	@Test
	@Atomic
	public void exportTweetsTest() {
		setUpDomain();
		new Tweet(virtualManager, "sourceLink", "date", "tweetText", 1111l, "location", "country", "username", "profURL",
				"profImgURL", 9999l, true, null);
		new Tweet(virtualManager, "sourceLink", "date", "tweetText", 1111l, "location", "country", "username", "profURL",
				"profImgURL", -1l, false, null);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportEmptyCriteriaTest() {
		setUpDomain();
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportMediaSourceTest() {
		setUpDomain();
		new MediaSource(virtualEdition, "Twitter");
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportTimeWindowTest() {
		setUpDomain();
		new TimeWindow(virtualEdition, new LocalDate("2018-03-06"), new LocalDate("2018-06-24"));
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportGeographicLocationTest() {
		setUpDomain();
		new GeographicLocation(virtualEdition, "Portugal", "Lisboa");
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportFrequencyTest() {
		setUpDomain();
		new Frequency(virtualEdition, 10);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportSeveralCriteriaTest() {
		setUpDomain();
		new GeographicLocation(virtualEdition, "Portugal", "Lisboa");
		new Frequency(virtualEdition, 10);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@Test
	@Atomic
	public void exportAllCriteriaTest() {
		setUpDomain();
		new MediaSource(virtualEdition, "Twitter");
		new TimeWindow(virtualEdition, new LocalDate("2018-03-06"), new LocalDate("2018-06-24"));
		new GeographicLocation(virtualEdition, "Portugal", "Lisboa");
		new Frequency(virtualEdition, 10);
		String result = exportPrintCleanAndImport();
		// Check if it was well exported
		assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
				Arrays.stream(export.export().split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")));
	}

	@AfterEach
	public void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}
}
