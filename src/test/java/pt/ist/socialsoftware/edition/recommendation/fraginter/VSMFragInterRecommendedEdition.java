package pt.ist.socialsoftware.edition.recommendation.fraginter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.VSMRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

public class VSMFragInterRecommendedEdition {

	protected List<Property> properties;
	protected VSMRecommender<FragInter> recommender;
	private FragInter fragInter;
	private List<FragInter> getRecommendedEdition;
	private long time;
	private Set<FragInter> inters;

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch(WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
		LdoD ldod = LdoD.getInstance();
		fragInter = new ArrayList<>(ldod.getFragment("Fr001").getFragmentInterSet()).get(0);
		properties = new ArrayList<Property>();
		inters = ldod.getEdition("TSC").getIntersSet();
		properties.add(new EditionProperty(1.0));
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		properties.add(new PrintedProperty());
		properties.add(new HeteronymProperty());
		properties.add(new DateProperty());
		properties.add(new TextProperty());
		// properties.add(new TaxonomyProperty());
		recommender = new VSMFragInterRecommender();
		time = System.currentTimeMillis();
	}

	@After
	public void tearDown() throws Exception {
		
		Double duration = ((System.currentTimeMillis() - time) / 1000.0);
		System.out.println("Time: " + duration + "s");
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch(IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testGetMostSimilarFragInterByDate() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new DateProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByEdition() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new EditionProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByHeteronym() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new HeteronymProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByManuscript() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new ManuscriptProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByPrinted() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new PrintedProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByTaxonomy() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new TaxonomyProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByText() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new TextProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragInterByTypescript() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, new TypescriptProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public void testGetRecommendedEdition() {
		getRecommendedEdition = recommender.getRecommendedEdition(fragInter, inters, properties);
		Assert.assertFalse(getRecommendedEdition.isEmpty());
	}
}
