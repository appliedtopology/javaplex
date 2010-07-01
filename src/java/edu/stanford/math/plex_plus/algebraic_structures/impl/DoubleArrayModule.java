package edu.stanford.math.plex_plus.algebraic_structures.impl;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.DoubleLeftModule;
import edu.stanford.math.plex_plus.utility.ArrayUtility;

public class DoubleArrayModule implements DoubleLeftModule<double[]> {
	private final int dimension;
	
	public DoubleArrayModule(int dimension) {
		this.dimension = dimension;
	}
	
	@Override
	public double[] getAdditiveIdentity() {
		return new double[this.dimension];
	}
	
	@Override
	public double[] add(double[] a, double[] b) {
		return ArrayUtility.sum(a, b);
	}	

	@Override
	public double[] multiply(double r, double[] a) {
		return ArrayUtility.scalarMultiply(a, r);
	}

	@Override
	public double[] negate(double[] a) {
		return ArrayUtility.negate(a);
	}

	@Override
	public double[] subtract(double[] a, double[] b) {
		return ArrayUtility.difference(a, b);
	}

}
