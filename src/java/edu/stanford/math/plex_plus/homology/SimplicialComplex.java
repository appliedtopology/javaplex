package edu.stanford.math.plex_plus.homology;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import edu.stanford.math.plex_plus.datastructures.LabeledGrid;
import edu.stanford.math.plex_plus.utility.ArrayUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;

/**
 * This abstract class defines functionality for a simplicial complex.
 * 
 * @author Andrew Tausz
 *
 */
public abstract class SimplicialComplex {
	public abstract void addSimplex(Simplex simplex);
	public abstract int getDimension();
	public abstract SortedSet<Simplex> getSkeleton(int k);
	
	public abstract int[][] getDenseBoundaryMatrix(int k);
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int d = 0; d <= this.getDimension(); d++) {
			builder.append(d + ": ");
			builder.append('{');
			SortedSet<Simplex> skeleton = this.getSkeleton(d);
			for (Simplex simplex : skeleton) {
				builder.append(simplex.toString());
				builder.append(' ');
			}
			builder.append("}\n");
		}
		return builder.toString();
	}
	
	/**
	 * TODO: Rewrite
	 * 
	 * @param k
	 * @return
	 */
	LabeledGrid<Simplex, Simplex, Integer> getBoundaryMatrix(int k) {
		ExceptionUtility.verifyPositive(k);
		List<Simplex> kSkeleton = new ArrayList<Simplex>();
		kSkeleton.addAll(this.getSkeleton(k));
		
		LabeledGrid<Simplex, Simplex, Integer> grid = new LabeledGrid<Simplex, Simplex, Integer>();
		
		for (int i = 0; i < kSkeleton.size(); i++) {
			int[] vertices = kSkeleton.get(i).getVertices();
			for (int j = 0; j < vertices.length; j++) {
				int[] boundaryVertex = ArrayUtility.removeIndex(vertices, j);
				grid.setEntry(new ArraySimplex(boundaryVertex), kSkeleton.get(i), (j % 2 == 0 ? 1 : -1));
			}
		}
		return grid;
	}
}
