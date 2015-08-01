package pt.ist.socialsoftware.edition.recommendation.fragment;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.EditionProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.ManuscriptProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.PrintedProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TypescriptProperty;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

public class VSMFragmentRecommendedEdition {

	protected List<Property> properties;
	protected VSMFragmentRecommender vsmFragmentRecomender;
	private Fragment fragment;
	private List<Fragment> getRecommendedEdition;
	private long time;

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
		} catch(WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
		LdoD ldod = LdoD.getInstance();
		fragment = ldod.getFragment("Fr001");
		properties = new ArrayList<Property>();
		properties.add(new EditionProperty());
		properties.add(new ManuscriptProperty());
		properties.add(new TypescriptProperty());
		properties.add(new PrintedProperty());
		properties.add(new HeteronymProperty());
		properties.add(new DateProperty());
		properties.add(new TextProperty());
		properties.add(new TaxonomyProperty());
		vsmFragmentRecomender = new VSMFragmentRecommender();
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
	public final void testGetMostSimilarFragmentByDate() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new DateProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragmentByEdition() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new EditionProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragmentByHeteronym() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new HeteronymProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragmentByManuscript() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new ManuscriptProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragmentByTaxonomy() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new TaxonomyProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragmentByText() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new TextProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public final void testGetMostSimilarFragmentByTypescript() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, new TypescriptProperty());
		Assert.assertNotNull(getRecommendedEdition);
	}

	@Test
	public void testGetRecommendedEdition() {
		getRecommendedEdition = vsmFragmentRecomender.getRecommendedEdition(fragment, properties);
		Assert.assertFalse(getRecommendedEdition.isEmpty());
	}
}
