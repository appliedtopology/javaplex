package edu.stanford.math.plex_plus.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cern.colt.matrix.DoubleMatrix1D;


/**
 * This class contains various static functions which perform array operations.
 */
public class ArrayUtility {
	
	/*
	 * Data type conversions
	 */
	
	public static double[] toDoubleArray(int[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i];
		}
		return result;
	}
	
	public static int[] toIntArray(double[] vector) {
		int n = vector.length;
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = (int) vector[i];
		}
		return result;
	}
	
	public static double[] toDoubleArray(DoubleMatrix1D vector) {
		int n = vector.size();
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector.getQuick(i);
		}
		return result;
	}
	
	public static double[] toDoubleArray(Collection<Double> collection) {
		int n = collection.size();
		double[] result = new double[n];
		int index = 0;
		for (Double element : collection) {
			result[index] = element;
			index++;
		}
		return result;
	}
	
	public static int[] toIntArray(Collection<Integer> collection) {
		int n = collection.size();
		int[] result = new int[n];
		int index = 0;
		for (Integer element : collection) {
			result[index] = element;
			index++;
		}
		return result;
	}
	
	/**
	 * 
	 * @param array
	 * @return a MATLAB string representation of the array
	 */
	public static String toMatlabString(int[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append("," + array[i]);
			} else {
				sb.append(array[i]);
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @param array
	 * @return a MATLAB string representation of the array
	 */
	public static String toMatlabString(double[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append("," + array[i]);
			} else {
				sb.append(array[i]);
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/*
	 * find operations
	 */
	
	/**
	 * This function returns the indices of the values in the
	 * array that are equal to the value to search for.
	 * 
	 * @param array the array to search in
	 * @param value the value to search for
	 * @return the indices of those entries equal to value
	 */
	public static int[] findIndices(int[] array, int value) {
		int matches = 0;
		int n = array.length;
		for (int i = 0; i < n; i++) {
			if (array[i] == value) {
				matches++;
			}
		}
		int[] indices = new int[matches];
		int matchIndex = 0;
		for (int i = 0; i < n; i++) {
			if (array[i] == value) {
				indices[matchIndex++] = i;
			}
		}
		return indices;
	}
	
	/**
	 * 
	 * @param array
	 * @param value
	 * @return
	 */
	public static boolean contains(int[] array, int value) {
		int n = array.length;
		for (int i = 0; i < n; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}
	
	public static int[] removeElement(int[] array, int value) {
		int n = array.length;
		int occurrences = 0;
		for (int i = 0; i < n; i++) {
			if (array[i] == value) {
				occurrences++;
			}
		}
		int[] result = new int[n - occurrences];
		int j = 0;
		for (int i = 0; i < n; i++) {
			if (array[i] != value) {
				result[j] = array[i];
				j++;
			}
		}
		return result;
	}
	
	public static int[] getMinimumIndices(double[] array, int k) {
		int n = array.length;
		ExceptionUtility.verifyGreaterThanOrEqual(n, k);
		int[] indices = new int[k];
		double[] minValues = new double[k];
		Arrays.fill(indices, -1);
		Arrays.fill(minValues, Double.MAX_VALUE);
		double minValue = Double.MAX_VALUE;
		int minIndex = 0;
		for (int j = 0; j < k; j++) {
			minValue = Double.MAX_VALUE;
			if (j == 0) {
				for (int i = 0; i < n; i++) {
					if (array[i] < minValue) {
						minValue = array[i];
						minIndex = i;
					}
				}
			} else {
				for (int i = 0; i < n; i++) {
					if (array[i] < minValue && (!contains(indices, i))) {
						minValue = array[i];
						minIndex = i;
					}
				}
			}
			indices[j] = minIndex;
			minValues[j] = minValue;
		}
		return indices;
	}
	
	/**
	 * This function extracts the entries of the specified array
	 * which have index in the indices array.
	 * @param array the array of values
	 * @param indices the indices to select
	 * @return a sub-array containing those values at the specified indices
	 */
	public static int[] getArraySubset(int[] array, int[] indices) {
		int[] subArray = new int[indices.length];
		for (int i = 0; i < indices.length; i++) {
			subArray[i] = array[indices[i]];
		}
		return subArray;
	}
	
	/**
	 * This function extracts the entries of the array which have index
	 * not in the indices array. In other words it performs the complement
	 * of getArraySubset.
	 * 
	 * @param array the array of values
	 * @param indices the indices to omit
	 * @return a sub-array containing the values excluding those at the specified indices
	 */
	public static int[] getArraySubsetComplement(int[] array, int[] indices) {
		indices = ArrayUtility.sortAscending(indices);
		int[] subArray = new int[array.length - indices.length];
		int subArrayIndex = 0;
		int omissionIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (omissionIndex < indices.length && i == indices[omissionIndex]) {
				omissionIndex++;
			} else {
				subArray[subArrayIndex++] = array[i];
			}
		}
		return subArray;
	}
	
	public static int[] getCounts(int[] array) {
		int maxValue = max(array);
		int[] counts = new int[maxValue + 1];
		for (int i = 0; i < array.length; i++) {
			counts[array[i]]++;
		}
		return counts;
	}
	
	/*
	 * Array filling and generation
	 */

	/**
	 * Generates an array consisting of the integers {start, ... , end - 1}
	 * @param start The start element
	 * @param end One past the last element
	 * @return
	 */
	public static int[] range(int start, int end) {
		int length = end - start;
		int[] values = new int[length];
		for (int i = 0; i < length; i++) {
			values[i] = start + i;
		}
		return values;
	}
	
	public static int[] range(int start, int end, int step) {
		int length = (end - start) / step;
		int[] values = new int[length];
		for (int i = 0; i < length; i ++) {
			values[i] = start + i * step;
		}
		return values;
	}
	
	/**
	 * This function returns an array of equally spaced ascending points
	 * starting at start and ending at end, of specified size. It is very
	 * similar to the linspace function in Matlab.
	 * 
	 * @param start the start value
	 * @param end the end value
	 * @param size the size of the array to create
	 * @return an array of linearly spaced points from start to end
	 */
	public static double[] linspace(double start, double end, int size) {
		ExceptionUtility.verifyGreaterThanOrEqual(size, 2);
		ExceptionUtility.verifyGreaterThan(end, start);
		double multiplier = (end - start) / (size - 1);
		double[] values = new double[size];
		for (int i = 0; i < size; i++) {
			values[i] = start + i * multiplier;
		}		
		return values;
	}
	
	/*
	 * DOUBLE SECTION
	 */
	
	/*
	 * Slice extraction and resizing
	 */
	
	public static double[] extractRow(double[][] matrix, int row) {
		ExceptionUtility.verifyLessThan(row, matrix.length);
		ExceptionUtility.verifyNonNegative(row);
		double[] result = matrix[row];
		return result;
	}
	
	public static double[] extractColumn(double[][] matrix, int column) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyLessThan(column, matrix[0].length);
		ExceptionUtility.verifyNonNegative(column);
		int m = matrix[0].length;
		double[] result = new double[m];
		for (int i = 0; i < m; i++) {
			result[i] = matrix[i][column];
		}
		return result;
	}
	
	public static double[][] stackRows(double[] vector1, double[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		int m = 2;
		double[][] result = new double[m][n];
		for (int j = 0; j < n; j++) {
			result[0][j] = vector1[j];
			result[1][j] = vector2[j];
		}
		return result;
	}
	
	public static double[][] stackColumns(double[] vector1, double[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = 2;
		int m = vector1.length;
		double[][] result = new double[m][n];
		for (int i = 0; i < m; i++) {
			result[i][0] = vector1[i];
			result[i][1] = vector2[i];
		}
		return result;
	}
	
	public static double[][] stackRows(List<double[]> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int n = vectors.get(0).length;
		int m = vectors.size();
		double[][] result = new double[m][n];
		for (int i = 0; i < m; i++) {
			ExceptionUtility.verifyEqual(n, vectors.get(i).length);
			for (int j = 0; j < n; j++) {
				result[i][j] = vectors.get(i)[j];
			}
		}
		return result;
	}
	
	public static double[][] stackColumns(List<double[]> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int m = vectors.get(0).length;
		int n = vectors.size();
		double[][] result = new double[m][n];
		for (int j = 0; j < n; j++) {
			ExceptionUtility.verifyEqual(m, vectors.get(j).length);
			for (int i = 0; i < m; i++) {
				result[i][j] = vectors.get(j)[i];
			}
		}
		return result;
	}
	
	public static double[] concatenate(double[] vector1, double[] vector2) {
		int n1 = vector1.length;
		int n2 = vector2.length;
		double[] result = new double[n1 + n2];
		for (int i = 0; i < n1; i++) {
			result[i] = vector1[i];
		}
		for (int i = 0; i < n2; i++) {
			result[i + n1] = vector2[i];
		}
		return result;
	}
	
	public static double[] concatenate(List<double[]> vectors) {
		ExceptionUtility.verifyPositive(vectors.size());
		int totalSize = 0;
		int numVectors = vectors.size();
		for (int vectorIndex = 0; vectorIndex < numVectors; vectorIndex++) {
			totalSize += vectors.get(vectorIndex).length;
		}
		double[] result = new double[totalSize];
		int writeIndex = 0;
		for (int vectorIndex = 0; vectorIndex < numVectors; vectorIndex++) {
			for (int i = 0; i < vectors.get(vectorIndex).length; i++) {
				result[writeIndex] = vectors.get(vectorIndex)[i];
				writeIndex++;
			}
		}
		return result;
	}
	
	public static double[][] embedAsRowMatrix(double[] vector) {
		int m = 1;
		int n = vector.length;
		double[][] result = new double[m][n];
		for (int j = 0; j < n; j++) {
			result[0][j] = vector[j];
		}
		return result;
	}
	
	public static double[][] embedAsColumnMatrix(double[] vector) {
		int m = vector.length;
		int n = 1;
		double[][] result = new double[m][n];
		for (int i = 0; i < m; i++) {
			result[i][0] = vector[i];
		}
		return result;
	}
	
	public static double[][] insertRow(double[][] matrix, double[] vector, int index) {
		if (matrix.length == 0) {
			return embedAsRowMatrix(vector);
		}
		ExceptionUtility.verifyEqual(matrix[0].length, vector.length);
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyLessThan(index, matrix.length + 1);
		int m = matrix.length;
		int n = matrix[0].length;

		double[][] result = new double[m + 1][n];
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
	
	public static double[][] insertColumn(double[][] matrix, double[] vector, int index) {
		if (matrix.length == 0) {
			return matrix;
		}
		ExceptionUtility.verifyEqual(matrix.length, vector.length);
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyLessThan(index, matrix[0].length + 1);
		int m = matrix.length;
		int n = matrix[0].length;

		double[][] result = new double[m][n + 1];
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
	
	public static double[][] appendRow(double[][] matrix, double[] vector) {
		return insertRow(matrix, vector, matrix.length);
	}
		
	public static double[][] appendColumn(double[][] matrix, double[] vector) {
		if (matrix.length == 0) {
			return embedAsColumnMatrix(vector);
		}
		return insertColumn(matrix, vector, matrix[0].length);
	}
	
	public static double[][] prependRow(double[] vector, double[][] matrix) {
		return insertRow(matrix, vector, 0);
	}
	
	public static double[][] prependColumn(double[] vector, double[][] matrix) {
		return insertColumn(matrix, vector, 0);
	}
	
	public static double[][] removeRow(double[][] matrix, int index) {
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyLessThan(index, matrix.length);
		ExceptionUtility.verifyPositive(matrix.length);
		int m = matrix.length;
		int n = matrix[0].length;
		double[][] result = new double[m - 1][n];
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
	
	public static double[][] removeColumn(double[][] matrix, int index) {
		ExceptionUtility.verifyNonNegative(index);
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		ExceptionUtility.verifyLessThan(index, matrix[0].length);
		int m = matrix.length;
		int n = matrix[0].length;
		double[][] result = new double[m][n - 1];
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
	
	/*
	 * Vector operations
	 */
	
	public static double[] negate(double[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = -vector[i];
		}
		return result;
	}
	
	public static double[] sum(double[] vector1,
			double[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector1[i] + vector2[i];
		}
		return result;
	}
	
	public static double[] difference(double[] vector1,
			double[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector1[i] - vector2[i];
		}
		return result;
	}
	
	public static double[] componentwiseProduct(double[] vector1,
			double[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector1[i] * vector2[i];
		}
		return result;
	}
	
	public static double[] scalarAdd(double[] vector, double scalar) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = vector[i] + scalar;
		}
		return result;
	}
	
	public static double[] scalarMultiply(double[] vector, double scalar) {
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
	
	public static double[][] sum(double[][] matrix1, double[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		double[][] result = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix1[i][j] + matrix2[i][j];
			}
		}
		return result;
	}
	
	public static double[][] difference(double[][] matrix1, double[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		double[][] result = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix1[i][j] - matrix2[i][j];
			}
		}
		return result;
	}
	
	public static double[][] componentwiseProduct(double[][] matrix1, double[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		double[][] result = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = matrix1[i][j] * matrix2[i][j];
			}
		}
		return result;
	}
	
	public static double[][] product(double[][] matrix1, double[][] matrix2) {
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyPositive(matrix2.length);
		ExceptionUtility.verifyPositive(matrix1[0].length);
		ExceptionUtility.verifyPositive(matrix1[0].length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2.length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		int p = matrix2[0].length;
		
		double[][] result = new double[m][p];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < p; j++) {
				for (int k = 0; k < n; k++) {
					result[i][j] += matrix1[i][k] * matrix2[k][j];
				}
			}
		}
		return result;
	}
	
	public static double[] product(double[][] matrix, double[] vector) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		ExceptionUtility.verifyEqual(matrix[0].length, vector.length);
		int m = matrix.length;
		int n = matrix[0].length;
		double[] result = new double[m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i] += matrix[i][j] * vector[j];
			}
		}
		return result;
	}
	
	public static double[] product(double[] vector, double[][] matrix) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		ExceptionUtility.verifyEqual(vector.length, matrix.length);
		int m = matrix.length;
		int n = matrix[0].length;
		double[] result = new double[n];
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				result[j] += vector[i] * matrix[i][j];
			}
		}
		return result;
	}
	
	public static double[][] transpose(double[][] matrix) {
		ExceptionUtility.verifyPositive(matrix.length);
		ExceptionUtility.verifyPositive(matrix[0].length);
		int m = matrix.length;
		int n = matrix[0].length;
		double[][] result = new double[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[j][i] = matrix[i][j];
			}
		}
		return result; 
	}
	
	public static double frobeniusNorm(double[][] matrix) {
		if (matrix.length == 0) {
			return 0;
		}
		double sum = 0;
		double entry = 0;
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
	
	public static double infinityNorm(double[][] matrix) {
		if (matrix.length == 0) {
			return 0;
		}
		double max = 0;
		double sum = 0;
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
	
	public static double oneNorm(double[][] matrix) {
		return infinityNorm(transpose(matrix));
	}
	
	public static double innerProduct(double[][] matrix1, double[][] matrix2) {
		ExceptionUtility.verifyEqual(matrix1.length, matrix2.length);
		ExceptionUtility.verifyPositive(matrix1.length);
		ExceptionUtility.verifyEqual(matrix1[0].length, matrix2[0].length);
		int m = matrix1.length;
		int n = matrix1[0].length;
		double sum = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				sum += matrix1[i][j] * matrix2[i][j];
			}
		}
		return sum;
	}
	
	public static double trace(double[][] matrix) {
		if (matrix.length == 0) {
			return 0;
		}
		double sum = 0;
		int m = Math.min(matrix.length, matrix[0].length);
		for (int i = 0; i < m; i++) {
			sum += matrix[i][i];
		}
		return sum;
	}
	
	public static void normalizeRows(double[][] matrix, double p) {
		ExceptionUtility.verifyNonNull(matrix);
		int m = matrix.length;
		if (m == 0) {
			return;
		}
		int n = matrix[0].length;
		double norm = 0;
		for (int i = 0; i < m; i++) {
			norm = ArrayUtility.norm(matrix[i], p);
			if (norm != 0) {
				for (int j = 0; j < n; j++) {
					matrix[i][j] /= norm;
				}
			}
		}
	}
	
	/*
	 * Componentwise functions
	 */
	
	public static double[] abs(double[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = Math.abs(vector[i]);
		}
		return result;
	}
	
	public static double[] sqrt(double[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = Math.sqrt(vector[i]);
		}
		return result;
	}
	
	public static double[] log(double[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			result[i] = Math.log(vector[i]);
		}
		return result;
	}
	
	public static double[] reciprocal(double[] vector) {
		int n = vector.length;
		double[] result = new double[n];
		for (int i = 0; i < n; i++) {
			if (vector[i] != 0) {
				result[i] = 1.0 / vector[i];
			}
		}
		return result;
	}
	
	/*
	 * Aggregate functions
	 */
	
	public static double max(double[] vector) {
		ExceptionUtility.verifyPositive(vector.length);
		double max = vector[0];
		int n = vector.length;
		for (int i = 0; i < n; i++) {
			if (vector[i] > max) { 
				max = vector[i];
			}
		}
		return max;
	}
	
	public static double min(double[] vector) {
		ExceptionUtility.verifyPositive(vector.length);
		double min = vector[0];
		int n = vector.length;
		for (int i = 0; i < n; i++) {
			if (vector[i] < min) { 
				min = vector[i];
			}
		}
		return min;
	}
	
	public static double sum(double[] vector) {
		double sum = 0;
		for (int i = 0; i < vector.length; i++) {
			sum += vector[i];
		}
		return sum;
	}
	
	public static double product(double[] vector) {
		double product = 1;
		for (int i = 0; i < vector.length; i++) {
			product *= vector[i];
		}
		return product;
	}
	
	
	public static double mean(double[] vector) {
		if (vector.length == 0) {
			return 0;
		}
		return sum(vector) / vector.length;
	}

	public static double standardDeviation(double[] array) {
		if (array.length <= 1) {
			return 0;
		}
		int n = array.length;
		double mean = mean(array);
		double sd = 0;
		for (int i = 0; i < n; i++) {
			sd += (array[i] - mean) * (array[i] - mean);
		}
		sd = Math.sqrt(sd / ((double) (n - 1)));
		return sd;
	}
	
	public static double sampleSkewness(double[] array) {
		if (array.length < 1) {
			return 0;
		}
		int n = array.length;
		double mean = mean(array);
		double m2 = 0;
		double m3 = 0;
		for (int i = 0; i < n; i++) {
			m2 += (array[i] - mean) * (array[i] - mean);
			m3 += (array[i] - mean) * (array[i] - mean) * (array[i] - mean);
		}
		m2 = m2 / ((double) n);
		m3 = m3 / ((double) n);
		return (m3 / Math.pow(m2, 1.5));
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
	public static double distance(double[] x1, double[] x2, double p) {
		double dist = 0;
		ExceptionUtility.verifyEqual(x1.length, x2.length);
		for (int i = 0; i < x1.length; i++) {
			dist += Math.pow(Math.abs(x1[i] - x2[i]), p);
		}
		dist = Math.pow(dist, 1.0 / p);
		return dist;
	}
	
	public static double norm(double[] vector, double p) {
		int n = vector.length;
		double norm = 0;
		for (int i = 0; i < n; i++) {
			norm += Math.pow(vector[i], p);
		}
		return Math.pow(norm, 1.0 / p);
	}
	
	public static double infinityNorm(double[] vector) {
		return max(abs(vector));
	}
	
	public static double innerProduct(double[] vector1, double[] vector2) {
		ExceptionUtility.verifyEqual(vector1.length, vector2.length);
		int n = vector1.length;
		double innerProduct = 0;
		for (int i = 0; i < n; i++) {
			innerProduct += vector1[i] * vector2[i];
		}
		return innerProduct;
	}
	
	/*
	 * Array functions
	 */

	public static double[] sortDescending(double[] array) {
		double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		java.util.Arrays.sort(result);
		ArrayUtility.reverse(result);
		return result;
	}

	public static double[] sortAscending(double[] array) {
		double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		java.util.Arrays.sort(result);
		return result;
	}
	
	public static void reverse(double[] array) {
		for (int left = 0, right = array.length - 1; left < right; left++, right--) {
			// exchange the first and last
			double temp = array[left];
			array[left] = array[right];
			array[right] = temp;
		}
	}
	
	public static double[] firstDifferences(double[] vector) {
		ExceptionUtility.verifyLessThan(1, vector.length);
		double[] result = new double[vector.length - 1];
		for (int i = 1; i < vector.length; i++) {
			result[i - 1] = vector[i] - vector[i - 1];
		}
		return result;
	}
	
	/*
	 * INT SECTION
	 */
	
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
	
	/*
	 * Array functions
	 */

	public static int[] sortDescending(int[] array) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		java.util.Arrays.sort(result);
		ArrayUtility.reverse(result);
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
