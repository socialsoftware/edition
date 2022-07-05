package pt.ist.socialsoftware.edition.ldod.export;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.jupiter.api.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.AwareAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.InfoRange;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Range;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionFragmentsTEIImport;

public class VirtualEditionFragmentsTEIExportTest extends TestWithFragmentsLoading {
	private VirtualEditionFragmentsTEIExport export;

	public static void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = { "001.xml", "002.xml", "003.xml" };

		return fragments;
	}

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Test
	@Atomic
	public void test() throws WriteOnReadError, NotSupportedException, SystemException {
		VirtualEditionFragmentsTEIExport export = new VirtualEditionFragmentsTEIExport();
		int count = 0;
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			if (count < 15) {
				String fragmentTEI = export.exportFragment(fragment);
				logger(fragmentTEI);

				int numberOfInters = fragment.getVirtualEditionInters().size();

				for (VirtualEditionInter inter : fragment.getVirtualEditionInters()) {
					inter.remove();
				}
				fragment.getCitationSet().forEach(citation -> citation.remove());

				VirtualEditionFragmentsTEIImport im = new VirtualEditionFragmentsTEIImport();
				im.importFragmentFromTEI(fragmentTEI);

				System.out.println(export.exportFragment(fragment));

				assertEquals(numberOfInters, fragment.getVirtualEditionInters().size());

				assertEquals(Arrays.stream(fragmentTEI.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
						Arrays.stream(export.exportFragment(fragment).split("\\r?\\n")).sorted()
								.collect(Collectors.joining("\\n")));
			}
			count++;
		}
		// debug
		// logger("original test size: " + LdoD.getInstance().getFragmentsSet().size());
	}

	// aux method
	private String exportPrintCleanAndImportFragment(Fragment fragment) {
		this.export = new VirtualEditionFragmentsTEIExport();
		String result = this.export.exportFragment(fragment);
		System.out.println(result);

		// Saving value for assert
		int numOfCitations = fragment.getCitationSet().size();
		int numberOfInters = fragment.getVirtualEditionInters().size();

		int numOfInfoRanges = 0;
		for (FragInter fragInter : fragment.getFragmentInterSet()) {
			numOfInfoRanges += fragInter.getInfoRangeSet().size();
			// logger("FragInter Title: " + fragInter.getTitle());
			// logger("FragInter xmlid: " + fragInter.getXmlId());
		}
		// logger("numOfInfoRanges: " + numOfInfoRanges);

		int altNumOfInfoRanges = 0;
		int numOfAwareAnnotations = 0;
		for (Citation citation : fragment.getCitationSet()) {
			altNumOfInfoRanges += citation.getInfoRangeSet().size();
			numOfAwareAnnotations += citation.getAwareAnnotationSet().size();
		}
		logger("numOfAwareAnnotations: " + numOfAwareAnnotations);

		// confirms that infoRanges can be accessed through two different ways
		assertEquals(numOfInfoRanges, altNumOfInfoRanges);

		// Clean
		for (VirtualEditionInter inter : fragment.getVirtualEditionInters()) {
			inter.remove();
		}
		fragment.getCitationSet().forEach(citation -> citation.remove());

		// Import
		VirtualEditionFragmentsTEIImport im = new VirtualEditionFragmentsTEIImport();
		im.importFragmentFromTEI(result);

		System.out.println(this.export.exportFragment(fragment));

		//assertEquals(numOfCitations, fragment.getCitationSet().size());
		assertEquals(numberOfInters, fragment.getVirtualEditionInters().size());
		int numOfInfoRangesAfterImport = 0;
		for (FragInter fragInter : fragment.getFragmentInterSet()) {
			numOfInfoRangesAfterImport += fragInter.getInfoRangeSet().size();
		}
		//assertEquals(numOfInfoRanges, numOfInfoRangesAfterImport);
		int altNumOfInfoRangesAfterImport = 0;
		int numOfAwareAnnotationsAfterImport = 0;
		for (Citation citation : fragment.getCitationSet()) {
			altNumOfInfoRangesAfterImport += citation.getInfoRangeSet().size();
			numOfAwareAnnotationsAfterImport += citation.getAwareAnnotationSet().size();
		}
		//assertEquals(altNumOfInfoRanges, altNumOfInfoRangesAfterImport);
		assertEquals(numOfAwareAnnotations, numOfAwareAnnotationsAfterImport);

		return result;
	}

	// TODO: test after running tweet factory
	@Test
	@Atomic
	public void exportCitationsTest() {
		int count = 0;
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			if (count < 20) {
				new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
						"country", "username", "profURL", "profImgURL");
				String result = exportPrintCleanAndImportFragment(fragment);
				// Check if it was well exported
				assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
						Arrays.stream(this.export.exportFragment(fragment).split("\\r?\\n")).sorted()
								.collect(Collectors.joining("\\n")));
			}
			count++;
		}
	}

	// TODO: test after running tweet factory
	@Test
	@Atomic
	public void exportInfoRangesTest() {
		int count = 0;
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			if (count < 15) {
				TwitterCitation tc = new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l,
						"location", "country", "username", "profURL", "profImgURL");
				String num = fragment.getXmlId();
				// logger("num: " + num);
				if (fragment.getFragInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
					new InfoRange(tc, fragment.getFragInterByXmlId(num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
							"/div[1]/div[1]/p[3]", 20, "quoteExample", "textExample");
				}
				String result = exportPrintCleanAndImportFragment(fragment);
				// Check if it was well exported
				assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
						Arrays.stream(this.export.exportFragment(fragment).split("\\r?\\n")).sorted()
								.collect(Collectors.joining("\\n")));
			}
			count++;
		}
	}

	// TODO
	@Test
	@Atomic
	public void exportAwareAnnotationsTest() {
		int count = 0;
		for (Fragment fragment : LdoD.getInstance().getFragmentsSet()) {
			if (count < 15) {
				TwitterCitation tc = new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l,
						"location", "country", "username", "profURL", "profImgURL");
				String num = fragment.getXmlId();
				// logger("num: " + num);
				if (fragment.getFragInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
					new InfoRange(tc, fragment.getFragInterByXmlId(num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
							"/div[1]/div[1]/p[3]", 20, "quoteExample", "textExample");
				}
				FragInter fragInter = fragment.getFragInterByXmlId("Fr023.WIT.ED.VIRT.LdoD-Duarte.1");
				if (fragInter != null && fragInter instanceof VirtualEditionInter) {
					VirtualEditionInter vei = (VirtualEditionInter) fragInter;
					AwareAnnotation annot = new AwareAnnotation(vei, "quoteAnnotationExample", "textAnnotationExample",
							tc);
					new Range(annot, "/div[1]/div[1]/p[3]", 10, "/div[1]/div[1]/p[3]", 20);
				}

				String result = exportPrintCleanAndImportFragment(fragment);
				// Check if it was well exported
				assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
						Arrays.stream(this.export.exportFragment(fragment).split("\\r?\\n")).sorted()
								.collect(Collectors.joining("\\n")));
			}
			count++;
		}
	}

}
