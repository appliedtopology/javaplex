package edu.stanford.math.plex4.array_utility;

import java.lang.reflect.Array;

public class ArrayCreation {
	public static int[][] newIntMatrix(int rows, int columns) {
		int[][] result = new int[rows][];
		
		for (int i = 0; i < rows; i++) {
			result[i] = new int[columns];
		}
		
		return result;
	}
	
	public static double[][] newDoubleMatrix(int rows, int columns) {
		double[][] result = new double[rows][];
		
		for (int i = 0; i < rows; i++) {
			result[i] = new double[columns];
		}
		
		return result;
	}
	
	public static int[] newIntArray(int length) {
		return new int[length];
	}
	
	public static double[] newDoubleArray(int length) {
		return new double[length];
	}
	
	public static <T> T[] newGenericArray(int length, T initializer) {
		//return ((T[]) new Object[length]);
		return ((T[]) Array.newInstance(initializer.getClass(), length));
	}
	
	public static <T> T[][] newGenericMatrix(int rows, int columns, T initializer) {
		//return ((T[]) new Object[length]);
		return ((T[][]) Array.newInstance(initializer.getClass(), new int[]{rows, columns}));
	}
}
