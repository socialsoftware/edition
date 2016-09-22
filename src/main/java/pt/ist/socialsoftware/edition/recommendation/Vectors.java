package pt.ist.socialsoftware.edition.recommendation;

import org.jblas.DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vectors {
	private static Logger logger = LoggerFactory.getLogger(Vectors.class);

	public static double calculateSimilarity(double[] t1, double[] t2) {
		DoubleMatrix matrix1 = new DoubleMatrix(t1);
		DoubleMatrix matrix2 = new DoubleMatrix(t2);
		double dotProduct = matrix1.dot(matrix2);
		double eucledianDist = matrix1.norm2() * matrix2.norm2();
		return eucledianDist == 0 ? 0 : dotProduct / eucledianDist;
	}

}