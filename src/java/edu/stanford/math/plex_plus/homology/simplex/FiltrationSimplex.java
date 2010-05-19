/**
 * 
 */
package edu.stanford.math.plex_plus.homology.simplex;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import edu.stanford.math.plex_plus.utility.MathUtility;

/**
 * @author atausz
 *
 */
public class FiltrationSimplex implements Comparable<FiltrationSimplex>, AbstractSimplex {
	private final double filtrationIndex;
	private final Simplex simplex;
	
	public FiltrationSimplex(Simplex simplex, double filtrationIndex) {
		ExceptionUtility.verifyNonNull(simplex);
		this.simplex = simplex;
		this.filtrationIndex = filtrationIndex;
	}
	
	public FiltrationSimplex(int[] vertices, double filtrationIndex) {
		ExceptionUtility.verifyNonNull(vertices);
		this.simplex = new Simplex(vertices);
		this.filtrationIndex = filtrationIndex;
	}

	public double getFiltrationIndex() {
		return this.filtrationIndex;
	}
	
	public Simplex getSimplex() {
		return this.simplex;
	}

	@Override
	public int compareTo(FiltrationSimplex o) {
		int signum = MathUtility.signum(this.filtrationIndex - o.filtrationIndex);
		if (signum != 0) {
			return signum;
		}
		return this.simplex.compareTo(o.simplex);
	}

	@Override
	public Simplex[] getBoundaryArray() {
		return this.simplex.getBoundaryArray();
	}

	@Override
	public int getDimension() {
		return this.simplex.getDimension();
	}

	@Override
	public int[] getVertices() {
		return this.simplex.getVertices();
	}
	
	@Override
	public String toString() {
		return (this.filtrationIndex + ": " + this.simplex.toString());
	}
}
