package edu.stanford.math.plex_plus.array_utility;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class ArrayGeneration {
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
	
	public static int[] replicate(int value, int length) {
		int[] result = new int[length];
		for (int i = 0; i < length; i++) {
			result[i] = value;
		}
		return result;
	}
	
	public static double[] replicate(double value, int length) {
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = value;
		}
		return result;
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
}
