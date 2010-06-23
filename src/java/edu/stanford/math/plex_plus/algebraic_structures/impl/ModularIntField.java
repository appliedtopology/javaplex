package edu.stanford.math.plex_plus.algebraic_structures.impl;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntField;
import edu.stanford.math.plex_plus.utility.MathUtility;

/**
 * This class implements finite field arithmetic. It uses a base type of
 * int. 
 * 
 * @author Andrew Tausz
 *
 */
public final class ModularIntField extends IntField {
	private final int p;
	private final int[] inverses;
	private static Map<Integer, ModularIntField> map = new HashMap<Integer, ModularIntField>();
	
	public static ModularIntField getInstance(int p) {
		if (map.containsKey(p)) {
			return map.get(p);
		} else {
			ModularIntField finiteField = new ModularIntField(p);
			map.put(p, finiteField);
			return finiteField;
		}
	}
	
	private ModularIntField(int p) {
		this.p = p;
		this.inverses = MathUtility.modularInverses(p);
	}

	@Override
	public int divide(int a, int b) {
		if ((b % p) == 0) {
			throw new ArithmeticException();
		}
		int index = b % p;
		if (index < 0) {
			index += p;
		}
		return ((a * this.inverses[index]) % p);
	}

	@Override
	public int invert(int a) {
		if ((a % p) == 0) {
			throw new ArithmeticException();
		}
		int r = a % p;
		if (r < 0) {
			r += p;
		}
		return this.inverses[r];
	}

	@Override
	public int add(int a, int b) {
		int r = (a + b) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public int getOne() {
		return 1;
	}

	@Override
	public int getZero() {
		return 0;
	}

	@Override
	public int multiply(int a, int b) {
		int r = (a * b) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public int negate(int a) {
		int r = (-a) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public int subtract(int a, int b) {
		int r = (a - b) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public int valueOf(int n) {
		int r = n % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public boolean isUnit(int a) {
		return (a % p != 0);
	}
	
	@Override
	public boolean isZero(int a) {
		return (a % p == 0);
	}
	
	@Override
	public boolean isOne(int a) {
		return (a % p == 1);
	}
	
}
