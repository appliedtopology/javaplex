package edu.stanford.math.plex_plus.utility;

import java.util.Collection;

/**
 * This class contains static functions for aiding in
 * creating exceptions, and verifying exceptions.
 * 
 * The methods starting with verify check a condition
 * on the arguments. If the condition is not satisfied,
 * and appropriate exception is thrown.
 * 
 * @author Andrew Tausz
 *
 */
public class ExceptionUtility {
	
	/*
	 * Indices
	 */

	/**
	 * This function checks to see if index is in the set {lowerBound, ..., upperBound - 1}.
	 * 
	 * @param lowerBound the minimum allowable value of index
	 * @param upperBound one past the maximum allowable value of index
	 * @param index the index to check
	 */
	public static void verifyIndex(int lowerBound, int upperBound, int index) {
		if (index < lowerBound || index >= upperBound) {
			throw new IndexOutOfBoundsException("Lower bound: " + lowerBound + ", Upper bound: " + upperBound + ", Index: " + index);
		}
	}
	
	/**
	 * This function checks to see if index is in the set {0, ..., upperBound - 1}.
	 * 
	 * @param upperBound one past the maximum allowable value of index
	 * @param index the index to check
	 */
	public static void verifyIndex(int upperBound, int index) {
		verifyIndex(0, upperBound, index);
	}
	
	/*
	 * Generic Objects
	 */
	
	public static void verifyNonNull(Object value) {
		if (value == null) {
			throw new NullPointerException("Argument must be non-null.");
		}
	}
	
	public static <T> void verifyAllNonNull(Collection<T> collection) {
		for (T object : collection) {
			if (object == null) {
				throw new NullPointerException("Container must contain only non-null entries.");
			}
		}
	}
	
	public static <T> void verifyNonEmpty(Collection<T> collection) {
		if (collection.isEmpty()) {
			throw new IllegalArgumentException("Container must not be empty.");
		}
	}
	
	public static <T> void verifyNonEmpty(int[] collection) {
		if (collection.length == 0) {
			throw new IllegalArgumentException("Container must not be empty.");
		}
	}
	
	public static <T> void verifyNonEmpty(double[] collection) {
		if (collection.length == 0) {
			throw new IllegalArgumentException("Container must not be empty.");
		}
	}
	
	/*
	 * Int comparison
	 */
	
