package edu.stanford.math.plex_plus.homology.utility;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * This class contains static utility functions to aid
 * the homology computations.
 * 
 * @author Andrew Tausz
 *
 */
public class HomologyUtility {
	/**
	 * This array caches the standard sequence of boundary coefficients. We 
	 * construct a cache since we do not want to keep re-creating arrays with
	 * coefficients [1, -1, 1, -1, ...], therefore we construct them once.
	 */
	private static int[][] defaultBoundaryCoefficients = null;
	
	/**
	 * This stores the maximum coefficient array size stored in the cache.
	 */
	private static int defaultCoefficientCacheSize = 8;
	
	/**
	 * This function initializes the set of default boundary coefficients. It
	 * constructs an array of the form
	 * [[],
	 *  [1],
	 *  [1, -1],
	 *  [1, -1, 1]
	 *  [1, -1, 1, -1],
	 *  ... ]
	 */
	private static void initializeDefaultBoundaryCoefficients() {
		defaultBoundaryCoefficients = new int[defaultCoefficientCacheSize][];
		for (int i = 0; i < defaultCoefficientCacheSize; i++) {
			defaultBoundaryCoefficients[i] = new int[i];
			for (int j = 0; j < i; j++) {
				defaultBoundaryCoefficients[i][j] = (j % 2 == 0 ? 1 : -1);
			}
		}
	}
	
	/**
	 * This function returns the standard sequence of boundary coefficients
	 * used in simplicial homology, defined by c_i = (-1)^i. For small values
	 * of the desired length, it returns a precomputed array. This optimization
	 * is performed because this operation is done very often during homology
	 * and cohomology calculations. Also, almost all calls to this function will
	 * involve small values of the argument length, since most of the homological
	 * features of interest are in low dimensions. Nevertheless, this function
	 * will return the desired sequence even if the cache is not populated with
	 * the desired array.
	 * 
	 * @param length the length of the array to get
	 * @return an array of with specified length containing the numbers (-1)^i
	 */
	public static int[] getDefaultBoundaryCoefficients(int length) {
		ExceptionUtility.verifyNonNegative(length);
		
		// initialize cache if necessary
		if (defaultBoundaryCoefficients == null) {
			initializeDefaultBoundaryCoefficients();
		}
		
		// return coefficients from cache if they exist
		if (length < defaultCoefficientCacheSize) {
			return defaultBoundaryCoefficients[length];
		}
		
		// the desired length exceeds that of those stored
		// in the cache - in this case just construct it
		
		int[] boundaryCoefficients = new int[length];

		for (int i = 0; i < length; i++) {
			boundaryCoefficients[i] = (i % 2 == 0 ? 1 : -1);
		}
		
		return boundaryCoefficients;
	}

	/**
	 * This function performs a comparison of two arrays of ints. It 
	 * returns a positive value if the first array is greater than the
	 * second, a negative value if the first array is less than the 
	 * second, and zero if they are equal. The comparison is done as 
	 * follows. First the lengths are compared. If they are unequal, then
	 * it returns the comparison of the lengths. If the array lengths
	 * are equal, it performs a dictionary comparison starting from the
	 * 0-th entry in the arrays.
	 * 
	 * @param array1 the first array parameter
	 * @param array2 the second array parameter
	 * @return 1 if array1 > array2, 0 if array1 == array2, -1 if array1 < array2
	 */
	public static int compareIntArrays (int[] array1, int[] array2) {
		ExceptionUtility.verifyNonNull(array1);
		ExceptionUtility.verifyNonNull(array2);
		int length1 = array1.length;
		int length2 = array2.length;
		if (length1 > length2) {
			return 1;
		} else if (length1 < length2) {
			return -1;
		}
		for (int i = 0; i < length1; i++) {
			if (array1[i] != array2[i])
				return (array1[i] - array2[i]);
		}
		return 0;
	}

	/**
	 * This function returns a new array which contains the same entries as the
	 * supplied array except that the element at the specified index has been
	 * removed and the proceeding elements have been shifted down by one.
	 * 
	 * @param array
	 * @param index
	 * @return
	 */
	public static int[] removeIndex(int[] array, int index) {
		ExceptionUtility.verifyNonNull(array);
		ExceptionUtility.verifyIndex(array.length, index);
		ExceptionUtility.verifyNonEmpty(array);
		
		int[] result = new int[array.length - 1];
		int newIndex = 0;
		int oldIndex = 0;
		while (oldIndex < array.length) {
			if (oldIndex == index) {
				oldIndex++;
			} else {
				result[newIndex] = array[oldIndex];
				newIndex++;
				oldIndex++;
			}
		}
		return result;
	}
	
	/**
	 * This function appends the argument newValue to the end of the
	 * supplied array. It does not modify the passed in array.
	 * 
	 * @param array the array to append to
	 * @param newValue the value to append
	 * @return the supplied array with newValue appended
	 */
	public static int[] appendToArray(int[] array, int newValue) {
		ExceptionUtility.verifyNonNull(array);
		int[] result = new int[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		result[array.length] = newValue;
		return result;
	}
	
	/**
	 * This function returns the first k entries of the supplied array.
	 * 
	 * @param array
	 * @param k
	 * @return the subarray array[0, ..., k]
	 */
	public static int[] lowerEntries(int[] array, int k) {
		ExceptionUtility.verifyNonNull(array);
		ExceptionUtility.verifyNonNegative(k);
		ExceptionUtility.verifyLessThan(k, array.length);
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = array[i];
		}
		return result;
	}
	
	/**
	 * This function returns the last entries of the supplied array
	 * starting from the index k.
	 * 
	 * @param array
	 * @param k
	 * @return the subarray array[k, ..., array.length - 1]
	 */
	public static int[] upperEntries(int[] array, int k) {
		ExceptionUtility.verifyNonNull(array);
		ExceptionUtility.verifyNonNegative(k);
		ExceptionUtility.verifyLessThan(k, array.length);
		int n = array.length;
		int[] result = new int[n - k];
		for (int i = k; i < n; i++) {
			result[i - k] = array[i];
		}
		return result;
	}
	
	/**
	 * This function computes the intersection between two sets of 
	 * integers.
	 * 
	 * @param set1 the first set
	 * @param set2 the second set
	 * @return a TIntSet containing elements common to both input sets
	 */
	public static TIntSet computeIntersection(TIntSet set1, TIntSet set2) {
		TIntSet smallerSet = null;
		TIntSet largerSet = null;
		
		/*
		 * Let's identify the smaller and larger sets,
		 * so that we only need to iterate through the smaller one.
		 */
		if (set1.size() < set2.size()) {
			smallerSet = set1;
			largerSet = set2;
		} else {
			smallerSet = set2;
			largerSet = set1;
		}
		
		TIntHashSet intersection = new TIntHashSet();
		
		TIntIterator iterator = smallerSet.iterator();
		while (iterator.hasNext()) {
			int element = iterator.next();
			if (largerSet.contains(element)) {
				intersection.add(element);
			}
		}
		
		return intersection;
	}
}
