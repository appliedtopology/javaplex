package edu.stanford.math.plex4.algebraic_structures.impl;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.math.plex4.algebraic_structures.interfaces.GenericField;
import edu.stanford.math.plex4.utility.MathUtility;

public class ModularIntegerField extends GenericField<Integer> {
	private final int p;
	private final int[] inverses;
	private static Map<Integer, ModularIntegerField> map = new HashMap<Integer, ModularIntegerField>();
	
	public static ModularIntegerField getInstance(Integer p) {
		if (map.containsKey(p)) {
			return map.get(p);
		} else {
			ModularIntegerField finiteField = new ModularIntegerField(p);
			map.put(p, finiteField);
			return finiteField;
		}
	}
	
	private ModularIntegerField(Integer p) {
		this.p = p;
		this.inverses = MathUtility.modularInverses(p);
	}

	@Override
	public Integer divide(Integer a, Integer b) {
		if ((b % p) == 0) {
			throw new ArithmeticException();
		}
		Integer index = b % p;
		if (index < 0) {
			index += p;
		}
		return ((a * this.inverses[index]) % p);
	}

	@Override
	public Integer invert(Integer a) {
		if ((a % p) == 0) {
			throw new ArithmeticException();
		}
		Integer r = a % p;
		if (r < 0) {
			r += p;
		}
		return this.inverses[r];
	}

	@Override
	public Integer add(Integer a, Integer b) {
		Integer r = (a + b) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public Integer getOne() {
		return 1;
	}

	@Override
	public Integer getZero() {
		return 0;
	}

	@Override
	public Integer multiply(Integer a, Integer b) {
		Integer r = (a * b) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public Integer negate(Integer a) {
		Integer r = (-a) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public Integer subtract(Integer a, Integer b) {
		Integer r = (a - b) % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public Integer valueOf(int n) {
		Integer r = n % p;
		if (r < 0) {
			r += p;
		}
		return r;
	}

	@Override
	public boolean isUnit(Integer a) {
		return (a % p != 0);
	}
	
	@Override
	public boolean isZero(Integer a) {
		return (a % p == 0);
	}
	
	@Override
	public boolean isOne(Integer a) {
		return (a % p == 1);
	}

	@Override
	public int characteristic() {
		return this.p;
	}
}
