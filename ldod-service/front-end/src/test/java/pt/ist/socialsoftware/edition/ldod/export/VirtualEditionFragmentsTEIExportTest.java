package pt.ist.socialsoftware.edition.ldod.export;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;

import pt.ist.socialsoftware.edition.ldod.frontend.game.FeGameRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.CitationDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.frontend.text.textDto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.TwitterCitationDto;
import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.VirtualEditionInterDto;


import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class VirtualEditionFragmentsTEIExportTest {

    private FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();
    private FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
    private FeGameRequiresInterface feGameRequiresInterface = new FeGameRequiresInterface();

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
        int count = 0;
//        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
        for (FragmentDto fragment : feTextRequiresInterface.getFragmentDtoSet()) {

            if (count < 15) {
                String fragmentTEI = feVirtualRequiresInterface.exportFragment(fragment.getXmlId());
                logger(fragmentTEI);

                int numberOfInters = feVirtualRequiresInterface.getVirtualEditionInterSetFromFragment(fragment.getXmlId()).size();

                for (VirtualEditionInterDto inter : feVirtualRequiresInterface.getVirtualEditionInterSetFromFragment(fragment.getXmlId())) {
                    inter.removeInter();
                }
                fragment.getCitationSet().forEach(citation -> citation.remove());


                try {
                    feVirtualRequiresInterface.importVirtualEditionFragmentFromTEI((fragmentTEI));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                feGameRequiresInterface.importGamesFromTEI(new ByteArrayInputStream(fragmentTEI.getBytes()));

                System.out.println(feVirtualRequiresInterface.exportFragment(fragment.getXmlId()));

                assertEquals(numberOfInters, feVirtualRequiresInterface.getVirtualEditionInterSetFromFragment(fragment.getXmlId()).size());

                assertEquals(Arrays.stream(fragmentTEI.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(feVirtualRequiresInterface.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
                                .collect(Collectors.joining("\\n")));
            }
            count++;
        }
        // debug
        // logger("original test size: " + VirtualModule.getInstance().getFragmentsSet().size());
    }

    // aux method
    private String exportPrintCleanAndImportFragment(FragmentDto fragment) {

        String result = feVirtualRequiresInterface.exportFragment(fragment.getXmlId());
        System.out.println(result);

        // Saving value for assert
        int numOfCitations = fragment.getCitationSet().size();
        int numberOfInters = feVirtualRequiresInterface.getVirtualEditionInterSetFromFragment(fragment.getXmlId()).size();


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
            System.out.println(altNumOfInfoRanges);
            altNumOfInfoRanges += citation.getNumberOfTimesCited();
            TwitterCitationDto twitterCitation = feVirtualRequiresInterface.createTwitterCitationFromCitation(citation);
            numOfAwareAnnotations += twitterCitation.getAwareAnnotationDtos().size();
//            while (true) {}
        }
        logger("numOfAwareAnnotations: " + numOfAwareAnnotations);

        // confirms that infoRanges can be accessed through two different ways
        assertEquals(numOfInfoRanges, altNumOfInfoRanges);

        // Clean
        for (VirtualEditionInterDto inter : feVirtualRequiresInterface.getVirtualEditionInterSetFromFragment(fragment.getXmlId())) {
            inter.removeInter();
        }
        fragment.getCitationSet().forEach(citation -> citation.remove());

        // Import
        try {
            feVirtualRequiresInterface.importVirtualEditionFragmentFromTEI((result));
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        feGameRequiresInterface.importGamesFromTEI(new ByteArrayInputStream(result.getBytes()));

        System.out.println(feVirtualRequiresInterface.exportFragment(fragment.getXmlId()));

        //TODO: add way to load citations independently from frag
        //assertEquals(numOfCitations, fragment.getCitationSet().size());
        assertEquals(numberOfInters, feVirtualRequiresInterface.getVirtualEditionInterSetFromFragment(fragment.getXmlId()).size());
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
            TwitterCitationDto twitterCitation = feVirtualRequiresInterface.createTwitterCitationFromCitation(citation);
            numOfAwareAnnotations += twitterCitation.getAwareAnnotationDtos().size();
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
                feVirtualRequiresInterface.createTwitterCitation((fragment.getXmlId()), "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
                        "country", "username", "profURL", "profImgURL");
                String result = exportPrintCleanAndImportFragment(fragment);
                // Check if it was well exported
                assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(feVirtualRequiresInterface.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
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

                TwitterCitationDto tc = feVirtualRequiresInterface.createTwitterCitation((fragment.getXmlId()), "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
                        "country", "username", "profURL", "profImgURL");
                String num = fragment.getXmlId();
                // logger("num: " + num);
                if (fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
                    feVirtualRequiresInterface.createInfoRange(tc.getId(), (num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
                            "/div[1]/div[1]/p[3]", 20, "quoteExample", "textExample");
                }
                String result = exportPrintCleanAndImportFragment(fragment);
                // Check if it was well exported
                assertEquals(Arrays.stream(result.split("\\r?\\n")).sorted().collect(Collectors.joining("\\n")),
                        Arrays.stream(feVirtualRequiresInterface.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
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
                TwitterCitationDto tc = feVirtualRequiresInterface.createTwitterCitation((fragment.getXmlId()), "sourceLink", "date", "fragText", "tweetText", 7777l, "location",
                        "country", "username", "profURL", "profImgURL");
                String num = fragment.getXmlId();
                // logger("num: " + num);
                if (fragment.getScholarInterByXmlId(num + ".WIT.ED.CRIT.Z") != null) {
                    feVirtualRequiresInterface.createInfoRange(tc.getId(), (num + ".WIT.ED.CRIT.Z"), "/div[1]/div[1]/p[3]", 10,
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
                        Arrays.stream(feVirtualRequiresInterface.exportFragment(fragment.getXmlId()).split("\\r?\\n")).sorted()
                                .collect(Collectors.joining("\\n")));
            }
            count++;
        }
    }

}
