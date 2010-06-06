package edu.stanford.math.plex_plus.utility;

import java.util.List;

/**
 * This class contains static helper functions for
 * manipulating arrays.
 * 
 * @author Andrew Tausz
 *
 */
public class ArrayUtility2 {
	public static <T> String toString(T[][] array) {
		ExceptionUtility.verifyNonNull(array);
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append('[');
			for (int j = 0; j < array[i].length; j++) {
				if (j > 0) {
					builder.append(", ");
				}
				builder.append(array[i][j]);
			}
			builder.append("]\n");
		}
		return builder.toString();		
	}
	
	public static String toString(int[][] array) {
		ExceptionUtility.verifyNonNull(array);
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (j > 0) {
					builder.append(", ");
				}
				builder.append(array[i][j]);
			}
			builder.append(";\n");
		}
		builder.append("]\n");
		return builder.toString();
	}
	
	public static String toString(double[][] array) {
		ExceptionUtility.verifyNonNull(array);
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (j > 0) {
					builder.append(", ");
				}
				builder.append(array[i][j]);
			}
			builder.append(";\n");
		}
		builder.append("]\n");
		return builder.toString();
	}
	
	public static String toString(int[] array) {
		ExceptionUtility.verifyNonNull(array);
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(array[i]);
		}
		builder.append("]");
		return builder.toString();		
	}
	
	public static String toString(double[] array) {
		ExceptionUtility.verifyNonNull(array);
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(array[i]);
		}
		builder.append("]");
		return builder.toString();		
	}
	
	public static <T> String toString(T[] array) {
		ExceptionUtility.verifyNonNull(array);
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(array[i].toString());
		}
		builder.append("]");
		return builder.toString();		
	}

	/**
	 * This function swaps two values in the supplied array.
	 * The array is modified in-place.
	 * 
	 * @param array the array to perform the swap on
	 * @param i the first index
	 * @param j the second index
	 */
	public static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public static <T> void swap(T[] array, int i, int j) {
		T temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public static <T> void swap(List<T> array, int i, int j) {
		T temp = array.get(i);
		array.set(i, array.get(j));
		array.set(j, temp);
	}

	public static double squaredDistance(double[] point1, double[] point2) {
		ExceptionUtility.verifyNonNull(point1);
		ExceptionUtility.verifyNonNull(point2);
		ExceptionUtility.verifyEqual(point1.length, point2.length);
		double squaredDistance = 0;
		double difference = 0;
		for (int i = 0; i < point1.length; i++) {
			difference = (point1[i] - point2[i]);
			squaredDistance += difference * difference;
		}
		return squaredDistance;
	}

	public static double squaredDistance(double point1, double point2) {
		double difference = (point1 - point2);
		return difference * difference;
	}
}
