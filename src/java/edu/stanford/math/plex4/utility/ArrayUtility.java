package edu.stanford.math.plex4.utility;



/**
 * This class contains various static functions which perform array operations.
 */
public class ArrayUtility {
	
	/*
	 * Data type conversions
	 */
	
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
	
	
	
	
	
	
	
	/*
	 * Array filling and generation
	 */

	
	
	
	
	/*
	 * DOUBLE SECTION
	 */
	
	/*
	 * Slice extraction and resizing
	 */
	
	
	
	
	
	
	
	/*
	 * Array functions
	 */

	
	
	/*
	 * INT SECTION
	 */
	
	
	
	
	
	/*
	 * Array functions
	 */

	
}
