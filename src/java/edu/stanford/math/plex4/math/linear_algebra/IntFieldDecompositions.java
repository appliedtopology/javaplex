package edu.stanford.math.plex4.math.linear_algebra;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex4.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import edu.stanford.math.plex4.utility.MemoryUtility;
import gnu.trove.map.hash.TIntIntHashMap;

public class IntFieldDecompositions {
	/**
	 * Prevent instantiation.
	 */
	private IntFieldDecompositions(){}

	private static void swapColumns(int[][] matrix, int i, int j) {
		ExceptionUtility.verifyNonNull(matrix);

		int m = matrix.length;
		if (m == 0) {
			return;
		}

		int n = matrix[0].length;

		ExceptionUtility.verifyIndex(n, i);
		ExceptionUtility.verifyIndex(n, j);

		int temp = 0;
		for (int k = 0; k < m; k++) {
			temp = matrix[k][i];
			matrix[k][i] = matrix[k][j];
			matrix[k][j] = temp;
		}
	}

	private static void swapRows(int[][] matrix, int i, int j) {
		ExceptionUtility.verifyNonNull(matrix);

		int m = matrix.length;
		if (m == 0) {
			return;
		}

		int n = matrix[0].length;
		ExceptionUtility.verifyIndex(m, i);
		ExceptionUtility.verifyIndex(m, j);

		int temp = 0;
		for (int k = 0; k < n; k++) {
			temp = matrix[i][k];
			matrix[i][k] = matrix[j][k];
			matrix[j][k] = temp;
		}
	}

	private static void addColumnMultiple(int[][] matrix, int destinationColumn, int sourceColumn, int multiplier) {
		ExceptionUtility.verifyNonNull(matrix);

		int m = matrix.length;
		if (m == 0) {
			return;
		}

		int n = matrix[0].length;
		ExceptionUtility.verifyIndex(n, destinationColumn);
		ExceptionUtility.verifyIndex(n, sourceColumn);

		for (int k = 0; k < m; k++) {
			matrix[k][destinationColumn] += multiplier * matrix[k][sourceColumn];
		}
	}

	private static void addRowMultiple(int[][] matrix, int destinationRow, int sourceRow, int multiplier) {
		ExceptionUtility.verifyNonNull(matrix);

		int m = matrix.length;
		if (m == 0) {
			return;
		}

		int n = matrix[0].length;
		ExceptionUtility.verifyIndex(m, destinationRow);
		ExceptionUtility.verifyIndex(m, sourceRow);

		for (int k = 0; k < n; k++) {
			matrix[destinationRow][k] += multiplier * matrix[sourceRow][k];
		}
	}

	private static void multiplyColumn(int[][] matrix, int column, int multiplier) {
		ExceptionUtility.verifyNonNull(matrix);

		int m = matrix.length;
		if (m == 0) {
			return;
		}

		int n = matrix[0].length;
		ExceptionUtility.verifyIndex(n, column);

		for (int k = 0; k < m; k++) {
			matrix[k][column] *= multiplier;
		}
	}

	private static void multiplyRow(int[][] matrix, int row, int multiplier) {
		ExceptionUtility.verifyNonNull(matrix);

		int m = matrix.length;
		if (m == 0) {
			return;
		}

		int n = matrix[0].length;
		ExceptionUtility.verifyIndex(m, row);

		for (int k = 0; k < n; k++) {
			matrix[row][k] *= multiplier;
		}
	}

	public static int[][] computeRowEchelonForm(int[][] matrix, IntField field) {
		int m = matrix.length;
		if (m == 0) return matrix;

		int[][] result = (int[][]) MemoryUtility.deepArrayCopy(matrix);
		field.valueOfInPlace(result);
		
		int n = result[0].length;
		int currentPivotRow = 0;
		int currentColumn = 0;
		int nextPivotRow = 0;
		
		while (nextPivotRow < m && currentColumn < n) {
			currentPivotRow = nextPivotRow;
			//System.out.println(ArrayUtility.toString(result));
			// find pivot row - we must have that the
			// entry A[j][j] be nonzero
			if (field.isZero(result[currentPivotRow][currentColumn])) {
				int pivotSwapRow = currentPivotRow;
				boolean pivot_found = false;
				for (int i = currentPivotRow + 1; i < m; i++) {
					if (!field.isZero(result[i][currentColumn])) {
						pivotSwapRow = i;
						//currentPivotRow = i;
						pivot_found = true;
						break;
					}
				}

				/*
				 * If we have not found a pivot element, then that
				 * means that all of the entries below and including
				 * the (j, j)-th position are zero. Thus we can simply 
				 * continue to the next column.
				 */
				if (!pivot_found) {
					currentColumn++;
					continue;
				}

				/*
				 * If we have found a pivot, row then we arrange the rows
				 * so that A[j][j] is non-zero.
				 */
				swapRows(result, currentPivotRow, pivotSwapRow);
				
				// since we have found a pivot, we can advance the current pivot row
				nextPivotRow = currentPivotRow + 1;
			} else {
				nextPivotRow = currentPivotRow + 1;
			}
			
			int leadingValue = field.invert(result[currentPivotRow][currentColumn]);
			
			// normalize current row so that leading entry is 1
			for (int k = currentColumn; k < n; k++) {
				result[currentPivotRow][k] = field.multiply(leadingValue, result[currentPivotRow][k]);
			}
			
			// iterate through each row with i > j
			// and eliminate everything under (j, j)--th entry
			for (int i = currentPivotRow + 1; i < m; i++) {
				//double alpha = A[i][j] / A[j][j];

				// compute multiplier
				int alpha = field.divide(result[i][currentColumn], result[currentPivotRow][currentColumn]);

				// apply tranformation to rows
				for (int k = currentColumn; k < n; k++) {
					//A[i][k] -= alpha * A[j][k];
					result[i][k] = field.subtract(result[i][k], field.multiply(alpha, result[currentPivotRow][k]));
				}
			}
			currentColumn++;
		}
		
		field.valueOfInPlace(result);
		
		return result;
	}

