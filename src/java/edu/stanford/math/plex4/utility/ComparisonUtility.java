package edu.stanford.math.plex4.utility;


import gnu.trove.TIntHashSet;

import java.util.Collection;

public class ComparisonUtility {
	
	public static final double LOW_PRECISION = 1e-3;
	public static final double MED_PRECISION = 1e-6;
	public static final double HIGH_PRECISION = 1e-9;
	
	public static boolean setEquals(Collection<?> a, Collection<?> b) {
		if (a.size() != b.size()) {
			return false;
		}
		return (a.containsAll(b) && b.containsAll(a));
	}
	
	public static boolean setEquals(TIntHashSet a, TIntHashSet b) {
		return a.containsAll(b.toArray()) && b.containsAll(a.toArray());
	}
	
	public static boolean compareDoublesAbsolute(double a, double b, double tolerance) {
		return (Math.abs(a - b) <= tolerance);
	}
	
	public static boolean compareDoublesRelative(double a, double b, double tolerance) {
		if (a == 0 && b == 0) {
			return true;
		}
		return (Math.abs((a - b) / (0.5 * (Math.abs(a) + Math.abs(b)))) <= tolerance);
	}
	
	public static boolean compareDoublesGuardedRelative(double a, double b, double tolerance) {
		double denom = 0.5 * (Math.abs(a) + Math.abs(b));
		if (denom < 1) {
			return (Math.abs(a - b) <= tolerance);
		} else {
			return (Math.abs(a - b) / denom <= tolerance);
		}
	}
}
