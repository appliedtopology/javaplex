package edu.stanford.math.plex_plus.homology.simplex;

import java.util.Arrays;

import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.CRC;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This class defines the functionality of a simplex. It stores the verties 
 * in an array of ints.
 * 
 * TODO: Produce bit-packed implementations as in the previous version
 * of plex. (MAYBE)
 * 
 * @author Andrew Tausz
 *
 */
public class Simplex implements Comparable<Simplex>, AbstractSimplex {
	final int[] vertices;
	
	public Simplex(int[] vertices) {
		ExceptionUtility.verifyNonNull(vertices);
		this.vertices = vertices;
		Arrays.sort(this.vertices);
	}
	
	public int getDimension() {
		return (this.vertices.length - 1);
	}

	public int[] getVertices() {
		return this.vertices;
	}

	public Simplex[] getBoundaryArray() {
		if (this.getDimension() == 0) {
			return new Simplex[0];
		}
		Simplex[] boundaryArray = new Simplex[this.vertices.length];		
		for (int i = 0; i < this.vertices.length; i++) {
			boundaryArray[i] = new Simplex(HomologyUtility.removeIndex(this.vertices, i));
		}
		return boundaryArray;
	}
	
	
	@Override
	public int compareTo(Simplex o) {
		ExceptionUtility.verifyNonNull(o);
		return HomologyUtility.compareIntArrays(this.getVertices(), o.getVertices());
	}
	
	/*
	 * TODO: 
	 * - possibly cache the hashcode for faster performance
	 * 
	 */
	
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
		return CRC.hash32(this.getVertices());
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
}
