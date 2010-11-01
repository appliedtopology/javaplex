package edu.stanford.math.plex4.deprecated_tests;

import edu.stanford.math.primitivelib.autogen.matrix.DoubleSparseMatrix;

public class DoubleSparseMatrixTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 100;
		DoubleSparseMatrix matrix = new DoubleSparseMatrix(n, n);
		matrix.set(1, 4, 3);
		matrix.set(5, 78, 9);
		System.out.println(matrix);
		System.out.println(matrix.transpose());
	}

}
