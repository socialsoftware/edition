package pt.ist.socialsoftware.edition.ldod.recommendation;

import org.apache.lucene.queryparser.classic.ParseException;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.*;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property.PropertyCache;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class VSMVirtualEditionInterRecomenderPerformanceTest {
    private static VirtualEdition pizarroVirtualEdition = null;
    private static Set<VirtualEditionInter> pizarroVirtualEditionInters = null;
    private static VirtualEdition zenithVirtualEdition = null;
    private static Set<VirtualEditionInter> zenithVirtualEditionInters = null;
    private static VirtualEdition cunhaVirtualEdition = null;
    private static Set<VirtualEditionInter> cunhaVirtualEditionInters = null;
    private static VSMRecommender<VirtualEditionInter> recommender;

    @BeforeAll
    @Atomic(mode = TxMode.WRITE)
    public static void setUpAll() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();

        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);
    }

    @AfterAll
    @Atomic(mode = TxMode.WRITE)
    public static void tearDownAll() throws FileNotFoundException {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    // Assuming that the 4 expert editions, the archive edition and user ars are
    // in the database @BeforeAll
    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    protected void setUp() {
        VirtualModule virtualModule = VirtualModule.getInstance();
        TextModule text = TextModule.getInstance();
        ExpertEdition pizarroEdition = text.getJPEdition();
        ExpertEdition zenithEdition = text.getRZEdition();
        ExpertEdition cunhaEdition = text.getTSCEdition();

        // create pizarro virtual edition
        pizarroVirtualEdition = virtualModule.createVirtualEdition(User.USER_ARS, "TestPizarroRecommendations",
                "TestPizarroRecommendations", LocalDate.now(), true, pizarroEdition.getAcronym());
        pizarroVirtualEditionInters = pizarroVirtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast)
                .collect(Collectors.toSet());

        // create pizarro taxonomy
        TopicModeler modeler = new TopicModeler();
        TopicListDTO topicListDTO = null;
        try {
            topicListDTO = modeler.generate(User.USER_ARS, pizarroVirtualEdition, 50, 6, 11, 100);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        pizarroVirtualEdition.getTaxonomy().createGeneratedCategories(topicListDTO);

        // create zenith virtual edition

        zenithVirtualEdition = virtualModule.createVirtualEdition(User.USER_ARS, "TestZenithRecommendations",
                "TestZenithRecommendations", LocalDate.now(), true, zenithEdition.getAcronym());
        zenithVirtualEditionInters = zenithVirtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast)
                .collect(Collectors.toSet());

        // create cunha virtual edition
        cunhaVirtualEdition = virtualModule.createVirtualEdition(User.USER_ARS, "TestCunhaRecommendations", "TestCunhaRecommendations",
                LocalDate.now(), true, cunhaEdition.getAcronym());
        cunhaVirtualEditionInters = cunhaVirtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast)
                .collect(Collectors.toSet());

        // create recommender
        recommender = new VSMVirtualEditionInterRecommender();
    }

    @AfterEach
    @Atomic(mode = TxMode.WRITE)
    protected void unpopulate4Test() {
        pizarroVirtualEdition.remove();
        zenithVirtualEdition.remove();
        cunhaVirtualEdition.remove();
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForAllAsList() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));
        properties.add(new DateProperty(1.0));
        properties.add(new TaxonomyProperty(1.0, pizarroVirtualEdition.getTaxonomy(), PropertyCache.OFF));
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForAllAsListTwo() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = (VirtualEditionInter) inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));
        properties.add(new DateProperty(1.0));
        properties.add(new TaxonomyProperty(1.0, pizarroVirtualEdition.getTaxonomy(), PropertyCache.OFF));
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForDateAsList() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new DateProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForDateAsListTwo() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new DateProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForHeteronymAsList() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : cunhaVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(cunhaVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForHeteronymAsListTwo() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : cunhaVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(cunhaVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForNotCachedTaxonomyAsList() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TaxonomyProperty(1.0, pizarroVirtualEdition.getTaxonomy(), PropertyCache.OFF));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForCachedTaxonomyAsList() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TaxonomyProperty(1.0, pizarroVirtualEdition.getTaxonomy(), PropertyCache.ON));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForTextAsListOneAgain() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForTextAsListOneCunha() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : cunhaVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(cunhaVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForTextAsListOneZenith() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : zenithVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(zenithVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForTextAsListOnePizarro() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForTextAsListTwo() throws IOException, ParseException {
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : pizarroVirtualEdition.getIntersSet()) {
            virtualEditionInter = inter;
            break;
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInter> result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);

        result = recommender.getMostSimilarItemsAsList(virtualEditionInter,
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

}
