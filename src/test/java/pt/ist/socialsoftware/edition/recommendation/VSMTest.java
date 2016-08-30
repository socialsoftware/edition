package pt.ist.socialsoftware.edition.recommendation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class VSMTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCalculateSimiliraties() {
		double[] array1 = { 1., .9, 0.8, .7 };
		double[] array2 = { 1., .9, 0.8, .7 };
		double calculateSimiliraty1 = Vectors.calculateSimilarity(array1, array2);
		double[] array4 = { .9, 1., .9, .8, };
		double calculateSimiliraty2 = Vectors.calculateSimilarity(array1, array4);
		Assert.assertTrue(calculateSimiliraty1 > calculateSimiliraty2);
		double[] array6 = { .8, .9, 1., .9, };
		double calculateSimiliraty3 = Vectors.calculateSimilarity(array1, array6);
		Assert.assertTrue(calculateSimiliraty2 > calculateSimiliraty3);
	}

	@Test
	public void testCalculateSimiliraty() {
		double[] array1 = { 2., 0., 1., 0. };
		double[] array2 = { 2., 1., 1., 1. };
		double calculateSimiliraty = Vectors.calculateSimilarity(array1, array2);
		Assert.assertEquals(calculateSimiliraty, 0.8451542547285165, 0.0000001);
	}

	@Test
	public void testCalculateSimiliratyZeroVector() {
		double[] array1 = { 0., 0., 0., 0. };
		double[] array2 = { 0., 0., 0., 0. };
		double calculateSimiliraty = Vectors.calculateSimilarity(array1, array2);
		Assert.assertEquals(calculateSimiliraty, 0.0, 0.0000001);
	}
}