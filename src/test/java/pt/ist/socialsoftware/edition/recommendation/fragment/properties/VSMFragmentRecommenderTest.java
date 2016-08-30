package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@Ignore
public abstract class VSMFragmentRecommenderTest {
	public static final double DELTA = 0.01;
	public static final double MAX = 1.00001;

	protected LdoD ldod;
	protected List<Property> properties;
	protected VSMFragmentRecommender vsmFragmentRecomender;
	protected Fragment frag1;
	protected Fragment frag2;
	protected List<Double> v1;
	protected List<Double> v2;
	protected Property property;
	protected Property propertyWithWeight;
	protected Property propertyWithZeroWeight;

	protected abstract Fragment getFragment1();

	protected abstract Fragment getFragment2();

	protected abstract Property getProperty();

	protected abstract Property getPropertyWithWeight();

	protected abstract Property getPropertyWithZeroWeight();

	protected void prepare() {
		
	}

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
			ldod = LdoD.getInstance();
			properties = new ArrayList<Property>();
			property = getProperty();
			propertyWithWeight = getPropertyWithWeight();
			propertyWithZeroWeight = getPropertyWithZeroWeight();
			properties.add(property);
			vsmFragmentRecomender = new VSMFragmentRecommender();
			frag1 = getFragment1();
			frag2 = getFragment2();
			this.prepare();
			property.setFragmentsGroup(frag1, frag2);
			v1 = new ArrayList<Double>(frag1.accept(property));
			v2 = new ArrayList<Double>(frag2.accept(property));
		} catch(WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch(IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, properties);
		Assert.assertTrue(calculateSimiliraty >= 0);
		Assert.assertTrue(calculateSimiliraty <= 1.0000000000000002);
	}

	@Test
	public void testCalculateSimiliraty2() {
		property.setFragmentsGroup(frag1, frag2);
		propertyWithWeight.setFragmentsGroup(frag1, frag2);
		ArrayList<Double> v1 = new ArrayList<Double>(frag1.accept(property));
		ArrayList<Double> v3 = new ArrayList<Double>(frag1.accept(propertyWithWeight));
		Assert.assertTrue(v1.size() > 0);
		Assert.assertTrue(v3.size() > 0);
		Assert.assertEquals(v1.size(), v3.size());
		for(int i = 0; i < v1.size(); i++) {
			if(v1.get(i) != 0.0) {
				Assert.assertTrue(v3.get(i) > v1.get(i));
			}
		}
	}

	@Test
	public void testCalculateSimiliratyAEqualsA() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag1, property);
		Assert.assertEquals(calculateSimiliraty, 1.0, 0.0000001);
	}

	@Test
	public void testCalculateSimiliratyAEqualsBandBEqualsA() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, property);
		double calculateSimiliratyWithWeight = vsmFragmentRecomender.calculateSimilarity(frag2, frag1, property);
		Assert.assertEquals(calculateSimiliraty, calculateSimiliratyWithWeight, 0.0000001);
	}

	@Test
	public void testCalculateSimiliratyValue() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, property);
		double calculateSimiliratyWithWeight = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, propertyWithWeight);
		Assert.assertEquals(calculateSimiliraty, calculateSimiliratyWithWeight, 0.0000001);
	}

	@Test
	public void testCalculateSimiliratyWithWeight() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, propertyWithWeight);
		Assert.assertTrue(calculateSimiliraty >= 0);
		Assert.assertTrue(calculateSimiliraty <= 1.0000000000000002);
	}

	@Test
	public void testCalculateSimiliratyWithZeroWeight() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, propertyWithZeroWeight);
		Assert.assertEquals(0, calculateSimiliraty, 0);
	}

	@Test
	public final void testProperty() {
		Assert.assertTrue(v1.size() > 0);
		Assert.assertTrue(v2.size() > 0);
		Assert.assertEquals(v1.size(), v2.size());
	}
}