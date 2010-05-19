package edu.stanford.math.plex_plus.datastructures.pairs;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.MathUtility;

public class DoubleLongPair implements Comparable<DoubleLongPair> {
	protected final double first;
	protected final long second;
	
	public DoubleLongPair(double first, long second) {
		ExceptionUtility.verifyNonNull(first);
		ExceptionUtility.verifyNonNull(second);
		this.first = first;
		this.second = second;
	}
	
	public DoubleLongPair(DoubleLongPair pair) {
		ExceptionUtility.verifyNonNull(pair);
		this.first = pair.first;
		this.second = pair.second;
	}
	
	public double getFirst() {
		return this.first;
	}
	
	public long getSecond() {
		return this.second;
	}
	
	@Override
	public String toString() {
		return ("(" + first + ", " + second + ")");
	}
	
	@Override
	public int compareTo(DoubleLongPair o) {
		ExceptionUtility.verifyNonNull(o);
		int comparison = MathUtility.signum(this.first - o.first);
		if (comparison != 0) {
			return comparison;
		} else {
			return MathUtility.signum(this.second - o.second);
		}
	}
}
