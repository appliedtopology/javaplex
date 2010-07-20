package edu.stanford.math.plex4.array_utility;

import java.util.Arrays;

import edu.stanford.math.plex4.utility.ExceptionUtility;

public class DoubleArrayQuery {
	/**
	 * This function returns the indices of the values in the
	 * array that are equal to the value to search for.
	 * 
	 * @param array the array to search in
	 * @param value the value to search for
	 * @return the indices of those entries equal to value
	 */
	public static int[] findIndices(double[] array, double value) {
		ExceptionUtility.verifyNonNull(array);
		
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
	 * This function returns true if the array contains the given value,
	 * and false otherwise. It does not assume anything about the ordering
	 * of the array, and therefore searches the entire array.
	 * 
	 * @param array the array to search
	 * @param value the value to search for
	 * @return true if the array contains the specified value and false otherwise
	 */
	public static boolean contains(double[] array, double value) {
		ExceptionUtility.verifyNonNull(array);
		int n = array.length;
		for (int i = 0; i < n; i++) {
			if (array[i] == value) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This function returns a new array with the contents of the supplied array with
	 * the specified value removed. Other than the removal of the designated value, 
	 * all of the other elements are the same and are returned in the same relative
	 * order.
	 * 
	 * @param array the array to search
	 * @param value the value to remove
	 * @return a new array with the specified value removed
	 */
	public static double[] removeElement(double[] array, double value) {
		ExceptionUtility.verifyNonNull(array);
		int n = array.length;
		int occurrences = 0;
		for (int i = 0; i < n; i++) {
			if (array[i] == value) {
				occurrences++;
			}
		}
		double[] result = new double[n - occurrences];
		int j = 0;
		for (int i = 0; i < n; i++) {
			if (array[i] != value) {
				result[j] = array[i];
				j++;
			}
		}
		return result;
	}
	
	/**
	 * This function returns the indices of the k lowest smallest. Note that
	 * it requires that k be less than or equal to the length of the array. It
	 * returns an array containing the indices of the k smallest elements.
	 * 
	 * @param array
	 * @param k
	 * @return
	 */
	public static int[] getMinimumIndices(double[] array, int k) {
		ExceptionUtility.verifyNonNull(array);
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
					if (array[i] < minValue && (!IntArrayQuery.contains(indices, i))) {
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
	public static double[] getArraySubset(double[] array, int[] indices) {
		ExceptionUtility.verifyNonNull(array);
		ExceptionUtility.verifyNonNull(indices);
		double[] subArray = new double[indices.length];
		for (int i = 0; i < indices.length; i++) {
			ExceptionUtility.verifyNonNegative(indices[i]);
			ExceptionUtility.verifyLessThan(indices[i], array.length);
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
	public static double[] getArraySubsetComplement(double[] array, int[] indices) {
		ExceptionUtility.verifyNonNull(array);
		ExceptionUtility.verifyNonNull(indices);
		indices = IntArrayManipulation.sortAscending(indices);
		double[] subArray = new double[array.length - indices.length];
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
}
