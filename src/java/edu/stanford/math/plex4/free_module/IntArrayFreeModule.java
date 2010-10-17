package edu.stanford.math.plex4.free_module;

import java.util.Comparator;

import edu.stanford.math.plex4.algebraic_structures.interfaces.IntLeftModule;
import edu.stanford.math.plex4.algebraic_structures.interfaces.IntRing;
import edu.stanford.math.plex4.array_utility.ArrayCreation;

public class IntArrayFreeModule<T> implements IntLeftModule<IntArraySum<T>> {
	private final IntRing ring;
	private final Comparator<T> comparator;
	
	public IntArrayFreeModule(IntRing ring, Comparator<T> comparator) {
		this.ring = ring;
		this.comparator = comparator;
	}
	
	public IntArraySum<T> createSum(int[] coefficients, T[] objects) {
		this.chain_sort(objects, coefficients, this.comparator);
		return new IntArraySum<T>(coefficients, objects);
	}

	public IntArraySum<T> add(IntArraySum<T> a, IntArraySum<T> b) {
		return this.merge(a, b, 1);
	}

	public IntArraySum<T> multiply(int r, IntArraySum<T> a) {
		int[] coefficients = a.getCoefficients();
		int length = coefficients.length;
		int[] r_coefficients = new int[length];
		
		for (int i = 0; i < length; i++) {
			r_coefficients[i] = r * coefficients[i];
		}
		
		return new IntArraySum<T>(r_coefficients, a.getObjects());
	}

	public IntArraySum<T> negate(IntArraySum<T> a) {
		int[] coefficients = a.getCoefficients();
		int length = coefficients.length;
		int[] r_coefficients = new int[length];
		
		for (int i = 0; i < length; i++) {
			r_coefficients[i] = -coefficients[i];
		}
		
		return new IntArraySum<T>(r_coefficients, a.getObjects());
	}

	public IntArraySum<T> subtract(IntArraySum<T> a, IntArraySum<T> b) {
		return this.merge(a, b, -1);
	}
	
	/**
	 * TODO: fix merge functions
	 */
	public void accumulate(IntArraySum<T> a, IntArraySum<T> b, int c) {
		a = this.merge(a, b, c);
	}

	public void accumulate(IntArraySum<T> a, IntArraySum<T> b) {
		a = this.merge(a, b, 1);
	}
	
	/**
	 * Computes a + c * b
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public IntArraySum<T> merge(IntArraySum<T> a, IntArraySum<T> b, int c) {
		int[] a_coefficients = a.getCoefficients();
		int[] b_coefficients = b.getCoefficients();
		T[] a_objects = a.getObjects();
		T[] b_objects = b.getObjects();
		
		int a_length = a_coefficients.length;
		int b_length = b_coefficients.length;
		
		int r_length = 0;
		
		if (c == 0) {
			return a;
		}
		
		if (a.isEmpty()) {
			return this.multiply(c, b);
		}
		
		if (b.isEmpty()) {
			return a;
		}
		
		int a_index = 0;
		int b_index = 0;
		
		while ((a_index < a_length) || (b_index < b_length)) {
			// try to move along A
			while ((a_index < a_length) && (b_index == b_length || comparator.compare(a_objects[a_index], b_objects[b_index]) < 0)) {
				r_length++;
				a_index++;
			}
			
			if ((a_index < a_length) && (b_index < b_length) && (comparator.compare(a_objects[a_index], b_objects[b_index]) == 0)) {
				if (!ring.isZero(ring.add(a_coefficients[a_index], ring.multiply(c, b_coefficients[b_index])))) {
					r_length++;
				}
				a_index++;
				b_index++;
			}
			
			while ((b_index < b_length) && (a_index == a_length || comparator.compare(b_objects[b_index], a_objects[a_index]) < 0)) {
				r_length++;
				b_index++;
			}
		}
		
		// check if the result is empty
		if (r_length == 0) {
			// TODO: redo (static cache)
			return new IntArraySum<T>();
		}
		
		int[] r_coefficients = new int[r_length];
		T[] r_objects = ArrayCreation.newGenericArray(r_length, a_objects[0]);
		
		a_index = 0;
		b_index = 0;
		int r_index = 0;
		
		while ((a_index < a_length) || (b_index < b_length)) {
			while ((a_index < a_length) && (b_index == b_length || comparator.compare(a_objects[a_index], b_objects[b_index]) < 0)) {
				r_coefficients[r_index] = a_coefficients[a_index];
				r_objects[r_index] = a_objects[a_index];
				r_index++;
				a_index++;
			}
			
			if ((a_index < a_length) && (b_index < b_length) && (comparator.compare(a_objects[a_index], b_objects[b_index]) == 0)) {
				if (!ring.isZero(ring.add(a_coefficients[a_index], ring.multiply(c, b_coefficients[b_index])))) {
					r_coefficients[r_index] = ring.add(a_coefficients[a_index], ring.multiply(c, b_coefficients[b_index]));
					r_objects[r_index] = a_objects[a_index];
					r_index++;
				}
				a_index++;
				b_index++;
			}
			
			while ((b_index < b_length) && (a_index == a_length || comparator.compare(b_objects[b_index], a_objects[a_index]) < 0)) {
				r_coefficients[r_index] = ring.multiply(c, b_coefficients[b_index]);
				r_objects[r_index] = b_objects[b_index];
				r_index++;
				b_index++;
			}
		}
		
		if (!IntArraySum.validateSorted(r_objects, comparator)) {
			throw new IllegalStateException();
		}
		IntArraySum<T> result = new IntArraySum<T>(r_coefficients, r_objects);
		return result;
	}
	
	protected void chain_sort(T[] b, int[] c, Comparator<T> comparator) {
		for (int j = b.length - 1; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (comparator.compare(b[i + 1], b[i]) < 0) {
					T dummyS = b[i];
					int dummyC = c[i];
					b[i] = b[i+1];
					b[i+1] = dummyS;
					c[i] = c[i+1];
					c[i+1] = dummyC;
				}
			}
		}
	}
}
