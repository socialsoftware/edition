package pt.ist.socialsoftware.edition.recommendation.fraginter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@Ignore
public class VSMFragInterRecommendedEditionWithoutDuplicatedFragments {

	protected List<Property> properties;
	protected VSMFragInterRecommender recommender;
	private FragInter fragInter;
	private List<FragInter> getRecommendedEdition;
	private long time;
	private int numberOfFragments;
	private Set<FragInter> inters;

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
		LdoD ldod = LdoD.getInstance();
		fragInter = new ArrayList<>(ldod.getFragment("Fr001").getFragmentInterSet()).get(0);
		properties = new ArrayList<Property>();
		inters = ldod.getEdition("TSC").getIntersSet();
		properties.add(new EditionProperty(1.0));
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		// properties.add(new PrintedProperty());
		properties.add(new HeteronymProperty());
		properties.add(new DateProperty());
		// properties.add(new TextProperty());
		// properties.add(new TaxonomyProperty());
		recommender = new VSMFragInterRecommender();
		time = System.currentTimeMillis();
		numberOfFragments = ldod.getFragmentsSet().size();
	}

	@After
	public void tearDown() throws Exception {

		Double duration = ((System.currentTimeMillis() - time) / 1000.0);
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testGetMostSimilarFragmentByDate() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new DateProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByEdition() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new EditionProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByHeteronym() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new HeteronymProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByManuscript() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new ManuscriptProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByTaxonomy() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new TaxonomyProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByText() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new TextProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public final void testGetMostSimilarFragmentByTypescript() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				new TypescriptProperty());
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}

	@Test
	public void testGetRecommendedEdition() {
		getRecommendedEdition = recommender.getRecommendedEditionWithoutDuplicatedFragments(fragInter, inters,
				properties);
		Assert.assertTrue(getRecommendedEdition.size() <= numberOfFragments);
	}
}
