package edu.stanford.math.plex_plus.math.structures.impl;

import edu.stanford.math.plex_plus.math.structures.interfaces.GenericRing;

public class StandardIntegerRing extends GenericRing<Integer> {
	
	private StandardIntegerRing() {}
	
	private static final StandardIntegerRing instance = new StandardIntegerRing();
	
	public static StandardIntegerRing getInstance() {
		return instance;
	}
	
	@Override
	public Integer add(Integer a, Integer b) {
		return a + b;
	}

	@Override
	public Integer getOne() {
		return Integer.valueOf(1);
	}

	@Override
	public Integer getZero() {
		return Integer.valueOf(0);
	}

	@Override
	public boolean isOne(Integer a) {
		return a.equals(Integer.valueOf(1));
	}

	@Override
	public boolean isUnit(Integer a) {
		return (a.equals(Integer.valueOf(1)) || a.equals(Integer.valueOf(-1)));
	}

	@Override
	public boolean isZero(Integer a) {
		return a.equals(Integer.valueOf(0));
	}

	@Override
	public Integer multiply(Integer a, Integer b) {
		return a * b;
	}

	@Override
	public Integer negate(Integer a) {
		return -a;
	}

	@Override
	public Integer subtract(Integer a, Integer b) {
		return a - b;
	}

	@Override
	public Integer valueOf(int n) {
		return Integer.valueOf(n);
	}

}
