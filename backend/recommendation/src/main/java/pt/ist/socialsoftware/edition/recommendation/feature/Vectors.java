package pt.ist.socialsoftware.edition.recommendation.feature;

import org.jblas.DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vectors {
    private static final Logger logger = LoggerFactory.getLogger(Vectors.class);

    public static double calculateSimilarity(double[] t1, double[] t2) {
        if (t1 == null || t2 == null) {
            return 0.0;
        }

        DoubleMatrix matrix1 = new DoubleMatrix(t1);
        DoubleMatrix matrix2 = new DoubleMatrix(t2);
        double dotProduct = matrix1.dot(matrix2);
        double eucledianDist = matrix1.norm2() * matrix2.norm2();

        // if ((eucledianDist == 0 ? 0 : dotProduct / eucledianDist) > 0.5) {
        // logger.debug("calculateSimilarity v1:{}", t1);
        // logger.debug("calculateSimilarity v2:{}", t2);
        // logger.debug("calculateSimilarity dot:{}, euc:{}, sim:{}",
        // dotProduct, eucledianDist,
        // eucledianDist == 0 ? 0 : dotProduct / eucledianDist);
        // }

        return eucledianDist == 0 ? 0 : dotProduct / eucledianDist;
    }

}