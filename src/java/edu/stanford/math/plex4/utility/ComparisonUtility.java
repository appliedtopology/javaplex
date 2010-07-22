package edu.stanford.math.plex4.utility;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;

import java.util.Collection;

public class ComparisonUtility {
	
	/**
	 * This function compares the contents of two collections and returns true if and
	 * only if they define the same set (ie. they have the same elements).
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> boolean setEquals(Collection<T> a, Collection<T> b) {
		return (a.containsAll(b) && b.containsAll(a));
	}
	
	public static <T> boolean setEquals(THashSet<T> a, THashSet<T> b) {
		return (a.containsAll(b) && b.containsAll(a));
	}
	
	public static boolean setEquals(TIntSet a, TIntSet b) {
		return (a.containsAll(b) && b.containsAll(a));
	}
}
