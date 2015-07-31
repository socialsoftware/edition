package pt.ist.socialsoftware.edition.recommendation;

import Jama.Matrix;

public class Vectors {
	public static double calculateSimiliraty(double[] t1, double[] t2) {
		Matrix matrix1 = new Matrix(t1, 1);
		Matrix matrix2 = new Matrix(t2, 1);
		double dotProduct = matrix1.arrayTimes(matrix2).normInf();
		double eucledianDist = matrix1.normF() * matrix2.normF();
		return eucledianDist == 0 ? 0 : dotProduct / eucledianDist;
	}
}