package edu.stanford.math.plex4.utility;


import gnu.trove.TIntHashSet;

import java.util.Collection;

/**
 * This class contains static methods for comparing various objects.
 * 
 * @author Andrew Tausz
 *
 */
public class ComparisonUtility {
	// default precision values
	public static final double LOW_PRECISION = 1e-3;
	public static final double MED_PRECISION = 1e-6;
	public static final double HIGH_PRECISION = 1e-9;
	
	/**
	 * This function returns true if two Collections define the same set; 
	 * that is they contain the same elements.
	 * 
	 * @param a the first Collection
	 * @param b the second Collection
	 * @return true if the two Collections contain the same set of elements.
	 */
	public static boolean setEquals(Collection<?> a, Collection<?> b) {
		if (a.size() != b.size()) {
			return false;
		}
		return (a.containsAll(b) && b.containsAll(a));
	}
	
	/**
	 * This function returns true if the two sets of integers contain the
	 * same elements.
	 * 
	 * @param a
	 * @param b
	 * @return true if the two sets are the same
	 */
	public static boolean setEquals(TIntHashSet a, TIntHashSet b) {
		return a.containsAll(b.toArray()) && b.containsAll(a.toArray());
	}
	
	/**
	 * This function compares two doubles to see if they are within the specified
	 * tolerance of each other.
	 * 
	 * @param a the first value
	 * @param b the second value
	 * @param tolerance the tolerance
	 * @return true iff |a - b| <= tolerance
	 */
	public static boolean compareDoublesAbsolute(double a, double b, double tolerance) {
		return (Math.abs(a - b) <= tolerance);
	}
	
	/**
	 * This function compares two doubles to see if their relative distance is within the
	 * specified tolerance.
	 * 
	 * @param a the first value
	 * @param b the second value
	 * @param tolerance the tolerance
	 * @return true iff |a - b| / (0.5 (|a| + |b|)) <= tolerance
	 */
	public static boolean compareDoublesRelative(double a, double b, double tolerance) {
		if (a == 0 && b == 0) {
			return true;
		}
		return (Math.abs((a - b) / (0.5 * (Math.abs(a) + Math.abs(b)))) <= tolerance);
	}
	
	/**
	 * This function compares two doubles to see if their (relative/absolute) distance
	 * is within the specified tolerance. If 0.5 (|a| + |b|) < 1 it uses an absolute comparison,
	 * and otherwise it uses a relative comparison.
	 * 
	 * @param a the first value
	 * @param b the second value
	 * @param tolerance the tolerance
	 * @return true if the (relative/absolute) distance of a and b is within the given tolerance
	 */
	public static boolean compareDoublesGuardedRelative(double a, double b, double tolerance) {
		double denom = 0.5 * (Math.abs(a) + Math.abs(b));
		if (denom < 1) {
			return (Math.abs(a - b) <= tolerance);
		} else {
			return (Math.abs(a - b) / denom <= tolerance);
		}
	}
}
