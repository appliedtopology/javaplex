package edu.stanford.math.plex_plus.math.matrix;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.math.plex_plus.math.interfaces.GenericRing;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * Given a ring R, this class implements computation over the product
 * ring R x R x ... x R. In practice, this is simply done by componentwise
 * operations.
 * 
 * @author Andrew Tausz
 *
 * @param <T> the data type over which the ring is defined
 */
public class GenericProductRing<T> {

	private final GenericRing<T> ring;
	
	public GenericProductRing(GenericRing<T> ring) {
		this.ring = ring;
	}
	
	public List<T> add(List<T> a, List<T> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.size(), b.size());
		List<T> result = new ArrayList<T>(a.size());
		for (int i = 0; i < a.size(); i++) {
			result.set(i, ring.add(a.get(i), b.get(i)));
		}
		return result;
	}
	
	public List<T> subtract(List<T> a, List<T> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.size(), b.size());
		List<T> result = new ArrayList<T>(a.size());
		for (int i = 0; i < a.size(); i++) {
			result.set(i, ring.subtract(a.get(i), b.get(i)));
		}
		return result;
	}
	
	public List<T> multiply(List<T> a, List<T> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.size(), b.size());
		List<T> result = new ArrayList<T>(a.size());
		for (int i = 0; i < a.size(); i++) {
			result.set(i, ring.multiply(a.get(i), b.get(i)));
		}
		return result;
	}
	
	public List<T> negate(List<T> a) {
		ExceptionUtility.verifyNonNull(a);
		List<T> result = new ArrayList<T>(a.size());
		for (int i = 0; i < a.size(); i++) {
			result.set(i, ring.negate(a.get(i)));
		}
		return result;
	}
}
