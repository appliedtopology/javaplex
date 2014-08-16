package edu.stanford.math.plex4.utility;


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

	/**
	 * This function returns the floor of the base-2 logarithm
	 * of the input. The input must be strictly positive.
	 * 
	 * @param input the input argument
	 * @return the floor of the base-2 logarithm of the input 
	 */
	public static int floorLog2(int input) {
		ExceptionUtility.verifyPositive(input);
		int bitMask = 1;
		int bitPosition = 0;
		int maxBitPosition = 0;
		while (bitPosition < 31) {
			if ((bitMask & input) > 0) {
				maxBitPosition = bitPosition;
			}
			bitMask <<= 1;
			bitPosition++;
		}
		return maxBitPosition;
	}

	public static int minLog2(int input) {
		int bitMask = 1;
		int bitPosition = 0;
		int maxBitMask = 0;
		while (bitPosition < 31) {
			if ((bitMask & input) > 0) {
				maxBitMask = (bitMask & input);
			}
			bitMask <<= 1;
			bitPosition++;
		}
		return maxBitMask;
	}

	public static int maxLog2(int input) {
		int minLog2 = minLog2(input);
		if (minLog2 == input) {
			return input;
		} else {
			return minLog2 << 1;
		}
	}


	/**
	 * This function returns the great circle distance (geodesic distance)
	 * between two points on the unit sphere.
	 * 
	 * @param latitude1 the latitude of the first point
	 * @param longitude1 the longitude of the first point
	 * @param latitude2 the latitude of the second point
	 * @param longitude2 the longitude of the second point
	 * @return the geodesic distance between the two input points
	 */
	public static double greatCircleDistance(double latitude1, double longitude1, 
			double latitude2, double longitude2) {
		double phi1 = latitude1;
		double phi2 = latitude2;
		double lambda1 = longitude1;
		double lambda2 = longitude2;
		double deltaLambda = lambda1 - lambda2;
		double deltaPhi = phi1 - phi2;
		double sin1 = Math.sin(0.5 * deltaPhi);
		double sin2 = Math.sin(0.5 * deltaLambda);
		double A = sin1 * sin1;
		double B = sin2 * sin2;
		double C = Math.cos(phi1) * Math.cos(phi2);
		double distance = 2 * Math.asin(A + B * C);
		return distance;
	}

	/**
	 * This function returns the great circle distance between two 
	 * points on the unit sphere given their rectangular coordinates.
	 * 
	 * @param x the first point
	 * @param y the second point
	 * @return the distance between x and y along the unit sphere
	 */
	public static double greatCircleDistance(double[] x, double[] y) {
		double x1 = x[0];
		double y1 = x[1];
		double z1 = x[2];
		double phi1 = Math.atan2(y1, x1);
		double theta1 = Math.acos(z1 / Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1));
		double latitude1 = 0.5 * Math.PI - theta1;

		double x2 = y[0];
		double y2 = y[1];
		double z2 = y[2];
		double phi2 = Math.atan2(y2, x2);
		double theta2 = Math.acos(z2 / Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2));
		double latitude2 = 0.5 * Math.PI - theta2;

		return MathUtility.greatCircleDistance(latitude1, phi1, latitude2, phi2);		
	}

	/**
	 * This function computes the Huber penalty function of an 
	 * input. The Huber penalty function is equal to x^2 for |x| <= M, 
	 * and linear for |x| > M.
	 * @param x the value at which to compute the Huber penalty function
	 * @param M the parameter which determines the linear/quadratic region
	 * @return The Huber penalty function at x
	 */
	public static double huberPenaltyFunction(double x, double M) {
		if (Math.abs(x) <= M) {
			return x * x;
		} else {
			return M * (2 * Math.abs(x) - M);
		}
	}

	public static double absApproximation(double x, double M) {
		return huberPenaltyFunction(x, M) / (2 * M);
	}

	public static double maxApproximation(double x, double y, double M) {
		return 0.5 * (absApproximation(x - y, M) + x + y);
	}

	/**
	 * This function evaluates the standard gaussian density
	 * with mean 0 and variance 1.
	 * 
	 * @param x the point to evaluate at
	 * @return the standard normal density evaluated at x
	 */
	public static double gaussianDensity(double x) {
		double constant = Math.sqrt(2 * Math.PI);
		return (Math.exp(0.5 * x * x) / constant);
	}

	/**
	 * This function evaluates the 1-dimensional gaussian
	 * density function with specified mean and standard deviation.
	 * 
	 * @param x the point to evaluate at
	 * @param mean the mean of the distribution
	 * @param standardDeviation the standard deviation of the distribution
	 * @return the gaussian density evaluated at x
	 */
	public static double gaussianDensity(double x, double mean, double standardDeviation) {
		return (gaussianDensity((x - mean) / standardDeviation) / standardDeviation);
	}
	
	
	public static double dotProduct(double[] a, double[] b) {
		double result = 0;
		
		for(int i = 0; i < a.length && i < b.length; i++) {
			result += a[i] * b[i];
		}
		
		return result;
	}
}
