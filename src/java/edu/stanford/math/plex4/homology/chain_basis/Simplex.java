package edu.stanford.math.plex4.homology.chain_basis;

import java.util.Arrays;

import edu.stanford.math.plex4.homology.utility.HomologyUtility;
import edu.stanford.math.primitivelib.utility.CRC;

/**
 * <p>This class implements the functionality of a simplex. A simplex
 * is an n-dimensional polytope which is the convex hull of its 
 * vertices. For our purposes, we simply represent a simplex by its
 * vertices which are labeled by integers. For example, a 2-simplex 
 * could be [0, 5, 9].</p>
 * 
 * <p>The vertices of a simplex are specified by non-negative integers. It uses
 * an array implementation to store the indices of the vertices of the 
 * simplex.</p> 
 * 
 * <p>This class is designed to be a standard implementation of the 
 * PrimitiveBasisElement interface. It is also immutable and implements 
 * value semantics.</p> 
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
	public Simplex(final int[] vertices) {

		// store the vertices
		this.vertices = vertices.clone();

		// make sure that the vertices are sorted
		Arrays.sort(this.vertices);

		// compute the hash code via CRC hashing
		this.cachedHashCode = CRC.hash32(this.getVertices());
	}
	
	public static Simplex makeSimplex(int... args) {
		return new Simplex(args);
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement#getDimension()
	 */
	public int getDimension() {
		return (this.vertices.length - 1);
	}

	/**
	 * This function returns an array containing the vertices of the simplex.
	 * 
	 * @return an array containing the vertices of the simplex
	 */
	public int[] getVertices() {
		return this.vertices;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement#getBoundaryArray()
	 */
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
	
	/* (non-Javadoc)
	 * @see edu.stanford.math.plex4.homology.chain_basis.PrimitiveBasisElement#getBoundaryCoefficients()
	 */
	public int[] getBoundaryCoefficients() {
		if (this.vertices.length == 1) {
			return HomologyUtility.getDefaultBoundaryCoefficients(0);
		}
		return HomologyUtility.getDefaultBoundaryCoefficients(this.vertices.length);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Simplex)) {
			return false;
		}
		Simplex o = (Simplex) obj;
		if (this.cachedHashCode != o.cachedHashCode) {
			return false;
		}
		return (HomologyUtility.compareIntArrays(this.getVertices(), o.getVertices()) == 0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.cachedHashCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
