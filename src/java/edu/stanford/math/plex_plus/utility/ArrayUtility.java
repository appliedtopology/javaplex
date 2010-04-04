package edu.stanford.math.plex_plus.utility;

/**
 * This class contains static helper functions for
 * manipulating arrays.
 * 
 * @author Andrew Tausz
 *
 */
public class ArrayUtility {
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
	
	public static String toString(double[][] array) {
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
}
