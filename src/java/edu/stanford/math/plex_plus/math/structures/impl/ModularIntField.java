package edu.stanford.math.plex_plus.math.structures.impl;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.math.plex_plus.math.structures.interfaces.IntField;
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
		return ((a * this.inverses[b % p]) % p);
	}

	@Override
	public int invert(int a) {
		if ((a % p) == 0) {
			throw new ArithmeticException();
		}
		a = a % p;
		if (a < 0) {
			a += p;
		}
		return this.inverses[a];
	}

	@Override
	public int add(int a, int b) {
		return (a + b) % p;
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
		return (a * b) % p;
	}

	@Override
	public int negate(int a) {
		return (-a) % p;
	}

	@Override
	public int subtract(int a, int b) {
		return (a - b) % p;
	}

	@Override
	public int valueOf(int n) {
		return n % p;
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
