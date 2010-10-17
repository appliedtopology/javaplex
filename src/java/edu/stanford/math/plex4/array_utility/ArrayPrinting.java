package edu.stanford.math.plex4.array_utility;

import edu.stanford.math.plex4.utility.ExceptionUtility;

public class ArrayPrinting {
	public static <T> String toString(T[][] array) {
		ExceptionUtility.verifyNonNull(array);
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			builder.append('[');
			for (int j = 0; j < array[i].length; j++) {
				if (j > 0) {
					builder.append(", ");
				}
				if (array[i][j] != null) {
					builder.append(array[i][j]);
				} else {
					builder.append(" ");
				}
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
}
