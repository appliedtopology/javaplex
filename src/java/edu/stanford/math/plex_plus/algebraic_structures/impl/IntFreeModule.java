/**
 * 
 */
package edu.stanford.math.plex_plus.algebraic_structures.impl;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntLeftModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntRing;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;

/**
 * This class implements the operations of the free R-module
 * over the set M. Elements of this module are of the form
 * r_1 m_1 + ... + r_k m_k, where r_i is an int and m_i is in M. 
 * 
 * In the constructor, the user is required to pass a IntRing 
 * object which implements the desired ring operations.
 * 
 * @author Andrew Tausz
 *
 * @param <M> the object data type (e.g. a simplex)
 */
public class IntFreeModule<M> implements IntLeftModule<IntFormalSum<M>> {
	private IntRing ring;
	
	public IntFreeModule(IntRing ring) {
		ExceptionUtility.verifyNonNull(ring);
		this.ring = ring;
	}

	@Override
	public IntFormalSum<M> add(IntFormalSum<M> a, IntFormalSum<M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		IntFormalSum<M> result = null;
		TObjectIntIterator<M> iterator = null;
		
		if (a.size() > b.size()) {
			result = new IntFormalSum<M>(a);
			iterator = b.iterator();
		} else {
			result = new IntFormalSum<M>(b);
			iterator = a.iterator();
		}
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, iterator.value(), iterator.key());
		}
		
		return result;
	}
	
	@Override
	public IntFormalSum<M> subtract(IntFormalSum<M> a, IntFormalSum<M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		IntFormalSum<M> result = new IntFormalSum<M>(a);
		TObjectIntIterator<M> iterator = b.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, ring.negate(iterator.value()), iterator.key());
		}
		
		return result;
	}
	
	@Override
	public IntFormalSum<M> multiply(int r, IntFormalSum<M> a) {
		ExceptionUtility.verifyNonNull(a);
		
		IntFormalSum<M> result = new IntFormalSum<M>();
		TObjectIntIterator<M> iterator = a.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, ring.multiply(r, iterator.value()), iterator.key());
		}
		
		return result;
	}

	@Override
	public IntFormalSum<M> negate(IntFormalSum<M> a) {
		ExceptionUtility.verifyNonNull(a);
		
		IntFormalSum<M> result = new IntFormalSum<M>();
		TObjectIntIterator<M> iterator = a.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, ring.negate(iterator.value()), iterator.key());
		}
		
		return result;
	}
	
	public IntFormalSum<M> add(IntFormalSum<M> a, M b) {
		return this.add(a, new IntFormalSum<M>(ring.getOne(), b));
	}
	
	public IntFormalSum<M> add(M a, IntFormalSum<M> b) {
		return this.add(new IntFormalSum<M>(ring.getOne(), a), b);
	}
	
	public IntFormalSum<M> add(M a, M b) {
		return this.add(new IntFormalSum<M>(ring.getOne(), a), new IntFormalSum<M>(ring.getOne(), b));
	}
	
	public IntFormalSum<M> subtract(IntFormalSum<M> a, M b) {
		return this.subtract(a, new IntFormalSum<M>(ring.getOne(), b));
	}
	
	public IntFormalSum<M> subtract(M a, IntFormalSum<M> b) {
		return this.subtract(new IntFormalSum<M>(ring.getOne(), a), b);
	}
	
	public IntFormalSum<M> subtract(M a, M b) {
		return this.subtract(new IntFormalSum<M>(ring.getOne(), a), new IntFormalSum<M>(ring.getOne(), b));
	}
	
	public IntFormalSum<M> multiply(int r, M a) {
		return this.multiply(r, new IntFormalSum<M>(ring.getOne(), a));
	}
	
	public IntFormalSum<M> negate(M a) {
		return this.negate(new IntFormalSum<M>(ring.getOne(), a));
	}
	
	public void addObject(IntFormalSum<M> formalSum, int coefficient, M object) {
		ExceptionUtility.verifyNonNull(object);
		ExceptionUtility.verifyNonNull(formalSum);
		if (formalSum.containsObject(object)) {
			int newCoefficient = this.ring.add(formalSum.getCoefficient(object), coefficient);
			if (ring.isZero(newCoefficient)) {
				formalSum.remove(object);
			} else {
				formalSum.put(newCoefficient, object);
			}
		} else {
			formalSum.put(coefficient, object);
		}
	}
	
	public IntFormalSum<M> createSum(int[] coefficients, M[] objects) {
		ExceptionUtility.verifyEqual(coefficients.length, objects.length);
		
		IntFormalSum<M> sum = new IntFormalSum<M>();
		
		for (int i = 0; i < coefficients.length; i++) {
			this.addObject(sum, coefficients[i], objects[i]);
		}
		
		return sum;
	}

}
