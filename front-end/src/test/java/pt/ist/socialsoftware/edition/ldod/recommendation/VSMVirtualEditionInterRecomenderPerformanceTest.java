package pt.ist.socialsoftware.edition.ldod.recommendation;

import org.apache.lucene.queryparser.classic.ParseException;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;
import pt.ist.socialsoftware.edition.text.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.text.domain.TextModule;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule;
import pt.ist.socialsoftware.edition.virtual.feature.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.virtual.utils.TopicListDTO;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class VSMVirtualEditionInterRecomenderPerformanceTest {
    public static final String TEST_PIZARRO_ACRONYM = "TestPizarroRecommendations";
    private static VirtualEdition pizarroVirtualEdition = null;
    private static Set<VirtualEditionInterDto> pizarroVirtualEditionInters = null;
    private static VirtualEdition zenithVirtualEdition = null;
    private static Set<VirtualEditionInterDto> zenithVirtualEditionInters = null;
    private static VirtualEdition cunhaVirtualEdition = null;
    private static Set<VirtualEditionInterDto> cunhaVirtualEditionInters = null;
    private static VSMRecommender<VirtualEditionInterDto> recommender;

    // Assuming that the 4 expert editions, the archive edition and user ars are
    // in the database @BeforeAll
    @BeforeEach
    @Atomic(mode = TxMode.WRITE)
    protected void setUp() throws FileNotFoundException {
        TestLoadUtils.setUpDatabaseWithCorpus();
        String[] fragments = {"001.xml", "002.xml", "003.xml"};
        TestLoadUtils.loadFragments(fragments);

        VirtualModule virtualModule = VirtualModule.getInstance();
        TextModule text = TextModule.getInstance();
        ExpertEdition pizarroEdition = text.getJPEdition();
        ExpertEdition zenithEdition = text.getRZEdition();
        ExpertEdition cunhaEdition = text.getTSCEdition();

        // create pizarro virtual edition
        pizarroVirtualEdition = virtualModule.createVirtualEdition(User.USER_ARS, TEST_PIZARRO_ACRONYM,
                "TestPizarroRecommendations", LocalDate.now(), true, pizarroEdition.getAcronym());
        pizarroVirtualEditionInters = pizarroVirtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast).map(VirtualEditionInterDto::new)
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
        zenithVirtualEditionInters = zenithVirtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast).map(VirtualEditionInterDto::new)
                .collect(Collectors.toSet());

        // create cunha virtual edition
        cunhaVirtualEdition = virtualModule.createVirtualEdition(User.USER_ARS, "TestCunhaRecommendations", "TestCunhaRecommendations",
                LocalDate.now(), true, cunhaEdition.getAcronym());
        cunhaVirtualEditionInters = cunhaVirtualEdition.getIntersSet().stream().map(VirtualEditionInter.class::cast).map(VirtualEditionInterDto::new)
                .collect(Collectors.toSet());

        // create recommender
        recommender = new VSMVirtualEditionInterRecommender();
    }

    @AfterEach
    @Atomic(mode = TxMode.WRITE)
    protected void tearDown() {
        TestLoadUtils.cleanDatabase();
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
        properties.add(new TaxonomyProperty(1.0, TEST_PIZARRO_ACRONYM, Property.PropertyCache.OFF));
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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
        properties.add(new TaxonomyProperty(1.0, TEST_PIZARRO_ACRONYM, Property.PropertyCache.OFF));
        properties.add(new TextProperty(1.0));

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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
        properties.add(new TaxonomyProperty(1.0, TEST_PIZARRO_ACRONYM, Property.PropertyCache.OFF));

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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
        properties.add(new TaxonomyProperty(1.0, TEST_PIZARRO_ACRONYM, Property.PropertyCache.ON));

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
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

        List<VirtualEditionInterDto> result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);

        result = recommender.getMostSimilarItemsAsList(new VirtualEditionInterDto(virtualEditionInter),
                new HashSet<>(pizarroVirtualEditionInters), properties);

        assertTrue(result.size() != 0);
    }

}
