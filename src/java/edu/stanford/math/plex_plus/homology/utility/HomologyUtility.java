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
}
