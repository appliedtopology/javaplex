package edu.stanford.math.plex4.homology.chain_basis;

import java.util.Arrays;

import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.plex4.utility.CRC;
import edu.stanford.math.plex4.utility.ExceptionUtility;

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
 * This class is designed to be a standard implementation of the 
 * PrimitiveBasisElement interface. It is also immutable and implements 
 * value semantics. 
 * 
 * @author Andrew Tausz
 *
 */
public class Simplex implements PrimitiveBasisElement {

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

	public int getDimension() {
		return (this.vertices.length - 1);
	}

	public int[] getVertices() {
		return this.vertices;
	}

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
	
	public int[] getBoundaryCoefficients() {
		if (this.vertices.length == 1) {
			return HomologyUtility.getDefaultBoundaryCoefficients(0);
		}
		return HomologyUtility.getDefaultBoundaryCoefficients(this.vertices.length);
	}

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

	public int hashCode() {
		return this.cachedHashCode;
	}

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
}
