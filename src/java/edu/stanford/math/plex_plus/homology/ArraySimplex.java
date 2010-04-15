
package edu.stanford.math.plex_plus.homology;

import java.util.Arrays;

import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
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
	public int getDimension() {
		return (this.vertices.length - 1);
	}

	@Override
	public int[] getVertices() {
		return this.vertices;
	}

	@Override
	public Simplex[] getBoundaryArray() {
		if (this.getDimension() == 0) {
			return new Simplex[0];
		}
		Simplex[] boundaryArray = new Simplex[this.vertices.length];		
		for (int i = 0; i < this.vertices.length; i++) {
			boundaryArray[i] = new ArraySimplex(HomologyUtility.removeIndex(this.vertices, i));
		}
		return boundaryArray;
	}
}
