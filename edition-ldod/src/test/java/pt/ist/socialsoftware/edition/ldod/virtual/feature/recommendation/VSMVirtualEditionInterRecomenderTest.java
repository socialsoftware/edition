package pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation;

import org.apache.lucene.queryparser.classic.ParseException;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.recommendation.properties.*;
import pt.ist.socialsoftware.edition.ldod.virtual.feature.topicmodeling.TopicModeler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VSMVirtualEditionInterRecomenderTest extends TestWithFragmentsLoading {
    private static final String ACRONYM = "TestRecommendations";

    private static final Logger logger = LoggerFactory.getLogger(VSMVirtualEditionInterRecomenderTest.class);

    private static VSMRecommender<VirtualEditionInter> recommender;

    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    private VirtualEdition virtualEdition;

    @Override
    protected String[] fragmentsToLoad4Test() {
        String[] fragments = {"001.xml", "181.xml", "593.xml"};

        return fragments;
    }

    // Assuming that the 4 expert editions and user ars are in the database
    @Override
    protected void populate4Test() {

        VirtualModule virtualModule = VirtualModule.getInstance();
        ExpertEdition pizarroEdition = TextModule.getInstance().getJPEdition();

        // create virtual edition
        this.virtualEdition = virtualModule.createVirtualEdition(User.USER_ARS, ACRONYM, "Name", LocalDate.now(), true,
                pizarroEdition.getAcronym());

        // create taxonomy
        TopicModeler modeler = new TopicModeler();
        TopicListDTO topicListDTO = null;
        try {
            topicListDTO = modeler.generate(User.USER_ARS, this.virtualEdition, 50, 6, 11, 10);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.virtualEdition.getTaxonomy().createGeneratedCategories(topicListDTO);

        // create recommender
        recommender = new VSMVirtualEditionInterRecommender();
    }

    @Override
    protected void unpopulate4Test() {
        TestLoadUtils.cleanDatabaseButCorpus();
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForHeteronym() {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if (inter.getLastUsed().getHeteronym() != null) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertEquals(virtualEditionInter.getLastUsed().getHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForNoHeteronym() {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if (getLastUsedScholarEditionInter(inter).getHeteronym() == NullHeteronym.getNullHeteronym()) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertEquals(NullHeteronym.getNullHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForDate() {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if ((inter.getLastUsed()).getLdoDDate() != null
                    && (inter.getLastUsed()).getLdoDDate().getDate().getYear() == 1929) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new DateProperty(1.0));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
                result.getLastUsed().getLdoDDate().getDate().getYear());
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void testGetMostSimilarItemForNoDate() {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if ((inter.getLastUsed()).getLdoDDate() == null) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new DateProperty(1.0));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertNotNull(result.getLastUsed().getLdoDDate());
        assertNull(virtualEditionInter.getLastUsed().getLdoDDate());
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForTaxonomy() {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if (!(inter).getTagSet().isEmpty()) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy(), Property.PropertyCache.OFF));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertFalse(virtualEditionInter.getTagSet().stream().map(t -> t.getCategory())
                .anyMatch(result.getTagSet().stream().map(t -> t.getCategory()).collect(Collectors.toSet())::contains));
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForText() throws IOException, ParseException {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        Indexer indexer = Indexer.getIndexer();

        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if (indexer.getTFIDFTerms(getFragment(inter), TextProperty.NUMBER_OF_TERMS).contains("cadeira")) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertTrue(indexer.getTFIDFTerms(getFragment(virtualEditionInter), TextProperty.NUMBER_OF_TERMS).stream()
                .anyMatch(indexer.getTFIDFTerms(getFragment(result), TextProperty.NUMBER_OF_TERMS)::contains));
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForAll() throws IOException, ParseException {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        Indexer indexer = Indexer.getIndexer();

        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            if (indexer.getTFIDFTerms(getFragment(inter), TextProperty.NUMBER_OF_TERMS).contains("cadeira")) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));
        properties.add(new DateProperty(1.0));
        properties.add(new TaxonomyProperty(1.0, virtualEdition.getTaxonomy(), Property.PropertyCache.OFF));
        properties.add(new TextProperty(1.0));

        VirtualEditionInter result = recommender.getMostSimilarItem(virtualEditionInter, new HashSet<>(virtualEdition
                .getIntersSet().stream().map(VirtualEditionInter.class::cast).collect(Collectors.toSet())), properties);

        assertTrue(virtualEditionInter != result);
        assertEquals(virtualEditionInter.getLastUsed().getHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
        assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
                result.getLastUsed().getLdoDDate().getDate().getYear());
        assertFalse(virtualEditionInter.getTagSet().stream().map(t -> t.getCategory())
                .anyMatch(result.getTagSet().stream().map(t -> t.getCategory()).collect(Collectors.toSet())::contains));
        assertFalse(indexer.getTFIDFTerms(getFragment(virtualEditionInter), TextProperty.NUMBER_OF_TERMS).stream()
                .anyMatch(indexer.getTFIDFTerms(getFragment(result), TextProperty.NUMBER_OF_TERMS)::contains));
    }


    private Fragment getFragment(VirtualEditionInter virtualEditionInter) {
        return getLastUsedScholarEditionInter(virtualEditionInter).getFragment();
    }

    private ScholarInter getLastUsedScholarEditionInter(VirtualEditionInter virtualEditionInter) {
        return TextModule.getInstance().getScholarInterByXmlId(virtualEditionInter.getLastUsed().getXmlId());
    }

}
