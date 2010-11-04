package edu.stanford.math.plex4.utility;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import gnu.trove.TIntHashSet;


/**
 * This class contains static members for generating random numbers.
 * 
 * @author Andrew Tausz
 * @author Tim Harrington
 *
 */
public class RandomUtility {
	
	private static Normal normalGenerator = new Normal(0, 1, new cern.jet.random.engine.MersenneTwister(Uniform
			.staticNextIntFromTo(0, Integer.MAX_VALUE)));
	private static Uniform uniformGenerator = new Uniform(new cern.jet.random.engine.MersenneTwister(Uniform
			.staticNextIntFromTo(0, Integer.MAX_VALUE)));

	public static void initializeWithSeed(int seed) {
		normalGenerator = new Normal(0, 1, new cern.jet.random.engine.MersenneTwister(seed));
		uniformGenerator = new Uniform(new cern.jet.random.engine.MersenneTwister(seed));
	}
	
	/**
	 * This function returns a sample of a Bernoulli random
	 * variable with parameter p.
	 * 
	 * @param p The probability of returning 1
	 * @return a Bernoulli random variable with probability p
	 */
	public static int nextBernoulli(double p) {
		ExceptionUtility.verifyNonNegative(p);
		ExceptionUtility.verifyLessThanOrEqual(p, 1);
		if (p == 1) {
			return 1;
		} else if (p == 0) {
			return 0;
		} else if (uniformGenerator.nextDouble() <= p) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * This function returns a uniform random number
	 * in the open interval (0, 1).
	 * 
	 * @return a double containing a uniform random number in (0, 1)
	 */
	public static double nextUniform() {
		return uniformGenerator.nextDouble();
	}

	/**
	 * This function returns a normal random number.
	 * 
	 * @return a normally distributed random number
	 */
	public static double nextNormal() {
		return normalGenerator.nextDouble();
	}

	/**
	 * Returns a uniformly distributed integer in the inclusive interval 
	 * {from, ..., to}
	 * 
	 * @param from the start integer
	 * @param to the end integer
	 * @return an integer uniformly distributed in {from, ..., to}
	 */
	public static int nextUniformInt(int from, int to) {
		return uniformGenerator.nextIntFromTo(from, to);
	}

	/**
	 * This function returns an integer uniformly distributed
	 * in the set {from, ..., to} \ {exclusion}.
	 * 
	 * @param from the lowest member of the set
	 * @param to the highest member of the set
	 * @param exclusion the member to exclude
	 * @return an integer uniformly distributed in {from, ..., to} \ {exclusion}
	 */
	public static int nextUniformIntExcluded(int from, int to, int exclusion) {
		if ((exclusion < from) || (exclusion > to)) {
			throw new IllegalArgumentException();
		}
		int randomInt = nextUniformInt(from, to - 1);
		// if randomInt >= exclude, increment it by 1
		if (randomInt >= exclusion) {
			randomInt++;
		}
		return randomInt;
	}

	/**
	 * This function returns an array of uniformly distributed
	 * random numbers.
	 * 
	 * @param length the length of the array to create
	 * @return an array containing uniformly distributed random numbers
	 */
	public static double[] uniformArray(int length) {
		ExceptionUtility.verifyPositive(length);
		double[] values = new double[length];
		for (int i = 0; i < length; i++) {
			values[i] = nextUniform();
		}
		return values;
	}

	/**
	 * This function returns an array of normally distributed
	 * random numbers.
	 * 
	 * @param length the length of the array to create
	 * @return an array containing normally distributed random numbers
	 */
	public static double[] normalArray(int length) {
		ExceptionUtility.verifyPositive(length);
		double[] values = new double[length];
		for (int i = 0; i < length; i++) {
			values[i] = nextNormal();
		}
		return values;
	}

	/**
	 * This function returns a matrix containing uniformly
	 * distributed random numbers.
	 * 
	 * @param rows the number of rows in the returned matrix
	 * @param columns the number of columns in the returned matrix
	 * @return a matrix containing uniformly distributed random numbers
	 */
	public static double[][] uniformMatrix(int rows, int columns) {
		ExceptionUtility.verifyPositive(rows);
		ExceptionUtility.verifyPositive(columns);
		double[][] values = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = nextUniform();
			}
		}
		return values;
	}

	/**
	 * This function returns a matrix containing normally
	 * distributed random numbers.
	 * 
	 * @param rows the number of rows in the returned matrix
	 * @param columns the number of columns in the returned matrix
	 * @return a matrix containing normally distributed random numbers
	 */
	public static double[][] normalMatrix(int rows, int columns) {
		ExceptionUtility.verifyPositive(rows);
		ExceptionUtility.verifyPositive(columns);
		double[][] values = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = nextNormal();
			}
		}
		return values;
	}

	/**
	 * This function computes a random permutation of the
	 * input array, and then applies the permutation. It
	 * performs the permutation in-place.
	 * 
	 * @param values the array of integers to permute
	 */
	public static int[] permute(int[] values) {
		int n = values.length;
		int k, temp;
		int[] permutedValues = values;
		while (n > 1) {
			n--;
			k = nextUniformInt(0, n);
			temp = permutedValues[k];
			permutedValues[k] = permutedValues[n];
			permutedValues[n] = temp;
		}
		return permutedValues;
	}

	/**
	 * This function computes a random subset of size subsetSize from the
	 * set {0, ..., selectionSetSize - 1}.
	 * 
	 * @param subsetSize the size of the subset to return
	 * @param selectionSetSize the size of the set to select from
	 * @return a subset of size subsetSize from the set {0, ..., selectionSetSize - 1}
	 */
	public static TIntHashSet randomSubset(int subsetSize, int selectionSetSize) {	
		TIntHashSet result = new TIntHashSet();
		
		if (subsetSize >= selectionSetSize) {
			for (int i = 0; i < selectionSetSize; i++) {
				result.add(i);
			}
			return result;
		}
		
		if (subsetSize > selectionSetSize / 2) {
			TIntHashSet negativeSet = randomSubset(selectionSetSize - subsetSize, selectionSetSize);
			for (int i = 0; i < selectionSetSize; i++) {
				if (!negativeSet.contains(i)) {
					result.add(i);
				}
			}
		} else {
			while (result.size() < subsetSize) {
				int randomPosition = RandomUtility.nextUniformInt(0, selectionSetSize - 1);
				if (!result.contains(randomPosition)) {
					result.add(randomPosition);
				}
			}
		}
		
		return result;
	}
}
