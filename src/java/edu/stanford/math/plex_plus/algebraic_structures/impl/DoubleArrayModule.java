package edu.stanford.math.plex_plus.algebraic_structures.impl;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.DoubleLeftModule;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

public class DoubleArrayModule implements DoubleLeftModule<double[]> {
	private final int dimension;
	
	public DoubleArrayModule(int dimension) {
		this.dimension = dimension;
	}
	
	public double[] getAdditiveIdentity() {
		return new double[this.dimension];
	}
	
	public double[] add(double[] a, double[] b) {
		return ArrayUtility.sum(a, b);
	}	

	public double[] multiply(double r, double[] a) {
		return ArrayUtility.scalarMultiply(a, r);
	}

	public double[] negate(double[] a) {
		return ArrayUtility.negate(a);
	}

	public double[] subtract(double[] a, double[] b) {
		return ArrayUtility.difference(a, b);
	}

	public void accumulate(double[] a, double[] b) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.length, b.length);
		
		for (int i = 0; i < a.length; i++) {
			a[i] += b[i];
		}
	}

	public void accumulate(double[] a, double[] b, double c) {
		ExceptionUtility.verifyNonNull(a);
		ExceptionUtility.verifyNonNull(b);
		ExceptionUtility.verifyEqual(a.length, b.length);
		
		for (int i = 0; i < a.length; i++) {
			a[i] += c * b[i];
		}
	}

}
