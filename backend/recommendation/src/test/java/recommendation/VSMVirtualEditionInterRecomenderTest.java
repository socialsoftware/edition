//package recommendation;
//
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.joda.time.LocalDate;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import pt.ist.fenixframework.Atomic;
//import pt.ist.fenixframework.Atomic.TxMode;
//import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
//import pt.ist.socialsoftware.edition.recommendation.api.textDto.FragmentDto;
//import pt.ist.socialsoftware.edition.recommendation.api.textDto.ScholarInterDto;
//import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.CategoryDto;
//import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto;
//import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionInterDto;
//import pt.ist.socialsoftware.edition.recommendation.feature.VSMRecommender;
//import pt.ist.socialsoftware.edition.recommendation.feature.VSMVirtualEditionInterRecommender;
//import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;
//
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//public class VSMVirtualEditionInterRecomenderTest {
//    private static final String ACRONYM = "TestRecommendations";
//    private static String USER_ARS = "ars";
//
//    private static final Logger logger = LoggerFactory.getLogger(VSMVirtualEditionInterRecomenderTest.class);
//    private FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
//    private FeVirtualRequiresInterface feVirtualRequiresInterface = new FeVirtualRequiresInterface();
//
//    private static VSMRecommender<VirtualEditionInterDto> recommender;
//
//    private VirtualEditionDto virtualEdition;
//
//    @AfterEach
//    @Atomic(mode = TxMode.WRITE)
//    protected void tearDown() {
//        TestLoadUtils.cleanDatabase();
//    }
//
//    // Assuming that the 4 expert editions and user ars are in the database
//    @BeforeEach
//    @Atomic(mode = TxMode.WRITE)
//    protected void setUp() throws FileNotFoundException {
//        TestLoadUtils.setUpDatabaseWithCorpus();
//        String[] fragments = {"001.xml", "181.xml", "593.xml"};
//        TestLoadUtils.loadFragments(fragments);
////        ExpertEdition pizarroEdition = TextModule.getInstance().getJPEdition();
//
//        // create virtual edition
//        feVirtualRequiresInterface.createVirtualEdition(USER_ARS, ACRONYM, "Name", LocalDate.now(), true,
//                "JP");
//        this.virtualEdition = feVirtualRequiresInterface.getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//
//        // set open vocabulary taxonomy
//        this.virtualEdition.getTaxonomy().setOpenVocabulary(true);
//
//        // create recommender
//        recommender = new VSMVirtualEditionInterRecommender();
//
//    }
//
//    @Test
//    @Atomic
//    public void testGetMostSimilarItemForHeteronym() {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
//            if (inter.getLastUsed().getHeteronym() != null) {
//                virtualEditionInter = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//
//        VirtualEditionInterDto virtualEditionInterDto = virtualEditionInter;
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
//        assertEquals(virtualEditionInter.getLastUsed().getHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
//        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
//    }
//
//    @Test
//    @Atomic
//    public void testGetMostSimilarItemForNoHeteronym() {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
//            if (getLastUsedScholarEditionInter(inter).getHeteronym().getName().equals("não atribuído")) {
//                virtualEditionInter = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//
//        VirtualEditionInterDto virtualEditionInterDto = (virtualEditionInter);
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
////        assertEquals("NULL", result.getLastUsed().getHeteronym().getXmlId());
//        assertEquals("não atribuído", result.getLastUsed().getHeteronym().getName());
//
//        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
//    }
//
//    @Test
//    @Atomic
//    public void testGetMostSimilarItemForDate() {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
//            if (inter.getLastUsed().getLdoDDate() != null
//                    && (inter.getLastUsed()).getLdoDDate().getDate().getYear() == 1929) {
//                virtualEditionInter = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new DateProperty(1.0));
//
//        VirtualEditionInterDto virtualEditionInterDto = (virtualEditionInter);
//
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
//        assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
//                result.getLastUsed().getLdoDDate().getDate().getYear());
//        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForNoDate() {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
//            if ((inter.getLastUsed()).getLdoDDate() == null) {
//                virtualEditionInter = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new DateProperty(1.0));
//
//        VirtualEditionInterDto virtualEditionInterDto = (virtualEditionInter);
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
//        assertNotNull(result.getLastUsed().getLdoDDate());
//        assertNull(virtualEditionInter.getLastUsed().getLdoDDate());
//        assertEquals(0.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
//    }
//
//    @Test
//    @Atomic
//    public void testGetMostSimilarItemForTaxonomy() {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        CategoryDto category = new CategoryDto();
//        virtualEdition.getTaxonomy().createCategory("car");
////        category.init(virtualEdition.getTaxonomy(), "car");
//        VirtualEditionInterDto virtualEditionInter1 = null;
//        VirtualEditionInterDto virtualEditionInter2 = null;
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
//            GameRequiresInterface.getInstance().createTag(virtualEdition.getAcronym(), inter.getXmlId(), "car", "ars");
//            //            new Tag().init(inter, category, "ars");
//            if (virtualEditionInter1 == null) {
////                new Tag().init(inter, category, "ars");
//                GameRequiresInterface.getInstance().createTag(virtualEdition.getAcronym(), inter.getXmlId(), "car", "ars");
//                virtualEditionInter1 = inter;
//            } else if (virtualEditionInter2 == null
//                    && !inter.getFragmentXmlId().equals(virtualEditionInter1.getFragmentXmlId())) {
////                new Tag().init(inter, category, "ars");
//                GameRequiresInterface.getInstance().createTag(virtualEdition.getAcronym(), inter.getXmlId(), "car", "ars");
//                virtualEditionInter2 = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TaxonomyProperty(1.0, "LdoD-"+ACRONYM, Property.PropertyCache.OFF));
//
//        VirtualEditionInterDto virtualEditionInterDto = (virtualEditionInter1);
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
//        assertEquals((virtualEditionInter2), result);
//        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
//    }
//
//    @Test
//    @Atomic
//    public void testGetMostSimilarItemForText() throws IOException, ParseException {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        VirtualEditionInterDto virtualEditionInter = null;
////        Indexer indexer = Indexer.getIndexer();
//
//        List<String> tfidfWords = new ArrayList<>();
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
//            tfidfWords = RecommendationRequiresInterface.getInstance().getFragmentTFIDF(getFragment(inter).getXmlId(), TextProperty.NUMBER_OF_TERMS);
//            System.out.println(tfidfWords.get(0));
//            if (tfidfWords.contains("noite")) {
//                virtualEditionInter = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TextProperty(1.0));
//
//        VirtualEditionInterDto virtualEditionInterDto = (virtualEditionInter);
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
//        assertEquals(0.01, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.1);
//    }
//
//    @Test
//    @Atomic
//    public void testGetMostSimilarItemForAll() throws IOException, ParseException {
//        pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto virtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + ACRONYM);
//        VirtualEditionInterDto virtualEditionInter = null;
////        Indexer indexer = Indexer.getIndexer();
//
//        List<String> tfidfWords = new ArrayList<>();
//        for (VirtualEditionInterDto inter : virtualEdition.getIntersSet()) {
////            tfidfWords = indexer.getTFIDFTerms(getFragment(inter), TextProperty.NUMBER_OF_TERMS);
//            tfidfWords = RecommendationRequiresInterface.getInstance().getFragmentTFIDF(getFragment(inter).getXmlId(), TextProperty.NUMBER_OF_TERMS);
//            if (tfidfWords.contains("noite")) {
//                virtualEditionInter = inter;
//                break;
//            }
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//        properties.add(new DateProperty(1.0));
//        properties.add(new TaxonomyProperty(1.0, "LdoD-" + ACRONYM, Property.PropertyCache.OFF));
//        properties.add(new TextProperty(1.0));
//
//        VirtualEditionInterDto virtualEditionInterDto = (virtualEditionInter);
//
//        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, new HashSet<>(virtualEdition
//                .getIntersSet()), properties);
//
//
//        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
//        assertEquals(virtualEditionInter.getLastUsed().getHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
//        assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
//                result.getLastUsed().getLdoDDate().getDate().getYear());
//
//        assertEquals(0.9, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.1);
//    }
//
//
////    private Fragment getFragment(VirtualEditionInter virtualEditionInter) {
////        return getLastUsedScholarEditionInter(virtualEditionInter).getFragment();
////    }
//
////    private ScholarInter getLastUsedScholarEditionInter(VirtualEditionInter virtualEditionInter) {
////        return TextModule.getInstance().getScholarInterByXmlId(virtualEditionInter.getLastUsed().getXmlId());
////    }
//
//    private FragmentDto getFragment(VirtualEditionInterDto virtualEditionInter) {
//        return getLastUsedScholarEditionInter(virtualEditionInter).getFragmentDto();
//    }
//
//private ScholarInterDto getLastUsedScholarEditionInter(VirtualEditionInterDto virtualEditionInter) {
//    return feTextRequiresInterface.getScholarInterByXmlId(virtualEditionInter.getLastUsed().getXmlId());
//}
//
//}
