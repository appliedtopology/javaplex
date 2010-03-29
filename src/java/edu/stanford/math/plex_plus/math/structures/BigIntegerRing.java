/**
 * 
 */
package edu.stanford.math.plex_plus.math.structures;

import java.math.BigInteger;

import edu.stanford.math.plex_plus.math.interfaces.GenericRing;

/**
 * @author Andrew Tausz
 *
 */
public class BigIntegerRing extends GenericRing<BigInteger> {
	
	private BigIntegerRing() {}
	
	private static final BigIntegerRing instance = new BigIntegerRing();
	
	public BigIntegerRing getInstance() {
		return instance;
	}
	
	@Override
	public BigInteger add(BigInteger a, BigInteger b) {
		return a.add(b);
	}

	@Override
	public BigInteger getOne() {
		return BigInteger.ONE;
	}
	
	@Override
	public BigInteger getZero() {
		return BigInteger.ZERO;
	}

	@Override
	public boolean isUnit(BigInteger a) {
		return a.abs().equals(BigInteger.ONE);
	}
	
	@Override
	public boolean isZero(BigInteger a) {
		return a.equals(BigInteger.ZERO);
	}
	
	@Override
	public boolean isOne(BigInteger a) {
		return a.equals(BigInteger.ONE);
	}

	@Override
	public BigInteger multiply(BigInteger a, BigInteger b) {
		return a.multiply(b);
	}

	@Override
	public BigInteger negate(BigInteger a) {
		return a.negate();
	}

	@Override
	public BigInteger subtract(BigInteger a, BigInteger b) {
		return a.subtract(b);
	}

	@Override
	public BigInteger valueOf(int n) {
		return BigInteger.valueOf(n);
	}
}
