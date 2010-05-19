package edu.stanford.math.plex_plus.utility;

/**
 * This class contains various static utility functions
 * relating to math.
 * 
 * @author Andrew Tausz
 *
 */
public class MathUtility {
	public static long euclideanGcd(long a, long b) {
		while (b != 0) {
			long t = b;
			b = a % b;
			a = t;
		}
		return a;
	}
	
	public static int signum(long l) {
		if (l > 0) {
			return 1;
		} else if (l < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public static int signum(double l) {
		if (l > 0) {
			return 1;
		} else if (l < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * This function returns the multiplicative inverses of the integers
	 * [0, 1, ..., p-1] in mod p arithmetic. Note that the 0-th index is 
	 * simply set to 0 for convenience so that the inverse of n is in the n-th
	 * component of the returned array.
	 * 
	 * TODO:
	 * This is an inefficient way to compute the inverses (it was inherited from 
	 * the original Plex implementation). It should be rewritten. 
	 * 
	 * @param p
	 * @return an array containing the multiplicative inverses in the field Z/pZ
	 */
	public static int[] modularInverses(int p) {
		ExceptionUtility.verifyGreaterThanOrEqual(p, 2);
		int[] inverses = new int[p];
		for (int i = 1; i < p; i++) {
			int inverse = 0;
			for (int j = 1; j < p; j++) {
				if (((j * i) % p) == 1) {
					inverse = j;
					break;
				}
			}

			if (inverse == 0) {
				throw new IllegalArgumentException(p + " is not a prime.");
			}

			inverses[i] = inverse;
		}
		
		return inverses;
	}
}
