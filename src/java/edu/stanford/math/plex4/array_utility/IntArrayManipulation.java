package edu.stanford.math.plex4.array_utility;

import java.util.List;

import edu.stanford.math.plex4.utility.ExceptionUtility;

public class IntArrayManipulation {
	/*
	 * Slice extraction and resizing
	 */
	
	public static int[] extractRow(int[][] matrix, int row) {
		ExceptionUtility.verifyLessThan(row, matrix.length);
		ExceptionUtility.verifyNonNegative(row);
		int[] result = matrix[row];
		return result;
	}
	
	public static int[] extractColumn(int[][] matrix, int column) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyLessThan(column, matrix[0].length);
		ExceptionUtility.verifyNonNegative(column);
		int m = matrix.length;
		int[] result = new int[m];
		for (int i = 0; i < m; i++) {
			result[i] = matrix[i][column];
		}
		return result;
	}
	
	public static int[][] stackRows(int[] vector1, int[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		int m = 2;
		int[][] result = new int[m][n];
		for (int j = 0; j < n; j++) {
			result[0][j] = vector1[j];
			result[1][j] = vector2[j];
		}
		return result;
	}
	
	public static int[][] stackColumns(int[] vector1, int[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = 2;
		int m = vector1.length;
		int[][] result = new int[m][n];
		for (int i = 0; i < m; i++) {
			result[i][0] = vector1[i];
			result[i][1] = vector2[i];
		}
		return result;
	}
	
	public static int[][] stackRows(List<int[]> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int n = vectors.get(0).length;
		int m = vectors.size();
		int[][] result = new int[m][n];
		for (int i = 0; i < m; i++) {
			ExceptionUtility.verifyEqual(n, vectors.get(i).length);
			for (int j = 0; j < n; j++) {
				result[i][j] = vectors.get(i)[j];
			}
		}
		return result;
	}
	
	public static int[][] stackColumns(List<int[]> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int m = vectors.get(0).length;
		int n = vectors.size();
		int[][] result = new int[m][n];
		for (int j = 0; j < n; j++) {
			ExceptionUtility.verifyEqual(m, vectors.get(j).length);
			for (int i = 0; i < m; i++) {
				result[i][j] = vectors.get(i)[j];
			}
		}
		return result;
	}
	
	public static int[] concatenate(int[] vector1, int[] vector2) {
		int n1 = vector1.length;
		int n2 = vector2.length;
		int[] result = new int[n1 + n2];
		for (int i = 0; i < n1; i++) {
			result[i] = vector1[i];
		}
		for (int i = 0; i < n2; i++) {
			result[i + n1] = vector2[i];
		}
		return result;
	}
	
	public static int[] concatenate(List<int[]> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int totalSize = 0;
		int numVectors = vectors.size();
		for (int vectorIndex = 0; vectorIndex < numVectors; vectorIndex++) {
			totalSize += vectors.get(vectorIndex).length;
		}
		int[] result = new int[totalSize];
		int writeIndex = 0;
		for (int vectorIndex = 0; vectorIndex < numVectors; vectorIndex++) {
			for (int i = 0; i < vectors.get(vectorIndex).length; i++) {
				result[writeIndex] = vectors.get(vectorIndex)[i];
				writeIndex++;
			}
		}
		return result;
	}
	
	public static int[][] embedAsRowMatrix(int[] vector) {
		int m = 1;
		int n = vector.length;
		int[][] result = new int[m][n];
		for (int j = 0; j < n; j++) {
			result[0][j] = vector[j];
		}
		return result;
	}
	
	public static int[][] embedAsColumnMatrix(int[] vector) {
		int m = vector.length;
		int n = 1;
		int[][] result = new int[m][n];
		for (int i = 0; i < m; i++) {
			result[i][0] = vector[i];
		}
		return result;
	}
	
	public static int[][] insertRow(int[][] matrix, int[] vector, int index) {
		if (matrix.length == 0) {
			return embedAsRowMatrix(vector);
		}
		ExceptionUtility.verifyEqual(matrix[0].length, vector.length);
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyLessThan(index, matrix.length + 1);
		int m = matrix.length;
		int n = matrix[0].length;

		int[][] result = new int[m + 1][n];
		for (int i = 0; i < index; i++) {
			for (int j = 0; j < index; j++) {
				result[i][j] = matrix[i][j];
			}
		}
		for (int j = 0; j < n; j++) {
			result[index][j] = vector[j];
		}
		for (int i = index; i < m; i++) {
			for (int j = 0; j < index; j++) {
				result[i + 1][j] = matrix[i][j];
			}
		}
		return result;
	}
	
	public static int[][] insertColumn(int[][] matrix, int[] vector, int index) {
		if (matrix.length == 0) {
			return matrix;
		}
		ExceptionUtility.verifyEqual(matrix.length, vector.length);
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyLessThan(index, matrix[0].length + 1);
		int m = matrix.length;
		int n = matrix[0].length;

		int[][] result = new int[m][n + 1];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < index; j++) {
				result[i][j] = matrix[i][j];
			}
			
			result[i][index] = vector[i];
			
			for (int j = index; j < n; j++) {
				result[i][j + 1] = matrix[i][j];
			}
		}
		return result;
	}
	
	public static int[][] appendRow(int[][] matrix, int[] vector) {
		return insertRow(matrix, vector, matrix.length);
	}
		
	public static int[][] appendColumn(int[][] matrix, int[] vector) {
		if (matrix.length == 0) {
			return embedAsColumnMatrix(vector);
		}
		return insertColumn(matrix, vector, matrix[0].length);
	}
	
	public static int[][] prependRow(int[] vector, int[][] matrix) {
		return insertRow(matrix, vector, 0);
	}
	
	public static int[][] prependColumn(int[] vector, int[][] matrix) {
		return insertColumn(matrix, vector, 0);
	}
	
	public static int[][] removeRow(int[][] matrix, int index) {
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyLessThan(index, matrix.length);
		ExceptionUtility.verifyPositive(matrix.length);
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] result = new int[m - 1][n];
		for (int i = 0; i < index; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix[i][j];
			}
		}
		for (int i = index + 1; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i - 1][j] = matrix[i][j];
			}
		}
		
		return result;
	}
	
	public static int[][] removeColumn(int[][] matrix, int index) {
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		ExceptionUtility.verifyLessThan(index, matrix[0].length);
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] result = new int[m][n - 1];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < index; j++) {
				result[i][j] = matrix[i][j];
			}
			for (int j = index + 1; j < n; j++) {
				result[i][j - 1] = matrix[i][j];
			}
		}
		return result;
	}
	
	public static int[] sortDescending(int[] array) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		java.util.Arrays.sort(result);
		IntArrayManipulation.reverse(result);
		return result;
	}

	public static int[] sortAscending(int[] array) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		java.util.Arrays.sort(result);
		return result;
	}
	
	public static void reverse(int[] array) {
		for (int left = 0, right = array.length - 1; left < right; left++, right--) {
			// exchange the first and last
			int temp = array[left];
			array[left] = array[right];
			array[right] = temp;
		}
	}
	
	public static int[] firstDifferences(int[] vector) {
		ExceptionUtility.verifyLessThan(1, vector.length);
		int[] result = new int[vector.length - 1];
		for (int i = 1; i < vector.length; i++) {
			result[i - 1] = vector[i] - vector[i - 1];
		}
		return result;
	}	
}
