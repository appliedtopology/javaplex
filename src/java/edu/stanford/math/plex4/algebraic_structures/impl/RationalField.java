package edu.stanford.math.plex4.algebraic_structures.impl;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericOrderedField;

public class RationalField extends GenericOrderedField<Fraction> {
	private RationalField() {}
	
	private static final RationalField instance = new RationalField();
	
	public static RationalField getInstance() {
		return instance;
	}
	
	@Override
	public Fraction add(Fraction a, Fraction b) {
		return a.add(b);
	}

	@Override
	public Fraction getOne() {
		return Fraction.ONE;
	}

	@Override
	public Fraction getZero() {
		return Fraction.ZERO;
	}

	@Override
	public Fraction multiply(Fraction a, Fraction b) {
		return a.multiply(b);
	}

	@Override
	public Fraction negate(Fraction a) {
		return a.negate();
	}

	@Override
	public Fraction subtract(Fraction a, Fraction b) {
		return a.subtract(b);
	}

	@Override
	public Fraction valueOf(int n) {
		return new Fraction(n);
	}

	@Override
	public Fraction divide(Fraction a, Fraction b) {
		return a.divide(b);
	}

	@Override
	public Fraction invert(Fraction a) {
		return a.reciprocal();
	}

	@Override
	public boolean isUnit(Fraction a) {
		return (a.getNumerator() != 0);
	}
	
	@Override
	public boolean isZero(Fraction a) {
		return (a.equals(Fraction.ZERO));
	}
	
	@Override
	public boolean isOne(Fraction a) {
		return (a.equals(Fraction.ONE));
	}
	
	@Override
	public int characteristic() {
		return 0;
	}

	public int compare(Fraction o1, Fraction o2) {
		return o1.compareTo(o2);
	}

	@Override
	public Fraction abs(Fraction a) {
		return a.abs();
	}
}
