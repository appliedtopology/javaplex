package edu.stanford.math.plex_plus.homology.utility;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class contains static utility functions to aid
 * the homology computations.
 * 
 * @author Andrew Tausz
 *
 */
public class HomologyUtility {
	public static int compareIntArrays (int[] array1, int[] array2) {
		ExceptionUtility.verifyNonNull(array1);
		ExceptionUtility.verifyNonNull(array2);
		ExceptionUtility.verifyEqual(array1.length, array2.length);
		int length1 = array1.length;
		int length2 = array2.length;
		int count = (length2 > length1) ? length2 : length1;
		for (int i = 0; i < count; i++) {
			if (array1[i] != array2[i])
				return (array1[i] - array2[i]);
		}
		return (length1 - length2);
	}
}
