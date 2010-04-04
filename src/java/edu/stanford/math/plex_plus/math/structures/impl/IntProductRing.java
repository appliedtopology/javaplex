package edu.stanford.math.plex_plus.math.structures.impl;

import edu.stanford.math.plex_plus.math.structures.interfaces.IntRing;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * Given a ring R, this class implements computation over the product
 * ring R x R x ... x R. In practice, this is simply done by componentwise
 * operations.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class IntProductRing {
	private final IntRing ring;
	
	public IntProductRing(IntRing ring) {
		this.ring = ring;
	}
	
	public int[] add(int[] a, int[] b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.length, b.length);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.add(a[i], b[i]);
		}
		return r;
	}
	
	public int[] subtract(int[] a, int[] b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.length, b.length);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.subtract(a[i], b[i]);
		}
		return r;
	}
	
	public int[] multiply(int[] a, int[] b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.length, b.length);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.multiply(a[i], b[i]);
		}
		return r;
	}
	
	public int[] negate(int[] a)  {
		ExceptionUtility.verifyNonNull(a);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.negate(a[i]);
		}
		return r;
	}
	
	public int[] add(int[] a, int b)  {
		ExceptionUtility.verifyNonNull(a);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.add(a[i], b);
		}
		return r;
	}
	
	public int[] subtract(int[] a, int b)  {
		ExceptionUtility.verifyNonNull(a);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.subtract(a[i], b);
		}
		return r;
	}
	
	public int[] multiply(int[] a, int b)  {
		ExceptionUtility.verifyNonNull(a);
		int[] r = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			r[i] = ring.multiply(a[i], b);
		}
		return r;
	}
	
	public int innerProduct(int[] a, int[] b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.length, b.length);
		if (a.length != b.length) {
			throw new IllegalArgumentException();
		}
		int r = 0;
		for (int i = 0; i < a.length; i++) {
			r = ring.add(ring.multiply(a[i], b[i]), r);
		}
		return r;
	}
}
