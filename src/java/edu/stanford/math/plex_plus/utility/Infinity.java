package edu.stanford.math.plex_plus.utility;

/**
 * This static class is used for handling infinite values.
 * 
 * @author Andrew Tausz
 *
 */
public class Infinity {
	public static class Double {
		/* Most algorithms do not expect infinite values so this constant holds
		 * a very large value that for all practical purposes can be considered
		 * as equivalent to infinity. Note: there is plenty of room to multiply
		 * this number without overflowing the double. */
		protected static final double POSITIVE_INFINITY = Math.sqrt(java.lang.Double.MAX_VALUE) - 1;

		/**
		 * This function returns true if a double represents the value of positive
		 * infinity, and false otherwise. 
		 * @param value the value to compare to positive infinity
		 * @return true if the input is equal to the representation of positive infinity
		 */
		public static boolean isPositiveInfinity(double value) {
			if (value >= POSITIVE_INFINITY) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * This function returns true if a double represents the value of negative
		 * infinity, and false otherwise. 
		 * @param value the value to compare to negative infinity
		 * @return true if the input is equal to the representation of negative infinity
		 */
		public static boolean isNegativeInfinity(double value) {
			return isPositiveInfinity(-value);
		}

		/**
		 * This function returns the representation of positive infinity.
		 * 
		 * @return the representation of positive infinity
		 */
		public static double getPositiveInfinity() {
			return POSITIVE_INFINITY;
		}

		/**
		 * This function returns the representation of negative infinity.
		 * 
		 * @return the representation of negative infinity
		 */
		public static double getNegativeInfinity() {
			return -getPositiveInfinity();
		}
	}
	
	public static class Int {
		/* Most algorithms do not expect infinite values so this constant holds
		 * a very large value that for all practical purposes can be considered
		 * as equivalent to infinity. Note: there is plenty of room to multiply
		 * this number without overflowing the double. */
		protected static final int POSITIVE_INFINITY = java.lang.Integer.MAX_VALUE;

		/**
		 * This function returns true if a double represents the value of positive
		 * infinity, and false otherwise. 
		 * @param value the value to compare to positive infinity
		 * @return true if the input is equal to the representation of positive infinity
		 */
		public static boolean isPositiveInfinity(int value) {
			if (value >= POSITIVE_INFINITY) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * This function returns true if a double represents the value of negative
		 * infinity, and false otherwise. 
		 * @param value the value to compare to negative infinity
		 * @return true if the input is equal to the representation of negative infinity
		 */
		public static boolean isNegativeInfinity(int value) {
			return isPositiveInfinity(-value);
		}

		/**
		 * This function returns the representation of positive infinity.
		 * 
		 * @return the representation of positive infinity
		 */
		public static int getPositiveInfinity() {
			return POSITIVE_INFINITY;
		}

		/**
		 * This function returns the representation of negative infinity.
		 * 
		 * @return the representation of negative infinity
		 */
		public static int getNegativeInfinity() {
			return -getPositiveInfinity();
		}
	}
}
