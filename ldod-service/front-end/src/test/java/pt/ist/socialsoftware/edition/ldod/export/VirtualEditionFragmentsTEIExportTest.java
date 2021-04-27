package pt.ist.socialsoftware.edition.ldod.export;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.game.feature.classification.inout.GameXMLImport;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;

import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.api.textDto.CitationDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.virtual.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionFragmentsTEIExport;
import pt.ist.socialsoftware.edition.virtual.feature.inout.VirtualEditionFragmentsTEIImport;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class VirtualEditionFragmentsTEIExportTest {
    private VirtualEditionFragmentsTEIExport export;
    private FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();

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
//        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
        for (FragmentDto fragment : feTextRequiresInterface.getFragmentDtoSet()) {

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

                GameXMLImport gameloader = new GameXMLImport();
                gameloader.importGamesFromTEI(fragmentTEI);

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
    private String exportPrintCleanAndImportFragment(FragmentDto fragment) {
        this.export = new VirtualEditionFragmentsTEIExport();
        String result = this.export.exportFragment(fragment.getXmlId());
        System.out.println(result);

        // Saving value for assert
        int numOfCitations = fragment.getCitationSet().size();
        int numberOfInters = VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).size();


        int numOfInfoRanges = 0;
        for (ScholarInterDto scholarInter : fragment.getScholarInterDtoSet()) {
            numOfInfoRanges += scholarInter.getNumberOfTimesCited();
            // logger("FragInter Title: " + scholarInter.getTitle());
            // logger("FragInter xmlid: " + scholarInter.getXmlId());
        }
        // logger("numOfInfoRanges: " + numOfInfoRanges);

        int altNumOfInfoRanges = 0;
        int numOfAwareAnnotations = 0;
        for (CitationDto citation : fragment.getCitationSet()) {
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\n");
            System.out.println(altNumOfInfoRanges);
            altNumOfInfoRanges += citation.getNumberOfTimesCited();
            TwitterCitation twitterCitation = new TwitterCitation(citation);
            numOfAwareAnnotations += twitterCitation.getAwareAnnotationSet().size();
//            while (true) {}
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

        GameXMLImport gameloader = new GameXMLImport();
        gameloader.importGamesFromTEI(result);

        System.out.println(this.export.exportFragment(fragment.getXmlId()));

        //TODO: add way to load citations independently from frag
        //assertEquals(numOfCitations, fragment.getCitationSet().size());
        assertEquals(numberOfInters, VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).size());
        int numOfInfoRangesAfterImport = 0;
        for (ScholarInterDto scholarInter : fragment.getScholarInterDtoSet()) {
            numOfInfoRangesAfterImport += scholarInter.getNumberOfTimesCited();
        }
        //TODO: add way to load citations independently from frag
        //assertEquals(numOfInfoRanges, numOfInfoRangesAfterImport);
        int altNumOfInfoRangesAfterImport = 0;
        int numOfAwareAnnotationsAfterImport = 0;
        for (CitationDto citation : fragment.getCitationSet()) {
            altNumOfInfoRangesAfterImport += citation.getNumberOfTimesCited();
            TwitterCitation twitterCitation = new TwitterCitation(citation);
            numOfAwareAnnotationsAfterImport += twitterCitation.getAwareAnnotationSet().size();
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
        for (FragmentDto fragment : feTextRequiresInterface.getFragmentDtoSet()) {
            if (count < 20) {
//                new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
//                        "country", "username", "profURL", "profImgURL");
                new TwitterCitation((fragment), "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
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
        for (FragmentDto fragment : feTextRequiresInterface.getFragmentDtoSet()) {
            if (count < 15) {
//                TwitterCitation tc = new TwitterCitation(, "sourceLink", "date", "fragText", "tweetText", 7777l,
//                        "location", "country", "username", "profURL", "profImgURL");
                TwitterCitation tc = new TwitterCitation((fragment), "sourceLink", "date", "fragText", "tweetText", 7777l,
                        "location", "country", "username", "profURL", "profImgURL");
                String num = fragment.getXmlId();
                // logger("num: " + num);
                if (fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
                    VirtualRequiresInterface.getInstance().createInfoRange(tc.getId(), (num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
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
        for (FragmentDto fragment : feTextRequiresInterface.getFragmentDtoSet()) {
            if (count < 15) {
//                TwitterCitation tc = new TwitterCitation(fragment, "sourceLink", "date", "fragText", "tweetText", 7777l,
//                        "location", "country", "username", "profURL", "profImgURL");
                TwitterCitation tc = new TwitterCitation((fragment), "sourceLink", "date", "fragText", "tweetText", 7777l,
                        "location", "country", "username", "profURL", "profImgURL");
                String num = fragment.getXmlId();
                // logger("num: " + num);
                if (fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
                    VirtualRequiresInterface.getInstance().createInfoRange(tc.getId(), (num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
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
