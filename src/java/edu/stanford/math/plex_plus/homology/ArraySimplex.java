
package edu.stanford.math.plex_plus.homology;

import java.util.Arrays;

import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class implements the Simplex abstract class.
 * It stores the simplex as an array of vertices.
 * 
 * @author Andrew Tausz
 *
 */
public class ArraySimplex extends Simplex {
	final int[] vertices;
	
	public ArraySimplex(int[] vertices) {
		ExceptionUtility.verifyNonNull(vertices);
		this.vertices = vertices;
		Arrays.sort(this.vertices);
	}
	
	@Override
	public int dimension() {
		return (this.vertices.length - 1);
	}

	@Override
	public int[] getVertices() {
		return this.vertices;
	}
}
