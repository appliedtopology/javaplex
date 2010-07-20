package edu.stanford.math.plex4.array_utility;

import edu.stanford.math.plex4.utility.ExceptionUtility;

public class IntArrayMath {
	/*
	 * Vector operations
	 */
	
	public static int[] sum(int[] vector1,
			int[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector1[i] + vector2[i];
		}
		return result;
	}
	
	public static int[] difference(int[] vector1,
			int[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector1[i] - vector2[i];
		}
		return result;
	}
	
	public static int[] componentwiseProduct(int[] vector1,
			int[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector1[i] * vector2[i];
		}
		return result;
	}
	
	public static int[] scalarAdd(int[] vector, int scalar) {
		int n = vector.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i] + scalar;
		}
		return result;
	}
	
	public static int[] scalarMultiply(int[] vector, int scalar) {
		int n = vector.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i] * scalar;
		}
		return result;
	}
	
	public static double[] scalarAdd(int[] vector, double scalar) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i] * scalar;
		}
		return result;
	}
	
	public static double[] scalarMultiply(int[] vector, double scalar) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i] * scalar;
		}
		return result;
	}
	
	/* 
	 * Matrix Operations
	 */
	
	public static int[][] sum(int[][] matrix1, int[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		int[][] result = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix1[i][j] + matrix2[i][j];
			}
		}
		return result;
	}
	
	public static int[][] difference(int[][] matrix1, int[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		int[][] result = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix1[i][j] - matrix2[i][j];
			}
		}
		return result;
	}
	
	public static int[][] componentwiseProduct(int[][] matrix1, int[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		int[][] result = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix1[i][j] * matrix2[i][j];
			}
		}
		return result;
	}
	
	public static int[][] product(int[][] matrix1, int[][] matrix2) {
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
					result[i][j] += matrix1[i][k] * matrix2[k][j];
				}
			}
		}
		return result;
	}
	
	public static int[] product(int[][] matrix, int[] vector) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		ExceptionUtility.verifyEqual(matrix[0].length, vector.length);
		int m = matrix.length;
		int n = matrix[0].length;
		int[] result = new int[m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i] += matrix[i][j] * vector[j];
			}
		}
		return result;
	}
	
	public static int[] product(int[] vector, int[][] matrix) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		ExceptionUtility.verifyEqual(vector.length, matrix.length);
		int m = matrix.length;
		int n = matrix[0].length;
		int[] result = new int[n];
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				result[j] += vector[i] * matrix[i][j];
			}
		}
		return result;
	}
	
	public static int[][] transpose(int[][] matrix) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] result = new int[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[j][i] = matrix[i][j];
			}
		}
		return result; 
	}
	
	public static double frobeniusNorm(int[][] matrix) {
		if (matrix.length == 0) {
			return 0;
		}
		int sum = 0;
		int entry = 0;
		int m = matrix.length;
		int n = matrix[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				entry = matrix[i][j];
				sum += entry * entry;
			}
		}
		return Math.sqrt(sum);
	}
	
	public static int infinityNorm(int[][] matrix) {
		if (matrix.length == 0) {
			return 0;
		}
		int max = 0;
		int sum = 0;
		int m = matrix.length;
		int n = matrix[0].length;
		for (int i = 0; i < m; i++) {
			sum = 0;
			for (int j = 0; j < n; j++) {
				sum += Math.abs(matrix[i][j]);
			}
			if (sum > max) {
				max = sum;
			}
		}
		return max;
	}
	
	public static int oneNorm(int[][] matrix) {
		return infinityNorm(transpose(matrix));
	}
	
	public static int innerProduct(int[][] matrix1, int[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		int sum = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += matrix1[i][j] * matrix2[i][j];
			}
		}
		return sum;
	}
	
	public static int trace(int[][] matrix) {
		if (matrix.length == 0) {
			return 0;
		}
		int sum = 0;
		int m = Math.min(matrix.length, matrix[0].length);
		for (int i = 0; i < m; i++) {
			sum += matrix[i][i];
		}
		return sum;
	}
	
	/*
	 * Componentwise functions
	 */
	
	public static int[] abs(int[] vector) {
		int n = vector.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = Math.abs(vector[i]);
		}
		return result;
	}
	
	/*
	 * Aggregate functions
	 */
	
	public static int max(int[] vector) {
		ExceptionUtility.verifyPositive(vector.length);
		int max = vector[0];
		int n = vector.length;
		for (int i = 0; i < n; i++) {
			if (vector[i] > max) { 
				max = vector[i];
			}
		}
		return max;
	}
	
	public static int min(int[] vector) {
		ExceptionUtility.verifyPositive(vector.length);
		int min = vector[0];
		int n = vector.length;
		for (int i = 0; i < n; i++) {
			if (vector[i] < min) { 
				min = vector[i];
			}
		}
		return min;
	}
	
	public static int sum(int[] vector) {
		int sum = 0;
		for (int i = 0; i < vector.length; i++) {
			sum += vector[i];
		}
		return sum;
	}
	
	public static double product(int[] vector) {
		int product = 1;
		for (int i = 0; i < vector.length; i++) {
			product *= vector[i];
		}
		return product;
	}
	
	public static double mean(int[] vector) {
		if (vector.length == 0) {
			return 0;
		}
		return ((double) sum(vector)) / ((double) vector.length);
	}

	public static double standardDeviation(int[] array) {
		if (array.length <= 1) {
			return 0;
		}
		int n = array.length;
		double mean = mean(array);
		double sd = 0;
		for (int i = 0; i < n; i++) {
			sd += (array[i] - mean) * (array[i] - mean);
		}
		sd = Math.sqrt(sd / ((int) (n - 1)));
		return sd;
	}
	
	/**
	 * Returns the distance between x1 and x2 using the p-norm, where x1 and x2
	 * are vectors with the same number of components.
	 * 
	 * @param x1
	 * @param x2
	 * @param p
	 * @return
	 */
	public static double distance(int[] x1, int[] x2, double p) {
		double dist = 0;
		ExceptionUtility.verifyEqual(x1.length, x2.length);
		for (int i = 0; i < x1.length; i++) {
			dist += Math.pow(Math.abs(x1[i] - x2[i]), p);
		}
		dist = Math.pow(dist, 1.0 / p);
		return dist;
	}
	
	public static double norm(int[] vector, double p) {
		int n = vector.length;
		double norm = 0;
		for (int i = 0; i < n; i++) {
			norm += Math.pow(vector[i], p);
		}
		return Math.pow(norm, 1.0 / p);
	}
	
	public static int infinityNorm(int[] vector) {
		return max(abs(vector));
	}
	
	public static int innerProduct(int[] vector1, int[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		int innerProduct = 0;
		for (int i = 0; i < n; i++) {
			innerProduct += vector1[i] * vector2[i];
		}
		return innerProduct;
	}
}
