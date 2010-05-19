package edu.stanford.math.plex_plus.homology.simplex;


public interface AbstractSimplex {
	public int getDimension();
	public int[] getVertices();
	public Simplex[] getBoundaryArray();
}
