package edu.stanford.math.plex4.free_module;

import edu.stanford.math.plex4.algebraic_structures.interfaces.DoubleLeftModule;
import edu.stanford.math.plex4.utility.ExceptionUtility;
import gnu.trove.TObjectDoubleIterator;

public class DoubleFreeModule<M> implements	DoubleLeftModule<DoubleFormalSum<M>> {
	
	public DoubleFreeModule() {	}

	public DoubleFormalSum<M> add(DoubleFormalSum<M> a, DoubleFormalSum<M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		DoubleFormalSum<M> result = null;
		TObjectDoubleIterator<M> iterator = null;
		
		if (a.size() > b.size()) {
			result = new DoubleFormalSum<M>(a);
			iterator = b.iterator();
		} else {
			result = new DoubleFormalSum<M>(b);
			iterator = a.iterator();
		}
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, iterator.value(), iterator.key());
		}
		
		return result;
	}
	
	public DoubleFormalSum<M> subtract(DoubleFormalSum<M> a, DoubleFormalSum<M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		DoubleFormalSum<M> result = new DoubleFormalSum<M>(a);
		TObjectDoubleIterator<M> iterator = b.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, -iterator.value(), iterator.key());
		}
		
		return result;
	}
	
	public DoubleFormalSum<M> multiply(double r, DoubleFormalSum<M> a) {
		ExceptionUtility.verifyNonNull(a);
		
		DoubleFormalSum<M> result = new DoubleFormalSum<M>();
		TObjectDoubleIterator<M> iterator = a.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, r * iterator.value(), iterator.key());
		}
		
		return result;
	}

	public DoubleFormalSum<M> negate(DoubleFormalSum<M> a) {
		ExceptionUtility.verifyNonNull(a);
		
		DoubleFormalSum<M> result = new DoubleFormalSum<M>();
		TObjectDoubleIterator<M> iterator = a.iterator();
		
		while(iterator.hasNext()) {
			iterator.advance();
			addObject(result, -iterator.value(), iterator.key());
		}
		
		return result;
	}
	
	public DoubleFormalSum<M> add(DoubleFormalSum<M> a, M b) {
		return this.add(a, new DoubleFormalSum<M>(1, b));
	}
	
	public DoubleFormalSum<M> add(M a, DoubleFormalSum<M> b) {
		return this.add(new DoubleFormalSum<M>(1, a), b);
	}
	
	public DoubleFormalSum<M> add(M a, M b) {
		return this.add(new DoubleFormalSum<M>(1, a), new DoubleFormalSum<M>(1, b));
	}
	
	public DoubleFormalSum<M> subtract(DoubleFormalSum<M> a, M b) {
		return this.subtract(a, new DoubleFormalSum<M>(1, b));
	}
	
	public DoubleFormalSum<M> subtract(M a, DoubleFormalSum<M> b) {
		return this.subtract(new DoubleFormalSum<M>(1, a), b);
	}
	
	public DoubleFormalSum<M> subtract(M a, M b) {
		return this.subtract(new DoubleFormalSum<M>(1, a), new DoubleFormalSum<M>(1, b));
	}
	
	public DoubleFormalSum<M> multiply(double r, M a) {
		return this.multiply(r, new DoubleFormalSum<M>(1, a));
	}
	
	public DoubleFormalSum<M> negate(M a) {
		return this.negate(new DoubleFormalSum<M>(1, a));
	}
	
	public void addObject(DoubleFormalSum<M> formalSum, double coefficient, M object) {
		ExceptionUtility.verifyNonNull(object);
		ExceptionUtility.verifyNonNull(formalSum);
		if (formalSum.containsObject(object)) {
			double newCoefficient = formalSum.getCoefficient(object) + coefficient;
			if (newCoefficient == 0) {
				formalSum.remove(object);
			} else {
				formalSum.put(newCoefficient, object);
			}
		} else {
			formalSum.put(coefficient, object);
		}
	}
	
	public DoubleFormalSum<M> createSum(double[] coefficients, M[] objects) {
		DoubleFormalSum<M> sum = new DoubleFormalSum<M>();

		if (coefficients == null || objects == null) {
			return sum;
		}
			
		ExceptionUtility.verifyEqual(coefficients.length, objects.length);
		
		for (int i = 0; i < coefficients.length; i++) {
			this.addObject(sum, coefficients[i], objects[i]);
		}
		
		return sum;
	}

	public DoubleFormalSum<M> getAdditiveIdentity() {
		return new DoubleFormalSum<M>();
	}
	
	public void accumulate(DoubleFormalSum<M> a, DoubleFormalSum<M> b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		
		for (TObjectDoubleIterator<M> iterator = b.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			this.addObject(a, iterator.value(), iterator.key());
		}
	}

	public void accumulate(DoubleFormalSum<M> a, DoubleFormalSum<M> b, double c) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);

		for (TObjectDoubleIterator<M> iterator = b.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			this.addObject(a, c * iterator.value(), iterator.key());
		}
	}
	
	public void accumulate(DoubleFormalSum<M> a, M b, double c) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);

		this.addObject(a, c, b);
	}
}
