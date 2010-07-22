package edu.stanford.math.plex4.deprecated;

import edu.stanford.math.plex4.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex4.utility.ExceptionUtility;

public class IntFieldLinearAlgebra {
	public static int[][] product(int[][] matrix1, int[][] matrix2, IntField field) {
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyPositive(matrix2.length);
		ExceptionUtility.verifyPositive(matrix1[0].length);
		ExceptionUtility.verifyPositive(matrix1[0].length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2.length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		int p = matrix2[0].length;
		
		int[][] result = new int[m][p];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < n; k++) {
					result[i][j] = field.add(result[i][j], field.multiply(matrix1[i][k], matrix2[k][j]));
				}
			}
		}
		return result;
	}
}
