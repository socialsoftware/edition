package pt.ist.socialsoftware.edition.ldod.recommendation;

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
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.VSMRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.feature.properties.*;
import pt.ist.socialsoftware.edition.ldod.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class VSMVirtualEditionInterRecomenderTest extends TestWithFragmentsLoading {
    private static final String ACRONYM = "TestRecommendations";

    private static final Logger logger = LoggerFactory.getLogger(VSMVirtualEditionInterRecomenderTest.class);

    private static VSMRecommender<VirtualEditionInterDto> recommender;

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

        // set open vocabulary taxonomy
        this.virtualEdition.getTaxonomy().setOpenVocabulary(true);

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

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter);

        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);

        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertEquals(virtualEditionInter.getLastUsed().getHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
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

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter);

        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);

        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertEquals(NullHeteronym.getNullHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
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

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter);


        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);

        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
                result.getLastUsed().getLdoDDate().getDate().getYear());
        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
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

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter);

        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);

        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertNotNull(result.getLastUsed().getLdoDDate());
        assertNull(virtualEditionInter.getLastUsed().getLdoDDate());
        assertEquals(0.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForTaxonomy() {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        Category category = new Category();
        category.init(virtualEdition.getTaxonomy(), "car");
        VirtualEditionInter virtualEditionInter1 = null;
        VirtualEditionInter virtualEditionInter2 = null;
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            new Tag().init(inter, category, "ars");
            if (virtualEditionInter1 == null) {
                new Tag().init(inter, category, "ars");
                virtualEditionInter1 = inter;
            } else if (virtualEditionInter2 == null
                    && !inter.getFragmentXmlId().equals(virtualEditionInter1.getFragmentXmlId())) {
                new Tag().init(inter, category, "ars");
                virtualEditionInter2 = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TaxonomyProperty(1.0, ACRONYM, Property.PropertyCache.OFF));

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter1);

        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);

        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertEquals(new VirtualEditionInterDto(virtualEditionInter2), result);
        assertEquals(1.0, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.001);
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForText() throws IOException, ParseException {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        Indexer indexer = Indexer.getIndexer();

        List<String> tfidfWords = new ArrayList<>();
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            tfidfWords = indexer.getTFIDFTerms(getFragment(inter), TextProperty.NUMBER_OF_TERMS);
            if (tfidfWords.contains("noite")) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new TextProperty(1.0));

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter);

        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);

        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertEquals(0.01, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.1);
    }

    @Test
    @Atomic
    public void testGetMostSimilarItemForAll() throws IOException, ParseException {
        VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(ACRONYM);
        VirtualEditionInter virtualEditionInter = null;
        Indexer indexer = Indexer.getIndexer();

        List<String> tfidfWords = new ArrayList<>();
        for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
            tfidfWords = indexer.getTFIDFTerms(getFragment(inter), TextProperty.NUMBER_OF_TERMS);
            if (tfidfWords.contains("noite")) {
                virtualEditionInter = inter;
                break;
            }
        }

        List<Property> properties = new ArrayList<>();
        properties.add(new HeteronymProperty(1.0));
        properties.add(new DateProperty(1.0));
        properties.add(new TaxonomyProperty(1.0, ACRONYM, Property.PropertyCache.OFF));
        properties.add(new TextProperty(1.0));

        VirtualEditionInterDto virtualEditionInterDto = new VirtualEditionInterDto(virtualEditionInter);

        VirtualEditionInterDto result = recommender.getMostSimilarItem(virtualEditionInterDto, virtualEdition
                .getIntersSet().stream().map(VirtualEditionInterDto::new).collect(Collectors.toSet()), properties);


        assertFalse(virtualEditionInterDto.getXmlId().equals(result.getXmlId()));
        assertEquals(virtualEditionInter.getLastUsed().getHeteronym().getXmlId(), result.getLastUsed().getHeteronym().getXmlId());
        assertEquals(virtualEditionInter.getLastUsed().getLdoDDate().getDate().getYear(),
                result.getLastUsed().getLdoDDate().getDate().getYear());

        assertEquals(0.9, recommender.calculateSimilarity(virtualEditionInterDto, result, properties), 0.1);
    }


    private Fragment getFragment(VirtualEditionInter virtualEditionInter) {
        return getLastUsedScholarEditionInter(virtualEditionInter).getFragment();
    }

    private ScholarInter getLastUsedScholarEditionInter(VirtualEditionInter virtualEditionInter) {
        return TextModule.getInstance().getScholarInterByXmlId(virtualEditionInter.getLastUsed().getXmlId());
    }

}
