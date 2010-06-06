/**
 * 
 */
package edu.stanford.math.plex_plus.math.linear_algebra;

import edu.stanford.math.plex_plus.math.structures.impl.ModularIntField;
import edu.stanford.math.plex_plus.math.structures.interfaces.IntField;
import edu.stanford.math.plex_plus.utility.ArrayUtility2;

/**
 * @author Andris
 *
 */
public class TestGaussianElimination {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		largeExample();
	}
	
	public static void largeExample() {
		int m = 500;
		int n = m + 4;
		int p = 1193;
		int[][] A = new int[m][n + 1];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				A[i][j] = i + j + i * j - (int) (p * Math.random());
			}
		}
		
		//System.out.println(ArrayUtility.toString(A));
		
		IntField field = ModularIntField.getInstance(p);
		
		int[][] R = IntFieldDecompositions.computeRowEchelonForm(A, field);

		//System.out.println(ArrayUtility.toString(R));
		
		int[][] N = IntFieldDecompositions.computeNullSpace(A, field);
		//System.out.println(ArrayUtility.toString(N));
		
		int[][] product = IntFieldLinearAlgebra.product(A, N, field);
		field.valueOfInPlace(product);
		System.out.println(ArrayUtility2.toString(product));
	}
	
	public static void smallExample() {
		int p = 2;
		int[][] A = new int[][]{new int[]{1, 2, 3, 0, 1}, new int[]{0, 1, 2, -1, 0}, new int[]{0, 0, 0, 1, 1}};
		
		System.out.println(ArrayUtility2.toString(A));
		
		IntField field = ModularIntField.getInstance(p);
		
		field.valueOfInPlace(A);
		
		int[][] R = IntFieldDecompositions.computeRowEchelonForm(A, field);

		System.out.println(ArrayUtility2.toString(R));
		
		int[][] N = IntFieldDecompositions.computeNullSpace(A, field);
		System.out.println(ArrayUtility2.toString(N));
		
		int[][] product = IntFieldLinearAlgebra.product(A, N, field);
		field.valueOfInPlace(product);
		System.out.println(ArrayUtility2.toString(product));
		
		int[][] C = IntFieldDecompositions.computeColumnSpace(A, field);
		System.out.println(ArrayUtility2.toString(C));
	}

}
