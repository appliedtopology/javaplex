package edu.stanford.math.plex_plus.homology.simplex;

import java.util.Arrays;

import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.CRC;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements the functionality of a simplex. A simplex
 * is an n-dimensional polytope which is the convex hull of its 
 * vertices. For our purposes, we simply represent a simplex by its
 * vertices which are labeled by integers. For example, a 2-simplex 
 * could be [0, 5, 9].
 * 
 * The vertices of a simplex are specified by non-negative integers. It uses
 * an array implementation to store the indices of the vertices of the 
 * simplex. 
 * 
 * This class is designed to be the standard implementation of the AbstractSimplex
 * interface. It is also immutable and implements value semantics. It also
 * implements the Comparable interface. The ordering of simplicies is
 * first defined by dimension, and then by dictionary ordering of its
 * vertices.
 * 
 * TODO: Previous versions of plex used bit-packed implementations. 
 * Maybe we can do this, although it seems that we can get better mileage
 * from using improved algorithms.
 * 
 * @author Andrew Tausz
 *
 */
public class Simplex implements ChainBasisElement {

	/**
	 * This stores the actual vertices of the simplex.
	 */
	private final int[] vertices;

	/**
	 * Stored cache of the hash code to prevent recomputing it.
	 */
	private final int cachedHashCode;

	/**
	 * This constructor initializes the simplex from a supplied array
	 * of integers.
	 * 
	 * @param vertices the vertices to 
	 */
	public Simplex(int[] vertices) {
		ExceptionUtility.verifyNonNull(vertices);

		// store the vertices
		this.vertices = vertices;

		// make sure that the vertices are sorted
		Arrays.sort(this.vertices);

		// compute the hash code via CRC hashing
		this.cachedHashCode = CRC.hash32(this.getVertices());
	}

	@Override
	public int getDimension() {
		return (this.vertices.length - 1);
	}

	//@Override
	public int[] getVertices() {
		return this.vertices;
	}

	@Override
	public Simplex[] getBoundaryArray() {
		// if this a point, return an empty array
		if (this.getDimension() == 0) {
			return new Simplex[0];
		}

		Simplex[] boundaryArray = new Simplex[this.vertices.length];		
		for (int i = 0; i < this.vertices.length; i++) {
			boundaryArray[i] = new Simplex(HomologyUtility.removeIndex(this.vertices, i));
		}
		return boundaryArray;
	}

	//@Override
	public int getCoboundaryCoefficient(ChainBasisElement element) {
		if (!(element instanceof Simplex)) {
			return 0;
		}
		Simplex proposedBoundaryElement = (Simplex) element;

		// make sure that this has dimension 1 greater than the proposal
		if (this.getDimension() != (proposedBoundaryElement.getDimension() + 1)) {
			return 0;
		}

		int coefficient = 1;
		for (int i = 0; i < this.vertices.length; i++) {
			int[] testArray = HomologyUtility.removeIndex(this.vertices, i);
			if (HomologyUtility.compareIntArrays(testArray, proposedBoundaryElement.getVertices()) == 0) {
				return coefficient;
			}
			coefficient *= -1;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Simplex)) {
			return false;
		}
		Simplex o = (Simplex) obj;
		return (HomologyUtility.compareIntArrays(this.getVertices(), o.getVertices()) == 0);
	}

	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		int[] vertices = this.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			if (i > 0) {
				builder.append(',');
			}
			builder.append(vertices[i]);
		}
		builder.append(']');
		return builder.toString();
	}

	@Override
	public int[] getBoundaryCoefficients() {
		if (this.vertices.length > 1) {
			return HomologyUtility.getDefaultBoundaryCoefficients(this.vertices.length);
		} else {
			return new int[0];
		}
	}
}
