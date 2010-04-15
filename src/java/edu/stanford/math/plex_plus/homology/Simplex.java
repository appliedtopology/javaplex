package edu.stanford.math.plex_plus.homology;

import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.CRC;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This abstract class defines the functionality of a simplex.
 * An implementation must actually store the vertices (or some 
 * representation of the simplex) in an appropriate form.
 * 
 * TODO: Produce bit-packed implementations as in the previous version
 * of plex.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class Simplex implements Comparable<Simplex> {
	private int filtrationIndex = 0;
	
	public abstract int[] getVertices();
	public abstract int getDimension();
	public abstract Simplex[] getBoundaryArray();
	
	public int getFiltrationIndex() {
		return this.filtrationIndex;
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
