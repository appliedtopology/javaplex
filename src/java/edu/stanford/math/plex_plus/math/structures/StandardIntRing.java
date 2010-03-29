/**
 * 
 */
package edu.stanford.math.plex_plus.math.structures;

import edu.stanford.math.plex_plus.math.interfaces.IntRing;

/**
 * @author Andrew Tausz
 *
 */
public class StandardIntRing extends IntRing {

	private StandardIntRing() {}
	
	private static final StandardIntRing instance = new StandardIntRing();
	
	public static StandardIntRing getInstance() {
		return instance;
	}
	
	@Override
	public int add(int a, int b) {
		return a + b;
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
		return a * b;
	}

	@Override
	public int negate(int a) {
		return -a;
	}

	@Override
	public int subtract(int a, int b) {
		return a - b;
	}

	@Override
	public int valueOf(int n) {
		return n;
	}

	@Override
	public boolean isUnit(int a) {
		return ((a == 1) || (a == -1));
	}
	
	@Override
	public boolean isZero(int a) {
		return (a == 0);
	}
	
	@Override
	public boolean isOne(int a) {
		return (a == 1);
	}
}