	public static void verifyEqual(int value1, int value2) {
		if (value1 != value2) {
			throw new IllegalArgumentException("Arguments must be equal. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyNonEqual(int value1, int value2) {
		if (value1 == value2) {
			throw new IllegalArgumentException("Arguments must be non-equal. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyLessThan(int value1, int value2) {
		if (value1 >= value2) {
			throw new IllegalArgumentException("Argument 1 must be less than Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyGreaterThan(int value1, int value2) {
		if (value1 <= value2) {
			throw new IllegalArgumentException("Argument 1 must be greater than Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyLessThanOrEqual(int value1, int value2) {
		if (value1 > value2) {
			throw new IllegalArgumentException("Argument 1 must be less than or equal to Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyGreaterThanOrEqual(int value1, int value2) {
		if (value1 < value2) {
			throw new IllegalArgumentException("Argument 1 must be greater than or equal to Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyPositive(int value) {
		if (value <= 0) {
			throw new IllegalArgumentException("Argument must be positive. Value: " + value);
		}
	}
	
	public static void verifyNonNegative(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Argument must be non-negative. Value: " + value);
		}
	}
	
	public static void verifyNegative(int value) {
		if (value >= 0) {
			throw new IllegalArgumentException("Argument must be negative. Value: " + value);
		}
	}
	
	public static void verifyNonPositive(int value) {
		if (value > 0) {
			throw new IllegalArgumentException("Argument must be non-positive. Value: " + value);
		}
	}
	
	public static void verifyNonZero(int value) {
		if (value == 0) {
			throw new IllegalArgumentException("Argument must be non-zero. Value: " + value);
		}
	}
	
	public static void verifyClosedInterval(int value, int lowerBound, int upperBound) {
		if ((value < lowerBound) || (value > upperBound)) {
			throw new IllegalArgumentException("Argument is outside the interval [" + lowerBound + ", " + upperBound + "] Value: " + value);
		}
	}
	
	/*
	 * Double comparison
	 */
	
	public static void verifyEqual(double value1, double value2) {
		if (value1 != value2) {
			throw new IllegalArgumentException("Arguments must be equal. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyNonEqual(double value1, double value2) {
		if (value1 == value2) {
			throw new IllegalArgumentException("Arguments must be non-equal. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyLessThan(double value1, double value2) {
		if (value1 >= value2) {
			throw new IllegalArgumentException("Argument 1 must be less than Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyGreaterThan(double value1, double value2) {
		if (value1 <= value2) {
			throw new IllegalArgumentException("Argument 1 must be greater than Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyLessThanOrEqual(double value1, double value2) {
		if (value1 > value2) {
			throw new IllegalArgumentException("Argument 1 must be less than or equal to Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyGreaterThanOrEqual(double value1, double value2) {
		if (value1 < value2) {
			throw new IllegalArgumentException("Argument 1 must be greater than or equal to Argument 2. Argument 1: " + value1 + " Argument 2: " + value2);
		}
	}
	
	public static void verifyPositive(double value) {
		if (value <= 0) {
			throw new IllegalArgumentException("Argument must be positive. Value: " + value);
		}
	}
	
	public static void verifyNonNegative(double value) {
		if (value < 0) {
			throw new IllegalArgumentException("Argument must be non-negative. Value: " + value);
		}
	}
	
	public static void verifyNegative(double value) {
		if (value >= 0) {
			throw new IllegalArgumentException("Argument must be negative. Value: " + value);
		}
	}
	
	public static void verifyNonPositive(double value) {
		if (value > 0) {
			throw new IllegalArgumentException("Argument must be non-positive. Value: " + value);
		}
	}
	
	public static void verifyNonZero(double value) {
		if (value == 0) {
			throw new IllegalArgumentException("Argument must be non-zero. Value: " + value);
		}
	}
	
	public static void verifyClosedInterval(double value, double lowerBound, double upperBound) {
		if ((value < lowerBound) || (value > upperBound)) {
			throw new IllegalArgumentException("Argument is outside the interval [" + lowerBound + ", " + upperBound + "] Value: " + value);
		}
	}
	
	public static void verifyOpenInterval(double value, double lowerBound, double upperBound) {
		if ((value <= lowerBound) || (value >= upperBound)) {
			throw new IllegalArgumentException("Argument is outside the interval (" + lowerBound + ", " + upperBound + ") Value: " + value);
		}
	}
	
	public static void verifyLeftHalfOpenInterval(double value, double lowerBound, double upperBound) {
		if ((value <= lowerBound) || (value > upperBound)) {
			throw new IllegalArgumentException("Argument is outside the interval (" + lowerBound + ", " + upperBound + "] Value: " + value);
		}
	}
	
	public static void verifyRightHalfOpenInterval(double value, double lowerBound, double upperBound) {
		if ((value < lowerBound) || (value >= upperBound)) {
			throw new IllegalArgumentException("Argument is outside the interval [" + lowerBound + ", " + upperBound + ") Value: " + value);
		}
	}
	
	public static void verifyTrue(boolean condition) {
		if (!condition) {
			throw new IllegalArgumentException("Condition must be true");
		}
	}
	
	public static void verifyFalse(boolean condition) {
		if (condition) {
			throw new IllegalArgumentException("Condition must be false");
		}
	}
	
	public static void verifyTrue(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void verifyFalse(boolean condition, String message) {
		if (condition) {
			throw new IllegalArgumentException(message);
		}
	}
}
