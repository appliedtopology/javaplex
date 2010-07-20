package edu.stanford.math.plex4.algebraic_structures.impl;

import org.apache.commons.math.fraction.BigFraction;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericOrderedField;

public class BigRationalField extends GenericOrderedField<BigFraction> {
	
	private BigRationalField() {}
	
	private static final BigRationalField instance = new BigRationalField();
	
	public BigRationalField getInstance() {
		return instance;
	}
	
	@Override
	public BigFraction add(BigFraction a, BigFraction b) {
		return a.add(b);
	}

	@Override
	public BigFraction getOne() {
		return BigFraction.ONE;
	}

	@Override
	public BigFraction getZero() {
		return BigFraction.ZERO;
	}

	@Override
	public BigFraction multiply(BigFraction a, BigFraction b) {
		return a.multiply(b);
	}

	@Override
	public BigFraction negate(BigFraction a) {
		return a.negate();
	}

	@Override
	public BigFraction subtract(BigFraction a, BigFraction b) {
		return a.subtract(b);
	}

	@Override
	public BigFraction valueOf(int n) {
		return new BigFraction(n);
	}

	@Override
	public BigFraction divide(BigFraction a, BigFraction b) {
		return a.divide(b);
	}

	@Override
	public BigFraction invert(BigFraction a) {
		return a.reciprocal();
	}

	@Override
	public boolean isUnit(BigFraction a) {
		return (!a.equals(BigFraction.ZERO));
	}
	
	@Override
	public boolean isZero(BigFraction a) {
		return (a.equals(BigFraction.ZERO));
	}
	
	@Override
	public boolean isOne(BigFraction a) {
		return (a.equals(BigFraction.ONE));
	}
	
	@Override
	public int characteristic() {
		return 0;
	}

	public int compare(BigFraction o1, BigFraction o2) {
		return o1.compareTo(o2);
	}

	@Override
	public BigFraction abs(BigFraction a) {
		return a.abs();
	}
}