	public static int[][] computeColumnSpace(int[][] A, IntField field) {
		ExceptionUtility.verifyNonNull(A);
		ExceptionUtility.verifyPositive(A.length);

		/**
		 * Stores a basis for the null space as column vectors.
		 */
		int[][] columnSpace = null;

		int m = A.length;
		int n = A[0].length;

		int[][] rowEchelonForm = computeRowEchelonForm(A, field);
		
		List<Integer> pivotColumns = new ArrayList<Integer>();

		/*
		 * Find all of the pivot columns.
		 */
		int numNonPivotColumns = 0;
		int j = 0;
		for (int i = 0; i < m; i++) {
			j = i + numNonPivotColumns;
			while ((j < n) && field.isZero(rowEchelonForm[i][j])) {
				numNonPivotColumns++;
				j++;
			}
			if (j < n) {
				pivotColumns.add(j);
			}
		}
		
		columnSpace = new int[m][pivotColumns.size()];
		
		j = 0;
		for (int pivotColumn: pivotColumns) {
			// copy the column with same index as pivot column to the column
			// space
			for (int i = 0; i < m; i++) {
				columnSpace[i][j] = A[i][pivotColumn];
			}
			j++;
		}
		
		return columnSpace;
	}
	
	public static int[][] computeNullSpace(int[][] A, IntField field) {
		ExceptionUtility.verifyNonNull(A);
		ExceptionUtility.verifyPositive(A.length);

		/**
		 * Stores a basis for the null space as column vectors.
		 */
		int[][] nullSpace = null;

		int m = A.length;
		int n = A[0].length;

		int[][] rowEchelonForm = computeRowEchelonForm(A, field);

		List<Integer> nonPivotColumns = new ArrayList<Integer>();

		/**
		 * maps rows -> leading pivot column
		 */
		TIntIntHashMap pivotColumns = new TIntIntHashMap();

		// find number of non-pivot columns - this equals the dimension
		// of the null space
		int numNonPivotColumns = 0;
		int j = 0;
		for (int i = 0; i < m; i++) {
			j = i + numNonPivotColumns;
			while ((j < n) && field.isZero(rowEchelonForm[i][j])) {
				nonPivotColumns.add(j);
				numNonPivotColumns++;
				j++;
			}
			if (j < n) {
				pivotColumns.put(i, j);
			}
		}

		// add remaining columns to list of non pivot columns
		numNonPivotColumns += (n - j - 1);
		for (int j_2 = j + 1; j_2 < n; j_2++) {
			nonPivotColumns.add(j_2);
		}

		// create the null space matrix
		nullSpace = new int[n][nonPivotColumns.size()];

		j = 0;
		for (int nonPivotColumn: nonPivotColumns) {
			nullSpace[nonPivotColumn][j] = 1;
			j++;
		}

		for (int i = m - 1; i >= 0; i--) {
			int leadingColumn = pivotColumns.get(i);
			if (!field.isZero(rowEchelonForm[i][leadingColumn])) {
				for (int k = n - 1; k > leadingColumn; k--) {
					addRowMultiple(nullSpace, leadingColumn, k, field.multiply(field.negate(rowEchelonForm[i][k]), field.invert(rowEchelonForm[i][leadingColumn])));
				}
			}
			field.valueOfInPlace(nullSpace[i]);
		}
		
		field.valueOfInPlace(nullSpace);

		return nullSpace;
	}
	
	
	public static int rank(int[][] A, IntField field) {
		int[][] columnSpace = computeColumnSpace(A, field);
		if (columnSpace.length == 0) {
			return 0;
		}
		/*
		 * Just return the total number of columns in the column space matrix
		 */
		return columnSpace[0].length;
	}
}
