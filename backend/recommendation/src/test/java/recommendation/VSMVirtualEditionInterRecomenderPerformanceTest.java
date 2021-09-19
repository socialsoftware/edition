//package recommendation;
//
//import org.apache.lucene.queryparser.classic.ParseException;
//import org.joda.time.LocalDate;
//import org.junit.jupiter.api.*;
//import pt.ist.fenixframework.Atomic;
//import pt.ist.fenixframework.Atomic.TxMode;
//import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
//import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
//import pt.ist.socialsoftware.edition.ldod.frontend.virtual.FeVirtualRequiresInterface;
//import pt.ist.socialsoftware.edition.ldod.frontend.virtual.virtualDto.TopicListDTO;
//
//import pt.ist.socialsoftware.edition.recommendation.api.RecommendationRequiresInterface;
//import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionDto;
//import pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionInterDto;
//import pt.ist.socialsoftware.edition.recommendation.feature.VSMRecommender;
//import pt.ist.socialsoftware.edition.recommendation.feature.VSMVirtualEditionInterRecommender;
//import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;
//
//
//
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.assertTrue;
//
//public class VSMVirtualEditionInterRecomenderPerformanceTest {
//    private static String USER_ARS = "ars";
//    public static final String TEST_PIZARRO_ACRONYM = "TestPizarroRecommendations";
//    private static VirtualEditionDto pizarroVirtualEdition = null;
//    private static Set<VirtualEditionInterDto> pizarroVirtualEditionInters = null;
//    private static VirtualEditionDto zenithVirtualEdition = null;
//    private static Set<VirtualEditionInterDto> zenithVirtualEditionInters = null;
//    private static VirtualEditionDto cunhaVirtualEdition = null;
//    private static Set<VirtualEditionInterDto> cunhaVirtualEditionInters = null;
//    private static VSMRecommender<pt.ist.socialsoftware.edition.recommendation.api.virtualDto.VirtualEditionInterDto> recommender;
//
//    private FeTextRequiresInterface feTextRequiresInterface;
//    private FeVirtualRequiresInterface feVirtualRequiresInterface;
//
//    // Assuming that the 4 expert editions, the archive edition and user ars are
//    // in the database @BeforeAll
//    @BeforeEach
//    @Atomic(mode = TxMode.WRITE)
//    protected void setUp() throws FileNotFoundException {
//        TestLoadUtils.setUpDatabaseWithCorpus();
//        String[] fragments = {"001.xml", "002.xml", "003.xml"};
//        TestLoadUtils.loadFragments(fragments);
//
//        feTextRequiresInterface = new FeTextRequiresInterface();
//        feVirtualRequiresInterface = new FeVirtualRequiresInterface();
////        TextModule text = TextModule.getInstance();
////        ExpertEdition pizarroEdition = text.getJPEdition();
////        ExpertEdition zenithEdition = text.getRZEdition();
////        ExpertEdition cunhaEdition = text.getTSCEdition();
//
//        // create pizarro virtual edition
//        feVirtualRequiresInterface.createVirtualEdition(USER_ARS, TEST_PIZARRO_ACRONYM,
//                "TestPizarroRecommendations", LocalDate.now(), true, "JP");
//        pizarroVirtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-" + TEST_PIZARRO_ACRONYM);
//        pizarroVirtualEditionInters = pizarroVirtualEdition.getIntersSet();
//
//        // create pizarro taxonomy
//        TopicListDTO topicListDTO = null;
//        try {
//            topicListDTO = feVirtualRequiresInterface.generateTopicModeler(USER_ARS, pizarroVirtualEdition.getExternalId(), 50, 6, 11, 100);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        feVirtualRequiresInterface.getVirtualEditionByAcronym(pizarroVirtualEdition.getAcronym()).getTaxonomy().createGeneratedCategories(topicListDTO);
//
//        // create zenith virtual edition
//        feVirtualRequiresInterface.createVirtualEdition(USER_ARS, "TestZenithRecommendations",
//                "TestZenithRecommendations", LocalDate.now(), true, "RZ");
//        zenithVirtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-TestZenithRecommendations");
//        zenithVirtualEditionInters = zenithVirtualEdition.getIntersSet();
//
//        // create cunha virtual edition
//        feVirtualRequiresInterface.createVirtualEdition(USER_ARS, "TestCunhaRecommendations", "TestCunhaRecommendations",
//                LocalDate.now(), true, "TSC");
//        cunhaVirtualEdition = RecommendationRequiresInterface.getInstance().getVirtualEditionByAcronym("LdoD-TestCunhaRecommendations");
//        cunhaVirtualEditionInters = cunhaVirtualEdition.getIntersSet();
//
//        // create recommender
//        recommender = new VSMVirtualEditionInterRecommender();
//    }
//
//    @AfterEach
//    @Atomic(mode = TxMode.WRITE)
//    protected void tearDown() {
//        TestLoadUtils.cleanDatabase();
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForAllAsList() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//        properties.add(new DateProperty(1.0));
//        properties.add(new TaxonomyProperty(1.0, "LdoD-" + TEST_PIZARRO_ACRONYM, Property.PropertyCache.OFF));
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForAllAsListTwo() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = (VirtualEditionInterDto) inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//        properties.add(new DateProperty(1.0));
//        properties.add(new TaxonomyProperty(1.0, "LdoD-" + TEST_PIZARRO_ACRONYM, Property.PropertyCache.OFF));
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForDateAsList() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new DateProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForDateAsListTwo() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new DateProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForHeteronymAsList() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : cunhaVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(cunhaVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForHeteronymAsListTwo() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : cunhaVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new HeteronymProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(cunhaVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForNotCachedTaxonomyAsList() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TaxonomyProperty(1.0, "LdoD-" + TEST_PIZARRO_ACRONYM, Property.PropertyCache.OFF));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForCachedTaxonomyAsList() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TaxonomyProperty(1.0, "LdoD-" + TEST_PIZARRO_ACRONYM, Property.PropertyCache.ON));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForTextAsListOneAgain() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForTextAsListOneCunha() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : cunhaVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(cunhaVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForTextAsListOneZenith() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : zenithVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(zenithVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForTextAsListOnePizarro() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//    @Test
//    @Atomic(mode = TxMode.WRITE)
//    public void testGetMostSimilarItemForTextAsListTwo() throws IOException, ParseException {
//        VirtualEditionInterDto virtualEditionInter = null;
//        for (VirtualEditionInterDto inter : pizarroVirtualEdition.getIntersSet()) {
//            virtualEditionInter = inter;
//            break;
//        }
//
//        List<Property> properties = new ArrayList<>();
//        properties.add(new TextProperty(1.0));
//
//        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//
//        result = recommender.getMostSimilarItemsAsList((virtualEditionInter),
//                new HashSet<>(pizarroVirtualEditionInters), properties);
//
//        assertTrue(result.size() != 0);
//    }
//
//}
