package edu.stanford.math.plex_plus.algebraic_structures.impl;

import java.util.Iterator;
import java.util.Map;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericLeftModule;
import edu.stanford.math.plex_plus.algebraic_structures.interfaces.GenericRing;
import edu.stanford.math.plex_plus.datastructures.GenericFormalSum;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements the operations of the free R-module
 * over the set M. Elements of this module are of the form
 * r_1 m_1 + ... + r_k m_k, where r_i is in R and m_i is in M. 
 * 
 * The class M is required to implement the Comparable<M> interface so 
 * that the operations can be computed efficiently. In the constructor,
 * the user is required to pass a GenericRing<R> object which implements
 * the desired ring operations.
 * 
 * @author Andrew Tausz
 *
 * @param <R> the coefficient data type over which a ring is defined
 * @param <M> the object data type (e.g. a simplex)
 */
public class GenericFreeModule<R, M extends Comparable<M>> implements GenericLeftModule<R, GenericFormalSum<R, M>> {
	private GenericRing<R> ring;
	
	public GenericFreeModule(GenericRing<R> ring) {
		ExceptionUtility.verifyNonNull(ring);
		this.ring = ring;
	}
	
	@Override
	public GenericFormalSum<R, M> add(GenericFormalSum<R, M> a, GenericFormalSum<R, M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		GenericFormalSum<R, M> result = null;
		Iterator<Map.Entry<M, R>> iterator = null;
		
		if (a.size() > b.size()) {
			result = new GenericFormalSum<R, M>(a);
			iterator = b.iterator();
		} else {
			result = new GenericFormalSum<R, M>(b);
			iterator = a.iterator();
		}
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, entry.getValue(), entry.getKey());
		}
		
		return result;
	}

	@Override
	public GenericFormalSum<R, M> subtract(GenericFormalSum<R, M> a, GenericFormalSum<R, M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		GenericFormalSum<R, M> result = new GenericFormalSum<R, M>(a);
		Iterator<Map.Entry<M, R>> iterator = b.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, ring.negate(entry.getValue()), entry.getKey());
		}
		
		return result;
	}
	
	
	@Override
	public GenericFormalSum<R, M> multiply(R r, GenericFormalSum<R, M> a) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(r);
		
		GenericFormalSum<R, M> result = new GenericFormalSum<R, M>();
		Iterator<Map.Entry<M, R>> iterator = a.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, ring.multiply(r, entry.getValue()), entry.getKey());
		}
		
		return result;
	}

	@Override
	public GenericFormalSum<R, M> negate(GenericFormalSum<R, M> a) {
		ExceptionUtility.verifyNonNull(a);
		
		GenericFormalSum<R, M> result = new GenericFormalSum<R, M>();
		Iterator<Map.Entry<M, R>> iterator = a.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, ring.negate(entry.getValue()), entry.getKey());
		}
		
		return result;
	}
	
	@Override
	public GenericFormalSum<R, M> multiply(int r, GenericFormalSum<R, M> a) {
		return multiply(this.ring.valueOf(r), a);
	}
	
	public GenericFormalSum<R, M> add(GenericFormalSum<R, M> a, M b) {
		return this.add(a, new GenericFormalSum<R, M>(ring.getOne(), b));
	}
	
	public GenericFormalSum<R, M> add(M a, GenericFormalSum<R, M> b) {
		return this.add(new GenericFormalSum<R, M>(ring.getOne(), a), b);
	}
	
	public GenericFormalSum<R, M> add(M a, M b) {
		return this.add(new GenericFormalSum<R, M>(ring.getOne(), a), new GenericFormalSum<R, M>(ring.getOne(), b));
	}
	
	public GenericFormalSum<R, M> subtract(GenericFormalSum<R, M> a, M b) {
		return this.subtract(a, new GenericFormalSum<R, M>(ring.getOne(), b));
	}
	
	public GenericFormalSum<R, M> subtract(M a, GenericFormalSum<R, M> b) {
		return this.subtract(new GenericFormalSum<R, M>(ring.getOne(), a), b);
	}
	
	public GenericFormalSum<R, M> subtract(M a, M b) {
		return this.subtract(new GenericFormalSum<R, M>(ring.getOne(), a), new GenericFormalSum<R, M>(ring.getOne(), b));
	}
	
	public GenericFormalSum<R, M> multiply(R r, M a) {
		return this.multiply(r, new GenericFormalSum<R, M>(ring.getOne(), a));
	}
	
	public GenericFormalSum<R, M> negate(M a) {
		return this.negate(new GenericFormalSum<R, M>(ring.getOne(), a));
	}
	
	public GenericFormalSum<R, M> multiply(int r, M a) {
		return this.multiply(r, new GenericFormalSum<R, M>(ring.getOne(), a));
	}
	
	private void addObject(GenericFormalSum<R, M> formalSum, R coefficient, M object) {
		ExceptionUtility.verifyNonNull(object);
		ExceptionUtility.verifyNonNull(formalSum);
		ExceptionUtility.verifyNonNull(coefficient);
		if (formalSum.containsObject(object)) {
			R newCoefficient = this.ring.add(formalSum.getCoefficient(object), coefficient);
			if (ring.isZero(newCoefficient)) {
				formalSum.remove(object);
			} else {
				formalSum.put(newCoefficient, object);
			}
		} else {
			formalSum.put(coefficient, object);
		}
	}
}
