package pt.ist.socialsoftware.edition.ldod.export;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionFragmentsTEIExport;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.inout.VirtualEditionFragmentsTEIImport;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class VirtualEditionFragmentsTEIExportTest {
    private VirtualEditionFragmentsTEIExport export;

    public static void logger(Object toPrint) {
        System.out.println(toPrint);
    }

    @BeforeEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void setUp() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};

        TestLoadUtils.loadFragments(fragments);
    }

    @AfterEach
    @Atomic(mode = Atomic.TxMode.WRITE)
    public void tearDown() {
        TestLoadUtils.cleanDatabase();
    }

    @Test
    @Atomic
    public void test() throws WriteOnReadError, NotSupportedException, SystemException {
        VirtualEditionFragmentsTEIExport export = new VirtualEditionFragmentsTEIExport();
        int count = 0;
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            if (count < 15) {
                String fragmentTEI = export.exportFragment(fragment.getXmlId());
                logger(fragmentTEI);

                int numberOfInters = VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).size();

                for (VirtualEditionInter inter : VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId())) {
                    inter.remove();
                }
                fragment.getCitationSet().forEach(citation -> citation.remove());

                VirtualEditionFragmentsTEIImport im = new VirtualEditionFragmentsTEIImport();
                im.importFragmentFromTEI(fragmentTEI);

                System.out.println(export.exportFragment(fragment.getXmlId()));

                assertEquals(numberOfInters, VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).size());

                assertEquals(Arrays.stream(fragmentTEI.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(export.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
                                .collect(Collectors.joining("\\n")));
            }
            count++;
        }
        // debug
        // logger("original test size: " + VirtualModule.getInstance().getFragmentsSet().size());
    }

    // aux method
    private String exportPrintCleanAndImportFragment(Fragment fragment) {
        this.export = new VirtualEditionFragmentsTEIExport();
        String result = this.export.exportFragment(fragment.getXmlId());
        System.out.println(result);

        // Saving value for assert
        int numOfCitations = fragment.getCitationSet().size();
        int numberOfInters = VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).size();

        int numOfInfoRanges = 0;
        for (ScholarInter scholarInter : fragment.getScholarInterSet()) {
            numOfInfoRanges += scholarInter.getInfoRangeSet().size();
            // logger("FragInter Title: " + scholarInter.getTitle());
            // logger("FragInter xmlid: " + scholarInter.getXmlId());
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
        for (VirtualEditionInter inter : VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId())) {
            inter.remove();
        }
        fragment.getCitationSet().forEach(citation -> citation.remove());

        // Import
        VirtualEditionFragmentsTEIImport im = new VirtualEditionFragmentsTEIImport();
        im.importFragmentFromTEI(result);

        System.out.println(this.export.exportFragment(fragment.getXmlId()));

        //TODO: add way to load citations independently from frag
        //assertEquals(numOfCitations, fragment.getCitationSet().size());
        assertEquals(numberOfInters, VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).size());
        int numOfInfoRangesAfterImport = 0;
        for (ScholarInter scholarInter : fragment.getScholarInterSet()) {
            numOfInfoRangesAfterImport += scholarInter.getInfoRangeSet().size();
        }
        //TODO: add way to load citations independently from frag
        //assertEquals(numOfInfoRanges, numOfInfoRangesAfterImport);
        int altNumOfInfoRangesAfterImport = 0;
        int numOfAwareAnnotationsAfterImport = 0;
        for (Citation citation : fragment.getCitationSet()) {
            altNumOfInfoRangesAfterImport += citation.getInfoRangeSet().size();
            numOfAwareAnnotationsAfterImport += citation.getAwareAnnotationSet().size();
        }
        //TODO: add way to load citations independently from frag
        //assertEquals(altNumOfInfoRanges, altNumOfInfoRangesAfterImport);
        assertEquals(numOfAwareAnnotations, numOfAwareAnnotationsAfterImport);

        return result;
    }

    // TODO: test after running tweet factory
    @Test
    @Atomic
    public void exportCitationsTest() {
        int count = 0;
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            if (count < 20) {
                new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
                        "country", "username", "profURL", "profImgURL");
                String result = exportPrintCleanAndImportFragment(fragment);
                // Check if it was well exported
                assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(this.export.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
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
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            if (count < 15) {
                TwitterCitation tc = new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l,
                        "location", "country", "username", "profURL", "profImgURL");
                String num = fragment.getXmlId();
                // logger("num: " + num);
                if (fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
                    new InfoRange(tc, fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
                            "/div[1]/div[1]/p[3]", 20, "quoteExample", "textExample");
                }
                String result = exportPrintCleanAndImportFragment(fragment);
                // Check if it was well exported
                assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(this.export.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
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
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            if (count < 15) {
                TwitterCitation tc = new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l,
                        "location", "country", "username", "profURL", "profImgURL");
                String num = fragment.getXmlId();
                // logger("num: " + num);
                if (fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
                    new InfoRange(tc, fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
                            "/div[1]/div[1]/p[3]", 20, "quoteExample", "textExample");
                }
				/*FragInter fragInter = fragment.getScholarInterByXmlId("Fr023.WIT.ED.VIRT.VirtualModule-Duarte.1");
				if (fragInter != null && fragInter instanceof VirtualEditionInter) {
					VirtualEditionInter vei = (VirtualEditionInter) fragInter;
					AwareAnnotation annot = new AwareAnnotation(vei, "quoteAnnotationExample", "textAnnotationExample",
							tc);
					new Range(annot, "/div[1]/div[1]/p[3]", 10, "/div[1]/div[1]/p[3]", 20);
				}*/

                String result = exportPrintCleanAndImportFragment(fragment);
                // Check if it was well exported
                assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(this.export.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
                                .collect(Collectors.joining("\\n")));
            }
            count++;
        }
    }

}
