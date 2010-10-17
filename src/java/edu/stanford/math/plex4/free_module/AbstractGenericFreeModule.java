package edu.stanford.math.plex4.free_module;

import java.util.Iterator;
import java.util.Map;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericLeftModule;
import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericRing;
import edu.stanford.math.plex4.utility.ExceptionUtility;

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
public abstract class AbstractGenericFreeModule<R, M> implements GenericLeftModule<R, AbstractGenericFormalSum<R, M>> {
	private GenericRing<R> ring;
	
	public AbstractGenericFreeModule(GenericRing<R> ring) {
		ExceptionUtility.verifyNonNull(ring);
		this.ring = ring;
	}
	
	public abstract AbstractGenericFormalSum<R, M> createNewSum();
	public abstract AbstractGenericFormalSum<R, M> createNewSum(R coefficient, M object);
	public abstract AbstractGenericFormalSum<R, M> createNewSum(AbstractGenericFormalSum<R, M> contents);
	
	public AbstractGenericFormalSum<R, M> add(AbstractGenericFormalSum<R, M> a, AbstractGenericFormalSum<R, M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		AbstractGenericFormalSum<R, M> result = null;
		Iterator<Map.Entry<M, R>> iterator = null;
		
		if (a.size() > b.size()) {
			result = this.createNewSum(a);
			iterator = b.iterator();
		} else {
			result = this.createNewSum(b);
			iterator = a.iterator();
		}
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, entry.getValue(), entry.getKey());
		}
		
		return result;
	}
	
	public AbstractGenericFormalSum<R, M> subtract(AbstractGenericFormalSum<R, M> a, AbstractGenericFormalSum<R, M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		AbstractGenericFormalSum<R, M> result = this.createNewSum(a);
		Iterator<Map.Entry<M, R>> iterator = b.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, ring.negate(entry.getValue()), entry.getKey());
		}
		
		return result;
	}
	
	public AbstractGenericFormalSum<R, M> multiply(R r, AbstractGenericFormalSum<R, M> a) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(r);
		
		AbstractGenericFormalSum<R, M> result = this.createNewSum();
		Iterator<Map.Entry<M, R>> iterator = a.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, ring.multiply(r, entry.getValue()), entry.getKey());
		}
		
		return result;
	}

	public AbstractGenericFormalSum<R, M> negate(AbstractGenericFormalSum<R, M> a) {
		ExceptionUtility.verifyNonNull(a);
		
		AbstractGenericFormalSum<R, M> result = this.createNewSum();
		Iterator<Map.Entry<M, R>> iterator = a.iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			addObject(result, ring.negate(entry.getValue()), entry.getKey());
		}
		
		return result;
	}
	
	public AbstractGenericFormalSum<R, M> multiply(int r, AbstractGenericFormalSum<R, M> a) {
		return multiply(this.ring.valueOf(r), a);
	}
	
	public AbstractGenericFormalSum<R, M> add(AbstractGenericFormalSum<R, M> a, M b) {
		return this.add(a, this.createNewSum(ring.getOne(), b));
	}
	
	public AbstractGenericFormalSum<R, M> add(M a, AbstractGenericFormalSum<R, M> b) {
		return this.add(this.createNewSum(ring.getOne(), a), b);
	}
	
	public AbstractGenericFormalSum<R, M> add(M a, M b) {
		return this.add(this.createNewSum(ring.getOne(), a), this.createNewSum(ring.getOne(), b));
	}
	
	public AbstractGenericFormalSum<R, M> subtract(AbstractGenericFormalSum<R, M> a, M b) {
		return this.subtract(a, this.createNewSum(ring.getOne(), b));
	}
	
	public AbstractGenericFormalSum<R, M> subtract(M a, AbstractGenericFormalSum<R, M> b) {
		return this.subtract(this.createNewSum(ring.getOne(), a), b);
	}
	
	public AbstractGenericFormalSum<R, M> subtract(M a, M b) {
		return this.subtract(this.createNewSum(ring.getOne(), a), this.createNewSum(ring.getOne(), b));
	}
	
	public AbstractGenericFormalSum<R, M> multiply(R r, M a) {
		return this.multiply(r, this.createNewSum(ring.getOne(), a));
	}
	
	public AbstractGenericFormalSum<R, M> negate(M a) {
		return this.negate(this.createNewSum(ring.getOne(), a));
	}
	
	public AbstractGenericFormalSum<R, M> multiply(int r, M a) {
		return this.multiply(r, this.createNewSum(ring.getOne(), a));
	}
	
	public R innerProduct(AbstractGenericFormalSum<R, M> v, AbstractGenericFormalSum<R, M> w) {
		R sum = this.ring.getZero();
		
		AbstractGenericFormalSum<R, M> smaller = (v.size() < w.size() ? v : w);
		AbstractGenericFormalSum<R, M> larger = (v.size() < w.size() ? w : v);
		
		Iterator<Map.Entry<M, R>> iterator = smaller.iterator();
		while (iterator.hasNext()) {
			Map.Entry<M, R> entry = iterator.next();
			if (larger.containsObject(entry.getKey())) {
				R other_coefficient = larger.getCoefficient(entry.getKey());
				sum = this.ring.add(sum, this.ring.multiply(entry.getValue(), other_coefficient));
			}
		} 
		
		return sum;
	}
	
	public void addObject(AbstractGenericFormalSum<R, M> formalSum, R coefficient, M object) {
		ExceptionUtility.verifyNonNull(object);
		ExceptionUtility.verifyNonNull(formalSum);
		ExceptionUtility.verifyNonNull(coefficient);
		
		if (this.ring.isZero(coefficient)) {
			return;
		}
		
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
	
	public AbstractGenericFormalSum<R, M> createSum(int[] coefficients, M[] objects) {
		AbstractGenericFormalSum<R, M> sum = new UnorderedGenericFormalSum<R, M>();

		if (coefficients == null || objects == null) {
			return sum;
		}
		
		ExceptionUtility.verifyEqual(coefficients.length, objects.length);
		
		for (int i = 0; i < coefficients.length; i++) {
			this.addObject(sum, this.ring.valueOf(coefficients[i]), objects[i]);
		}
		
		return sum;
	}

	public void accumulate(AbstractGenericFormalSum<R, M> a, AbstractGenericFormalSum<R, M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		for (Map.Entry<M, R> entry: b) {
			this.addObject(a, entry.getValue(), entry.getKey());
		}
	}

	public void accumulate(AbstractGenericFormalSum<R, M> a, AbstractGenericFormalSum<R, M> b, R c) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyNonNull(c);
		
		for (Map.Entry<M, R> entry: b) {
			this.addObject(a, ring.multiply(c, entry.getValue()), entry.getKey());
		}
	}
	
	public void accumulate(AbstractGenericFormalSum<R, M> a, M b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		this.addObject(a, this.ring.getOne(), b);
	}
	
	public void accumulate(AbstractGenericFormalSum<R, M> a, M b, R c) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		this.addObject(a, c, b);
	}
}
