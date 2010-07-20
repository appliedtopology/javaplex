package edu.stanford.math.plex4.functional;

import edu.stanford.math.plex4.algebraic_structures.interfaces.DoubleLeftModule;

public class FunctionObjectModule<M> implements DoubleLeftModule<GenericDoubleFunction<M>> {
	public GenericDoubleFunction<M> add(final GenericDoubleFunction<M> a, final GenericDoubleFunction<M> b) {
		return new GenericDoubleFunction<M>() {
			
			public double evaluate(M argument) {
				return (a.evaluate(argument) + b.evaluate(argument));
			}
			
		};
	}
	
	public GenericDoubleFunction<M> multiply(final double r, final GenericDoubleFunction<M> a) {
		return new GenericDoubleFunction<M>() {
			
			public double evaluate(M argument) {
				return (r * a.evaluate(argument));
			}
			
		};
	}

	public GenericDoubleFunction<M> negate(final GenericDoubleFunction<M> a) {
		return new GenericDoubleFunction<M>() {

			public double evaluate(M argument) {
				return (-a.evaluate(argument));
			}
			
		};
	}

	public GenericDoubleFunction<M> subtract(final GenericDoubleFunction<M> a, final GenericDoubleFunction<M> b) {
		return new GenericDoubleFunction<M>() {

			public double evaluate(M argument) {
				return (a.evaluate(argument) - b.evaluate(argument));
			}
			
		};
	}

	public GenericDoubleFunction<M> getAdditiveIdentity() {
		return new GenericDoubleFunction<M>() {

			public double evaluate(M argument) {
				return 0;
			}
			
		};
	}

	public void accumulate(GenericDoubleFunction<M> a, final GenericDoubleFunction<M> b) {
		throw new UnsupportedOperationException();
	}

	public void accumulate(GenericDoubleFunction<M> a, GenericDoubleFunction<M> b, double c) {
		throw new UnsupportedOperationException();
	}
}
