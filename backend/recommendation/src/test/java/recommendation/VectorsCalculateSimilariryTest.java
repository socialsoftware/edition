package recommendation;

import org.junit.jupiter.api.Test;
import pt.ist.socialsoftware.edition.recommendation.feature.Vectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VectorsCalculateSimilariryTest {

    @Test
    public void testCalculateSimiliraties() {
        double[] array1 = {1.0, 0.9, 0.8, 0.7};
        double[] array2 = {1.0, 0.9, 0.8, 0.7};
        double calculateSimiliraty1 = Vectors.calculateSimilarity(array1, array2);
        double[] array4 = {0.9, 1.0, 0.9, 0.8,};
        double calculateSimiliraty2 = Vectors.calculateSimilarity(array1, array4);
        assertTrue(calculateSimiliraty1 > calculateSimiliraty2);
        double[] array6 = {0.8, 0.9, 1.0, 0.9,};
        double calculateSimiliraty3 = Vectors.calculateSimilarity(array1, array6);
        assertTrue(calculateSimiliraty2 > calculateSimiliraty3);
    }

    @Test
    public void testCalculateSimiliraty() {
        double[] array1 = {2.0, 0.0, 1.0, 0.0};
        double[] array2 = {2.0, 1.0, 1.0, 1.0};
        double calculateSimiliraty = Vectors.calculateSimilarity(array1, array2);
        assertEquals(calculateSimiliraty, 0.8451542547285165, 0.0000001);
    }

    @Test
    public void testCalculateSimiliratyZeroVector() {
        double[] array1 = {0.0, 0.0, 0.0, 0.0};
        double[] array2 = {0.0, 0.0, 0.0, 0.0};
        double calculateSimiliraty = Vectors.calculateSimilarity(array1, array2);
        assertEquals(calculateSimiliraty, 0.0, 0.0000001);
    }
}