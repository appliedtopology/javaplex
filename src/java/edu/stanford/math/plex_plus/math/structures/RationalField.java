package edu.stanford.math.plex_plus.math.structures;

import org.apache.commons.math.fraction.Fraction;

import edu.stanford.math.plex_plus.math.interfaces.GenericField;

public class RationalField extends GenericField<Fraction> {
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
}
